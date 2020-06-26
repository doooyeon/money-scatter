package com.pay.money.scatter.domain.repository;

import com.pay.money.scatter.domain.model.ScatteredMoney;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScatteredMoneyRepository extends JpaRepository<ScatteredMoney, Long> {
}
