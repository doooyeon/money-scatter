package com.pay.money.scatter.interfaces.response;

import com.pay.money.scatter.domain.model.AssignedMoney;

public class MoneyView {
    private final Long value;

    public static MoneyView of(final AssignedMoney money) {
        return new MoneyView(money.toLong());
    }

    private MoneyView(final Long value) {
        this.value = value;
    }

    public Long getMoney() {
        return value;
    }
}
