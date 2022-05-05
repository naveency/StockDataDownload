package com.ncytech.stockdatadownload;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class StockDataDownloadApplication implements CommandLineRunner {

    private static Logger logger = Logger.getLogger(StockDataDownloadApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(StockDataDownloadApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting Stock Download");
    }
}
