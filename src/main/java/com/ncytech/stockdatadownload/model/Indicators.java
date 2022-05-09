package com.ncytech.stockdatadownload.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@IdClass(StockDataId.class)
@Getter
@Setter
public class Indicators implements Serializable {

    @Id
    private LocalDate date;
    @Id
    @ManyToOne
    private Security security;

    private float volumeSma50;
    private float ema10;
    private float ema21;
    private float ema50;
    private float ema200;
    private float rsi14;
    private float macd;
    private float macdSignal;
    private float macdHistogram;
    private float swingHigh;
    private float swingLow;

    @CreatedDate
    @Column(updatable = false)
    private Instant createDateTime;
    @LastModifiedDate
    private Instant updateDateTime;
}
