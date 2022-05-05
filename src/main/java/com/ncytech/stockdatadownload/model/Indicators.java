package com.ncytech.stockdatadownload.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @CreationTimestamp
    private LocalDateTime createDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}
