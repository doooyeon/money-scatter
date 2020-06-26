package com.pay.money.scatter.interfaces.request;

public class MoneyRequest {
    private Long money;

    private Long divisionCount;

    public MoneyRequest() {
    }

    public MoneyRequest(final Long money, final Long divisionCount) {
        this.money = money;
        this.divisionCount = divisionCount;
    }

    public Long getMoney() {
        return money;
    }

    public Long getDivisionCount() {
        return divisionCount;
    }
}
