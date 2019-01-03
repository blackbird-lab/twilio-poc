package com.blackbird.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;

@Data
@Entity
@EqualsAndHashCode(of = {"id", "sid", "errorCode"})
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sid;
    private String body;
    @Column(name = "from_phone")
    private String from;
    @Column(name = "to_phone")
    private String to;
    private Integer errorCode;
    private String errorMessage;
    private BigDecimal price;
    private Currency priceUnit;
    private String status;
    private String uri;
}
