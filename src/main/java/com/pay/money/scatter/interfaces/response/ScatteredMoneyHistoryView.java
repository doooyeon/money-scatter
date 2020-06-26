package com.pay.money.scatter.interfaces.response;

import com.pay.money.scatter.domain.model.AssignedMoney;
import com.pay.money.scatter.domain.model.ScatteredMoney;
import com.pay.money.scatter.domain.model.Token;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ScatteredMoneyHistoryView {
    private final LocalDateTime scatteredAt;

    private final Long scatteredMoney;

    private final Long assignedMoney;

    private final List<AssignedMoneyView> assignedHistory;

    public static ScatteredMoneyHistoryView of(final Token token, final List<ScatteredMoney> scattered, final List<AssignedMoney> assigned) {
        final Long scatteredMoney = scattered.stream().mapToLong(ScatteredMoney::toLong).sum();
        final Long assignedMoney = assigned.stream().mapToLong(AssignedMoney::toLong).sum();
        final List<AssignedMoneyView> assignedMoneyHistory = assigned.stream().map(AssignedMoneyView::of).collect(Collectors.toList());
        return new ScatteredMoneyHistoryView(token.getCreatedAt(), scatteredMoney, assignedMoney, assignedMoneyHistory);
    }

    private ScatteredMoneyHistoryView(final LocalDateTime scatteredAt,
                                      final Long scatteredMoney,
                                      final Long assignedMoney,
                                      final List<AssignedMoneyView> assignedHistory) {
        this.scatteredAt = scatteredAt;
        this.scatteredMoney = scatteredMoney;
        this.assignedMoney = assignedMoney;
        this.assignedHistory = assignedHistory;
    }

    public LocalDateTime getScatteredAt() {
        return scatteredAt;
    }

    public Long getScatteredMoney() {
        return scatteredMoney;
    }

    public Long getAssignedMoney() {
        return assignedMoney;
    }

    public List<AssignedMoneyView> getAssignedHistory() {
        return assignedHistory;
    }
}
