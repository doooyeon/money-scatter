package com.pay.money.scatter.interfaces;

import com.pay.money.scatter.domain.model.AssignedMoney;
import com.pay.money.scatter.domain.model.ScatteredMoney;
import com.pay.money.scatter.domain.model.Token;
import com.pay.money.scatter.interfaces.request.MoneyRequest;
import com.pay.money.scatter.interfaces.response.MoneyView;
import com.pay.money.scatter.interfaces.response.ScatteredMoneyHistoryView;
import com.pay.money.scatter.interfaces.response.TokenView;
import com.pay.money.scatter.service.MoneyService;
import com.pay.money.scatter.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/money")
public class MoneyController {

    private final TokenService tokenService;
    private final MoneyService moneyService;

    public MoneyController(final TokenService tokenService, final MoneyService moneyService) {
        this.tokenService = tokenService;
        this.moneyService = moneyService;
    }

    @PostMapping
    public ResponseEntity<TokenView> scatterMoney(@RequestHeader("X-USER-ID") final Long userId,
                                                  @RequestHeader("X-ROOM-ID") final Long roomId,
                                                  @RequestBody final MoneyRequest request) {
        final Token token = tokenService.createToken(userId, roomId);
        moneyService.scatter(token, request.getMoney(), request.getNumOfPeople());
        return ResponseEntity.ok(TokenView.of(token));
    }

    @GetMapping
    public ResponseEntity<MoneyView> assignMoney(@RequestHeader("X-USER-ID") final Long userId,
                                                 @RequestHeader("X-ROOM-ID") final Long roomId,
                                                 @RequestParam("token") final String value) {
        final Token token = tokenService.getUsingToken(value, userId, roomId);
        final AssignedMoney money = moneyService.assign(token, userId);
        return ResponseEntity.ok(MoneyView.of(money));
    }

    @GetMapping("/history")
    public ResponseEntity<ScatteredMoneyHistoryView> getScatteredMoneyHistory(@RequestHeader("X-USER-ID") final Long userId,
                                                                              @RequestHeader("X-ROOM-ID") final Long roomId,
                                                                              @RequestParam("token") final String value) {
        final Token token = tokenService.getReadingToken(value, userId, roomId);
        final List<ScatteredMoney> scattered = moneyService.getScatteredMoneys(token);
        final List<AssignedMoney> assigned = moneyService.getAssignedMoneys(token);
        return ResponseEntity.ok(ScatteredMoneyHistoryView.of(token, scattered, assigned));
    }
}
