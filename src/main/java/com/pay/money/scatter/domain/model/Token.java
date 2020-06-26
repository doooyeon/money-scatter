package com.pay.money.scatter.domain.model;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String value;

    @Column
    private Long userId;

    @Column
    private Long roomId;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime usingExpiredAt;

    @Column
    private LocalDateTime readingExpiredAt;

    public static Token of(final Long userId, final Long roomId) {
        final String value =  RandomStringUtils.randomAlphabetic(3);
        return new Token(value, userId, roomId);
    }

    private Token() {
    }

    private Token(final String value, final Long userId, final Long roomId) {
        this.value = value;
        this.userId = userId;
        this.roomId = roomId;
        this.createdAt = LocalDateTime.now();
        this.usingExpiredAt = this.createdAt.plusMinutes(10L);
        this.readingExpiredAt = this.createdAt.plusDays(7L);
    }

    public String toString() {
        return value;
    }
}
