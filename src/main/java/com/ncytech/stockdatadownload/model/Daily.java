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
public class Daily implements Serializable {
    @Id
    private LocalDate date;
    @Id
    @ManyToOne
    private Security security;

    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;
    private float adjustedClose;

    @CreatedDate
    @Column(updatable = false)
    private Instant createDateTime;
    @LastModifiedDate
    private Instant updateDateTime;
}
