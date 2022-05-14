package com.ncytech.stockdatadownload.handlers;

import com.ncytech.stockdatadownload.model.Daily;
import com.ncytech.stockdatadownload.model.Security;
import com.ncytech.stockdatadownload.repository.DailyRepository;
import com.ncytech.stockdatadownload.repository.SecurityRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Ta4jHandler {

    private DailyRepository dailyRepository;
    private SecurityRepository securityRepository;

    public Ta4jHandler(DailyRepository dailyRepository, SecurityRepository securityRepository) {
        this.dailyRepository = dailyRepository;
        this.securityRepository = securityRepository;
    }

    public void calculateIndicators() {
        List<Security> securityList = securityRepository.findAll();
        securityList.parallelStream().map(security -> {
            List<Daily> dailyList = dailyRepository.findDailiesByTickerAndExchange(security.getTicker(),
                    security.getExchange());

            return security;
        }).collect(Collectors.toList());
    }
}
