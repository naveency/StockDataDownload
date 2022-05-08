package com.ncytech.stockdatadownload.repository;

import com.ncytech.stockdatadownload.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityRepository extends JpaRepository<Security, Long> {
    Security findSecurityByTicker(String ticker);
}
