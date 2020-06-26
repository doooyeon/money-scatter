package com.pay.money.scatter.domain.model;

import javax.persistence.*;

@Entity
public class ScatteredMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long value;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private Token token;

    @Column
    private Boolean assigned;

    public static ScatteredMoney of(final Long value, final Token token) {
        return new ScatteredMoney(value, token);
    }

    private ScatteredMoney() {
    }

    private ScatteredMoney(final Long value, final Token token) {
        this.value = value;
        this.token = token;
        this.assigned = false;
    }
}
