package com.ncytech.stockdatadownload.repository;

import com.ncytech.stockdatadownload.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface SecurityRepository extends JpaRepository<Security, Long> {
    Security findSecurityByTickerAAndExchange(String ticker, String exchange);
}
