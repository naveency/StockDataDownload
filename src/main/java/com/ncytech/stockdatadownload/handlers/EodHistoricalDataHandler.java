package com.ncytech.stockdatadownload.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncytech.stockdatadownload.model.Security;
import com.ncytech.stockdatadownload.repository.SecurityRepository;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EodHistoricalDataHandler {
    private WebClient webClient;
    private SecurityRepository securityRepository;
    @Value("${API_KEY}")
    private String API_KEY;
    private static final List<String> EXCL_EXCHANGES = List.of("PINK", "OTCQB", "OTCCE", "OTCGREY", "OTCQX", "OTCMKTS");
    private static final List<String> INCLUDED_TYPES = List.of("Common Stock", "ETF", "Futures");

    private static final List<String> NO_SECTOR_CALLS = List.of("ETF", "Futures");

    public EodHistoricalDataHandler(WebClient.Builder builder, SecurityRepository securityRepository) {
        this.webClient = builder.baseUrl("https://eodhistoricaldata.com/api/").
                codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)).build();
        this.securityRepository = securityRepository;
    }

    public List<Security> getSymbolsFromExchange(String exchangeName) {
        Mono<List<Security>> result = webClient.get().uri(uriBuilder -> uriBuilder.path("exchange-symbol-list/US").
                queryParam("api_token", API_KEY).
                queryParam("fmt", "json").
                build()).retrieve().bodyToMono(new ParameterizedTypeReference<List<Security>>() {
        });

        List<Security> symbols = result.block().parallelStream().filter(security ->
                !EXCL_EXCHANGES.contains(security.getExchange()) && INCLUDED_TYPES.contains(security.getType())
        ).collect(Collectors.toList());

        symbols = symbols.stream().map(security -> {
            if (!NO_SECTOR_CALLS.contains(security.getType())) {
                String[] sectorIndustry = getSectorIndustry(security.getTicker(), security.getExchange());
                security.setSector(sectorIndustry[0]);
                security.setIndustry(sectorIndustry[1]);
            } else {
                security.setIndustry("NI");
                security.setSector("NS");
            }

            return security;
        }).collect(Collectors.toList());

        return symbols;
    }

    public String[] getSectorIndustry(String ticker, String exchange) {
        String[] sectorIndustry = {"NS", "NI"};
        String[][] filters = {{"code", "=", ticker}, {"exchange", "=", exchange}};
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String filterStr;
        try {
            filterStr = mapper.writeValueAsString(filters);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode screenerData = webClient.get().uri(uriBuilder -> uriBuilder.path("screener")
                .queryParam("filters", filterStr)
                .queryParam("api_token", API_KEY).
                queryParam("fmt", "json").build()).retrieve().bodyToMono(JsonNode.class).block();

        JsonNode data = screenerData.get("data");
        if (data != null) {
            JsonNode tickerData = data.get(0);
            if (tickerData != null) {
                SectorIndustry tempSectorIndustry = mapper.convertValue(tickerData, SectorIndustry.class);

                if (!StringUtils.isAllBlank(tempSectorIndustry.getSector())) {
                    sectorIndustry[0] = tempSectorIndustry.getSector();
                    sectorIndustry[1] = tempSectorIndustry.getIndustry();
                }
            }
        }

        return sectorIndustry;
    }
}

@Data
class SectorIndustry {
    @JsonProperty("sector")
    private String sector;
    @JsonProperty("industry")
    private String industry;

}
