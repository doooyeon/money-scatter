package com.pay.money.scatter.service.money;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MoneyDivisionStrategy {
    List<Money> divide(final Money Money, final Long count);
}
