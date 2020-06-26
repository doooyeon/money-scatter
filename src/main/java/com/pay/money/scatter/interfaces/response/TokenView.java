package com.pay.money.scatter.interfaces.response;

import com.pay.money.scatter.domain.model.Token;

public class TokenView {
    private final String value;

    public static TokenView of(final Token token) {
        return new TokenView(token.toString());
    }

    private TokenView(final String value) {
        this.value = value;
    }

    public String getToken() {
        return value;
    }
}
