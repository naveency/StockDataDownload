package com.ncytech.stockdatadownload.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@IdClass(SecurityId.class)
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

    @CreationTimestamp
    private LocalDateTime createDateTime;
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
}
