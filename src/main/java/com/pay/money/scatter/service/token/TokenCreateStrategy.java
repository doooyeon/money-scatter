package com.pay.money.scatter.service.token;

import org.springframework.stereotype.Component;

@Component
public interface TokenCreateStrategy {
    Token create(final int length);
}
