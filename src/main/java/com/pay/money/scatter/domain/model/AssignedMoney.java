package com.pay.money.scatter.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AssignedMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "scattered_money_id")
    private ScatteredMoney scatteredMoney;

    @ManyToOne
    @JoinColumn(name = "token_id")
    private Token token;

    @Column
    private Long assignor;

    @Column
    private LocalDateTime assignedAt;

    public static AssignedMoney of(final ScatteredMoney scatteredMoney, final Token token, final Long assignor) {
        return new AssignedMoney(scatteredMoney, token, assignor);
    }

    private AssignedMoney() {
    }

    private AssignedMoney(final ScatteredMoney scatteredMoney, final Token token, final Long assignor) {
        this.scatteredMoney = scatteredMoney;
        this.token = token;
        this.assignor = assignor;
        this.assignedAt = LocalDateTime.now();
    }
}
