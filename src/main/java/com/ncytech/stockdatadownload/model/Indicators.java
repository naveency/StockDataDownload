package com.ncytech.stockdatadownload.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@IdClass(DailyId.class)
@Getter
@Setter
public class Indicators implements Serializable {

    @Id
    private LocalDate date;
    @Id
    private String ticker;
    @Id
    private String exchange;

    @Column(nullable = true)
    private float volumeSma50;
    @Column(nullable = true)
    private float ema10;
    @Column(nullable = true)
    private float ema21;
    @Column(nullable = true)
    private float ema50;
    @Column(nullable = true)
    private float ema200;
    @Column(nullable = true)
    private float rsi14;
    @Column(nullable = true)
    private float macd;
    @Column(nullable = true)
    private float macdSignal;
    @Column(nullable = true)
    private float macdHistogram;
    @Column(nullable = true)
    private float swingHigh;
    @Column(nullable = true)
    private boolean isSwingHigh;
    @Column(nullable = true)
    private float swingLow;
    @Column(nullable = true)
    private boolean isSwingLow;
}
