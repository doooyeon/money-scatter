package com.pay.money.scatter.domain.repository;

import com.pay.money.scatter.domain.model.AssignedMoney;
import com.pay.money.scatter.domain.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignedMoneyRepository extends JpaRepository<AssignedMoney, Long> {

    boolean existsByTokenAndAssignor(final Token token, final Long assignor);
}
