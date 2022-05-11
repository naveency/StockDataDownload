package com.ncytech.stockdatadownload.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@IdClass(DailyId.class)
@EntityListeners(AuditingEntityListener.class)
@Table(indexes = {
        @Index(columnList = "ticker, exchange")
})
public class Daily implements Serializable {
    @Id
    @JsonProperty("date")
    private LocalDate date;
    @Id
    private String ticker;
    @Id
    private String exchange;

    @JsonProperty("open")
    private float open;
    @JsonProperty("high")
    private float high;
    @JsonProperty("low")
    private float low;
    @JsonProperty("close")
    private float close;
    @JsonProperty("volume")
    private float volume;
    @JsonProperty("adjusted_close")
    private float adjustedClose;

    @CreatedDate
    @Column(updatable = false)
    private Instant createDateTime;
    @LastModifiedDate
    private Instant updateDateTime;
}
