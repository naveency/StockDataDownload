package com.ncytech.stockdatadownload.repository;

import com.ncytech.stockdatadownload.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SecurityRepository extends JpaRepository<Security, Long> {
    Security findSecurityByTickerAndExchange(String ticker, String exchange);
    List<Security> findSecuritiesByExchange(String exchange);
}
