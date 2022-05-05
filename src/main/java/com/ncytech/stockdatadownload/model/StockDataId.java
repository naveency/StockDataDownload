package com.ncytech.stockdatadownload.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter
public class StockDataId implements Serializable {
    private LocalDate date;
    private Security security;
}

