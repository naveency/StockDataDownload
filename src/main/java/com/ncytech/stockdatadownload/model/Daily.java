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

    @CreationTimestamp
    private LocalDateTime createDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}
