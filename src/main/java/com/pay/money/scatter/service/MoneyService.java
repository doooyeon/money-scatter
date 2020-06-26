package com.pay.money.scatter.service;

import com.pay.money.scatter.domain.model.AssignedMoney;
import com.pay.money.scatter.domain.model.ScatteredMoney;
import com.pay.money.scatter.domain.repository.AssignedMoneyRepository;
import com.pay.money.scatter.domain.repository.ScatteredMoneyRepository;
import com.pay.money.scatter.domain.model.Token;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class MoneyService {

    private final ScatteredMoneyRepository scatteredMoneyRepository;

    private final AssignedMoneyRepository assignedMoneyRepository;

    public MoneyService(final ScatteredMoneyRepository scatteredMoneyRepository, final AssignedMoneyRepository assignedMoneyRepository) {
        this.scatteredMoneyRepository = scatteredMoneyRepository;
        this.assignedMoneyRepository = assignedMoneyRepository;
    }

    @Transactional
    public void scatter(final Token token, final Long money, final Long numOfPeople) {
        // TODO 분배 로직 DI
        final Long divided = money / numOfPeople;
        final List<ScatteredMoney> scatteredMoneys = new ArrayList<>();
        for (int i = 0; i < numOfPeople; i++) {
            scatteredMoneys.add(ScatteredMoney.of(divided, token));
        }
        scatteredMoneyRepository.saveAll(scatteredMoneys);
    }

    @Transactional
    public AssignedMoney assign(final Token token, final Long userId) {
        if (assignedMoneyRepository.existsByTokenAndAssignor(token, userId)) {
            throw new IllegalArgumentException("이미 받음");
        }
        final List<ScatteredMoney> unAssignedMoneys = scatteredMoneyRepository.findAllByTokenAndAssignedIsFalse(token);
        if (unAssignedMoneys.isEmpty()) {
            throw new IllegalArgumentException("다 받음");
        }

        final ScatteredMoney scatteredMoney = unAssignedMoneys.get(0);
        final AssignedMoney assignedMoney = AssignedMoney.of(scatteredMoney, token, userId);
        scatteredMoney.assign();
        return assignedMoneyRepository.save(assignedMoney);
    }
}
