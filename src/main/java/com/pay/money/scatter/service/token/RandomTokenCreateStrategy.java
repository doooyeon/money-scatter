package com.pay.money.scatter.service.token;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomTokenCreateStrategy implements TokenCreateStrategy {

    @Override
    public Token create(final int length) {
        return Token.of(RandomStringUtils.randomAlphabetic(length));
    }
}
