package com.pay.money.scatter.service;

import com.pay.money.scatter.domain.model.Token;
import com.pay.money.scatter.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(final TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(final Long userId, final Long roomId) {
        return tokenRepository.save(Token.of(userId, roomId));
    }
}
