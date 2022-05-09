package com.ncytech.stockdatadownload.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class StockDataId implements Serializable {
    private LocalDate date;
    private Security security;
}

