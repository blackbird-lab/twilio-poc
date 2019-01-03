package com.blackbird.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;


@Data
public class MessageDto {
    private String sid;
    private String body;
    private String to;
    private String from;
    @JsonProperty(value = "code")
    private Integer errorCode;
    @JsonProperty(value = "message")
    private String errorMessage;
    private BigDecimal price;
    private Currency priceUnit;
    private String status;
    private String uri;
}
