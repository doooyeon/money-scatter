package com.pay.money.scatter.service.token;

public class Token {
    private final String value;

    public static Token of(final String value) {
        return new Token(value);
    }

    private Token(final String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
