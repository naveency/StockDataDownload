package com.ncytech.stockdatadownload;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EodHistoricalData {
    private final WebClient webClient;
    public EodHistoricalData(WebClient.Builder webClientBulder) {
        this.webClient = webClientBulder.baseUrl("https://eodhistoricaldata.com/api/").build();
    }


}
