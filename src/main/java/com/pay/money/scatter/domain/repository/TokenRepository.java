package com.pay.money.scatter.domain.repository;

import com.pay.money.scatter.domain.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
