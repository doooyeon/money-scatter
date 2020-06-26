package com.pay.money.scatter.service.money;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EqualDivisionStrategy implements MoneyDivisionStrategy {

    @Override
    public List<Money> divide(final Money money, final Long count) {
        final List<Money> dividedMoneys = new ArrayList<>();
        final Long quotient = money.toLong() / count;
        final Long remainder = money.toLong() % count;
        for (int i = 0; i < count; i++) {
            final Money dividedMoney = remainder > i ? Money.of(quotient + 1) : Money.of(quotient);
            dividedMoneys.add(dividedMoney);
        }
        return dividedMoneys;
    }
}
