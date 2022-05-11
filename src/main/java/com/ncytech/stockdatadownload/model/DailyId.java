package com.ncytech.stockdatadownload.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DailyId implements Serializable {
    private LocalDate date;
    private String ticker;
    private String exchange;
}

