package com.ncytech.stockdatadownload;

import com.ncytech.stockdatadownload.handlers.EodHistoricalDataHandler;
import com.ncytech.stockdatadownload.model.Security;
import com.ncytech.stockdatadownload.repository.SecurityRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
@AllArgsConstructor
@EnableJpaAuditing
public class StockDataDownloadApplication implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(StockDataDownloadApplication.class.getName());
    private EodHistoricalDataHandler eodHistoricalDataHandler;
    private SecurityRepository securityRepository;

    public static void main(String[] args) {
        SpringApplication.run(StockDataDownloadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting Stock Download");
        eodHistoricalDataHandler.processSymbolsFromExchange("US");
        eodHistoricalDataHandler.processSymbolsFromExchange("COMM");
        eodHistoricalDataHandler.startDailyDownload("US");
        eodHistoricalDataHandler.startDailyDownload("COMM");

        logger.info("Done with stock download");
    }
}
