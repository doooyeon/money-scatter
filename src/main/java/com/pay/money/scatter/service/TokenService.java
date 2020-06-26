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

    public Token getUsingToken(final String value, final Long userId, final Long roomId) {
        final Token token = getToken(value, roomId);
        if (token.usingExpired()) throw new IllegalArgumentException("토큰 사용 유효기간 만료");
        if (token.isInvalidRoom(roomId)) throw new IllegalArgumentException("방에 속한 사람만 받을 수 있음");
        if (token.isOwner(userId)) throw new IllegalArgumentException("뿌린 사람은 받을 수 없음");
        return token;
    }

    public Token getReadingToken(final String value, final Long userId, final Long roomId) {
        final Token token = getToken(value, roomId);
        if (token.readingExpired()) throw new IllegalArgumentException("토큰 읽기 유효기간 만료");
        if (token.isInvalidRoom(roomId)) throw new IllegalArgumentException("방 정보가 일치하지 않음");
        if (!token.isOwner(userId)) throw new IllegalArgumentException("뿌린 사람만 조회 가능");
        return token;
    }

    private Token getToken(final String value, final Long roomId) {
        return tokenRepository.findByValueAndRoomId(value, roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토큰"));
    }
}
