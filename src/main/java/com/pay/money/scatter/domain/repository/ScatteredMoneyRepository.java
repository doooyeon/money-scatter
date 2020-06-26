package com.pay.money.scatter.domain.repository;

import com.pay.money.scatter.domain.model.ScatteredMoney;
import com.pay.money.scatter.domain.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScatteredMoneyRepository extends JpaRepository<ScatteredMoney, Long> {

    List<ScatteredMoney> findAllByTokenAndAssignedIsFalse(final Token token);
}
