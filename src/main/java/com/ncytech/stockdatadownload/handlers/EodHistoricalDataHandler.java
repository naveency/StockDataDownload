package com.ncytech.stockdatadownload.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncytech.stockdatadownload.model.Daily;
import com.ncytech.stockdatadownload.model.Security;
import com.ncytech.stockdatadownload.repository.DailyRepository;
import com.ncytech.stockdatadownload.repository.SecurityRepository;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class EodHistoricalDataHandler {
    private static final Logger logger = Logger.getLogger(EodHistoricalDataHandler.class.getName());
    private WebClient webClient;
    private SecurityRepository securityRepository;
    private DailyRepository dailyRepository;
    @Value("${API_KEY}")
    private String API_KEY;
    private static final List<String> EXCL_EXCHANGES = List.of("PINK", "OTCQB", "OTCCE", "OTCGREY", "OTCQX", "OTCMKTS", "OTCBB");
    private static final List<String> INCLUDED_TYPES = List.of("Common Stock", "ETF", "Futures");

    private static final List<String> NO_SECTOR_CALLS = List.of("ETF", "Futures");

    private static final LocalDate START_DATE = LocalDate.of(2000,01,01);

    public EodHistoricalDataHandler(WebClient.Builder builder, SecurityRepository securityRepository,
                                    DailyRepository dailyRepository) {
        this.webClient = builder.baseUrl("https://eodhistoricaldata.com/api/").
                codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024)).build();
        this.securityRepository = securityRepository;
        this.dailyRepository = dailyRepository;
    }

    public void processSymbolsFromExchange(String exchangeName) {
        Map<String, Security> symbolsFromDbMap = new HashMap<>();
        MapUtils.populateMap(symbolsFromDbMap, securityRepository.findAll(), Security::getTicker);
        Mono<List<Security>> result = webClient.get().uri(uriBuilder -> uriBuilder.path("exchange-symbol-list/" + exchangeName).
                queryParam("api_token", API_KEY).
                queryParam("fmt", "json").
                build()).retrieve().bodyToMono(new ParameterizedTypeReference<List<Security>>() {
        });

        List<Security> symbols = result.block().parallelStream().filter(security ->
                !EXCL_EXCHANGES.contains(security.getExchange()) && INCLUDED_TYPES.contains(security.getType())
        ).collect(Collectors.toList());

        symbols.stream().map(security -> {
            Security securityFromDb = symbolsFromDbMap.get(security.getTicker());
            if (securityFromDb != null && securityFromDb.getUpdateDateTime().plus(7, ChronoUnit.DAYS).isAfter(Instant.now())) {
                logger.info("Not Updating " + security.getTicker());
                return securityFromDb;
            }
            if (!NO_SECTOR_CALLS.contains(security.getType())) {
                String[] sectorIndustry = getSectorIndustry(security.getTicker(), security.getExchange());
                security.setSector(sectorIndustry[0]);
                security.setIndustry(sectorIndustry[1]);
            } else {
                security.setIndustry("NI");
                security.setSector("NS");
            }

            security.setExchange(exchangeName);
            logger.info("Done with " + security.getTicker());
            securityRepository.save(security);
            return security;
        }).collect(Collectors.toList());
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

    public List<Daily> getData(String ticker, String exchange, LocalDate fromDate, LocalDate toDate) {
        List<Daily> dailyList = new ArrayList<>();
        LocalDate maxDate = dailyRepository.getMaxDailyDateForSecurity(ticker, exchange);
        if (maxDate != null) {
            maxDate.isAfter(toDate);
        }

        return dailyList;
    }

    public void startDailyDownload(String exchange) {
        List<Security> securityList = exchange == null ? securityRepository.findAll()
                : securityRepository.findSecuritiesByExchange(exchange);
        securityList.stream().map(security -> {

            Mono<List<Daily>> result = webClient.get().uri(uriBuilder -> uriBuilder.path("eod/" + security.getTicker() + "." + security.getExchange()).
                    queryParam("api_token", API_KEY).
                    queryParam("fmt", "json").
                    queryParam("from", START_DATE)
                    .build()).retrieve().bodyToMono(new ParameterizedTypeReference<List<Daily>>() {
            });

            List<Daily> dailyList = result.block();
            dailyList = dailyList.stream().map(daily -> {
                daily.setTicker(security.getTicker());
                daily.setExchange(security.getExchange());

                return daily;
            }).collect(Collectors.toList());

            dailyRepository.saveAll(dailyList);

            logger.info("Downloaded data for " + security.getTicker() + "." + security.getExchange());
            return security;
        }).collect(Collectors.toList());
    }
}

@Data
class SectorIndustry {
    @JsonProperty("sector")
    private String sector;
    @JsonProperty("industry")
    private String industry;
}
