package com.pay.money.scatter.service;

import com.pay.money.scatter.domain.model.ScatteredMoney;
import com.pay.money.scatter.domain.repository.ScatteredMoneyRepository;
import com.pay.money.scatter.domain.model.Token;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class MoneyService {

    private final ScatteredMoneyRepository scatteredMoneyRepository;

    public MoneyService(final ScatteredMoneyRepository scatteredMoneyRepository) {
        this.scatteredMoneyRepository = scatteredMoneyRepository;
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
}
