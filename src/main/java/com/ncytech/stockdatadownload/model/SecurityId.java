package com.ncytech.stockdatadownload.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SecurityId implements Serializable {
    private String ticker;
    private String exchange;
}
