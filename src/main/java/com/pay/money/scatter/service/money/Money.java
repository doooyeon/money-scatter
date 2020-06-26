package com.pay.money.scatter.service.money;

public class Money {
    private final Long value;

    public static Money of(final Long value) {
        return new Money(value);
    }

    private Money(final Long value) {
        this.value = value;
    }

    public Long toLong() {
        return value;
    }
}
