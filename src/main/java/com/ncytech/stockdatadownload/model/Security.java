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

@Entity
@Getter
@Setter
@IdClass(SecurityId.class)
@EntityListeners(AuditingEntityListener.class)
public class Security implements Serializable {
    @Id
    @JsonProperty("Code")
    private String ticker;
    @Id
    @JsonProperty("Exchange")
    private String exchange;

    @JsonProperty("Name")
    private String name;
    @JsonProperty("sector")
    private String sector;
    @JsonProperty("industry")
    private String industry;
    @JsonProperty("Type")
    private String type;

    @CreatedDate
    @Column(updatable = false)
    private Instant createDateTime;
    @LastModifiedDate
    private Instant updateDateTime;
}
