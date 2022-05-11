package com.ncytech.stockdatadownload.repository;

import com.ncytech.stockdatadownload.model.Daily;
import com.ncytech.stockdatadownload.model.DailyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface DailyRepository extends JpaRepository<Daily, DailyId> {
    @Query(nativeQuery = true, value = "select max(date) from daily where security_ticker=? and security_exchange=?")
    LocalDate getMaxDailyDateForSecurity(String ticker, String exchange);
}
