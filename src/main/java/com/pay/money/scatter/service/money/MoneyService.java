package com.pay.money.scatter.service.money;

import com.pay.money.scatter.domain.model.AssignedMoney;
import com.pay.money.scatter.domain.model.ScatteredMoney;
import com.pay.money.scatter.domain.repository.AssignedMoneyRepository;
import com.pay.money.scatter.domain.repository.ScatteredMoneyRepository;
import com.pay.money.scatter.domain.model.Token;
import com.pay.money.scatter.exception.UnAuthorizedException;
import com.pay.money.scatter.exception.EntityDuplicationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoneyService {

    private final ScatteredMoneyRepository scatteredMoneyRepository;

    private final AssignedMoneyRepository assignedMoneyRepository;

    private final MoneyDivisionStrategy moneyDivisionStrategy;

    public MoneyService(final ScatteredMoneyRepository scatteredMoneyRepository,
                        final AssignedMoneyRepository assignedMoneyRepository,
                        final EqualDivisionStrategy equalDivisionStrategy) {
        this.scatteredMoneyRepository = scatteredMoneyRepository;
        this.assignedMoneyRepository = assignedMoneyRepository;
        this.moneyDivisionStrategy = equalDivisionStrategy;
    }

    @Transactional
    public void scatter(final Token token, final Long value, final Long divisionCount) {
        final List<Money> divided = moneyDivisionStrategy.divide(Money.of(value), divisionCount);
        scatteredMoneyRepository.saveAll(
                divided.stream().map(money -> ScatteredMoney.of(money.toLong(), token)).collect(Collectors.toList()));
    }

    @Transactional
    public AssignedMoney assign(final Token token, final Long userId) {
        if (assignedMoneyRepository.existsByTokenAndAssignor(token, userId)) throw new EntityDuplicationException("이미 받은 뿌리기 건입니다.");

        final List<ScatteredMoney> unAssignedMoneys = scatteredMoneyRepository.findAllByTokenAndAssignedIsFalse(token);
        if (unAssignedMoneys.isEmpty()) throw new UnAuthorizedException("해당 뿌리기 건은 분배가 완료되었습니다.");

        final ScatteredMoney scatteredMoney = unAssignedMoneys.get(0);
        final AssignedMoney assignedMoney = AssignedMoney.of(scatteredMoney, token, userId);
        scatteredMoney.assign();
        return assignedMoneyRepository.save(assignedMoney);
    }

    @Transactional
    public List<ScatteredMoney> getScatteredMoneys(final Token token) {
        return scatteredMoneyRepository.findAllByToken(token);
    }

    @Transactional
    public List<AssignedMoney> getAssignedMoneys(final Token token) {
        return assignedMoneyRepository.findAllByToken(token);
    }
}
