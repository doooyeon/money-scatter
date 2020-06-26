package com.pay.money.scatter.service.token;

import com.pay.money.scatter.domain.model.Token;
import com.pay.money.scatter.domain.repository.TokenRepository;
import com.pay.money.scatter.exception.UnAuthorizedException;
import com.pay.money.scatter.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final int TOKEN_LENGTH = 3;

    private final TokenRepository tokenRepository;

    private final TokenCreateStrategy tokenCreateStrategy;

    public TokenService(final TokenRepository tokenRepository, final RandomTokenCreateStrategy randomTokenCreateStrategy) {
        this.tokenRepository = tokenRepository;
        this.tokenCreateStrategy = randomTokenCreateStrategy;
    }

    public Token createToken(final Long userId, final Long roomId) {
        return tokenRepository.save(Token.of(tokenCreateStrategy.create(TOKEN_LENGTH).toString(), userId, roomId));
    }

    public Token getUsingToken(final String value, final Long userId, final Long roomId) {
        final Token token = getToken(value);
        if (token.usingExpired()) throw new UnAuthorizedException("유효기간이 만료된 토큰입니다.");
        if (token.isInvalidRoom(roomId)) throw new UnAuthorizedException("방에 속한 사람만 받을 수 있습니다.");
        if (token.isOwner(userId)) throw new UnAuthorizedException("뿌린 사람은 받을 수 없습니다.");
        return token;
    }

    public Token getReadingToken(final String value, final Long userId, final Long roomId) {
        final Token token = getToken(value);
        if (token.readingExpired()) throw new UnAuthorizedException("유효기간이 만료된 토큰입니다.");
        if (token.isInvalidRoom(roomId)) throw new UnAuthorizedException("방 정보가 일치하지 않습니다.");
        if (!token.isOwner(userId)) throw new UnAuthorizedException("뿌린 사람만 조회 가능합니다.");
        return token;
    }

    private Token getToken(final String value) {
        return tokenRepository.findByValue(value).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 토큰입니다."));
    }
}
