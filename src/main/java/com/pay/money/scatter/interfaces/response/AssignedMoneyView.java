package com.pay.money.scatter.interfaces.response;

import com.pay.money.scatter.domain.model.AssignedMoney;

public class AssignedMoneyView {
    private final Long value;

    private final Long assignor;

    public static AssignedMoneyView of(final AssignedMoney assignedMoney) {
        return new AssignedMoneyView(assignedMoney.toLong(), assignedMoney.getAssignor());
    }

    private AssignedMoneyView(final Long value, final Long assignor) {
        this.value = value;
        this.assignor = assignor;
    }

    public Long getMoney() {
        return value;
    }

    public Long getAssignor() {
        return assignor;
    }
}
