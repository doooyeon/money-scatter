package com.pay.money.scatter.interfaces;

import com.pay.money.scatter.interfaces.request.MoneyRequest;
import com.pay.money.scatter.interfaces.response.MoneyView;
import com.pay.money.scatter.interfaces.response.RestExceptionView;
import com.pay.money.scatter.interfaces.response.ScatteredMoneyHistoryView;
import com.pay.money.scatter.interfaces.response.TokenView;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MoneyControllerTest {

    private final TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders;

    private static final String BASE_URL = "/money";

    public MoneyControllerTest(final TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.httpHeaders = new HttpHeaders();
    }

    @BeforeEach
    public void setUp() {
        this.httpHeaders.set("X-USER-ID", "1");
        this.httpHeaders.set("X-ROOM-ID", "123");
    }

    @DisplayName("돈 뿌리기 - 성공, 토큰 반환")
    @Test
    public void scatterMoneySuccess() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> response =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getToken().length()).isEqualTo(3);
    }

    @DisplayName("돈 받기 - 성공, N등분")
    @Test
    public void getMoneySuccess() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        this.httpHeaders.set("X-USER-ID", "2");
        final ResponseEntity<MoneyView> response = restTemplate.exchange(
                BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), MoneyView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMoney()).isEqualTo(2000L);
    }

    @DisplayName("돈 받기 - 실패, 존재하지 않는 토큰")
    @Test
    public void getMoneyFail_token() {
        ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, "AAA");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("존재하지 않는 토큰입니다.");
    }

    @DisplayName("돈 받기 - 실패, 방에 속한 사람만 가능")
    @Test
    public void getMoneyFail_room() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        this.httpHeaders.set("X-ROOM-ID", "124");
        final ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("방에 속한 사람만 받을 수 있습니다.");
    }

    @DisplayName("돈 받기 - 실패, 뿌린 사람은 받을 수 없음")
    @Test
    public void getMoneyFail_owner() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        final ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("뿌린 사람은 받을 수 없습니다.");
    }

    @DisplayName("돈 받기 - 실패, 두 번 받기")
    @Test
    public void getMoneyFail_conflict() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        this.httpHeaders.set("X-USER-ID", "2");
        restTemplate.exchange(BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);
        final ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getMessage()).isEqualTo("이미 받은 뿌리기 건입니다.");
    }

    @DisplayName("돈 받기 - 실패, 분배 완료")
    @Test
    public void getMoneyFail_complete() {
        final MoneyRequest request = new MoneyRequest(10000L, 1L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        this.httpHeaders.set("X-USER-ID", "2");
        restTemplate.exchange(BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);
        this.httpHeaders.set("X-USER-ID", "3");
        final ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("해당 뿌리기 건은 분배가 완료되었습니다.");
    }

    @DisplayName("조회 - 성공")
    @Test
    public void getHistorySuccess() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        final ResponseEntity<ScatteredMoneyHistoryView> response = restTemplate.exchange(
                BASE_URL + "/history?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), ScatteredMoneyHistoryView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getScatteredMoney()).isEqualTo(10000L);
        assertThat(response.getBody().getAssignedMoney()).isEqualTo(0L);
        assertThat(response.getBody().getAssignedHistory().isEmpty()).isTrue();
    }

    @DisplayName("조회 - 실패, 방 정보가 일치하지 않음")
    @Test
    public void getHistoryFail_room() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        this.httpHeaders.set("X-ROOM-ID", "124");
        final ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "/history?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("방 정보가 일치하지 않습니다.");
    }

    @DisplayName("조회 - 실패, 뿌린 사람만 조회 가능")
    @Test
    public void getHistoryFail_owner() {
        final MoneyRequest request = new MoneyRequest(10000L, 5L);
        final ResponseEntity<TokenView> tokenResponse =
                restTemplate.postForEntity(BASE_URL, new HttpEntity<>(request, httpHeaders), TokenView.class);
        final String token = tokenResponse.getBody().getToken();

        this.httpHeaders.set("X-USER-ID", "2");
        final ResponseEntity<RestExceptionView> response = restTemplate.exchange(
                BASE_URL + "/history?token={token}", HttpMethod.GET, new HttpEntity<>(httpHeaders), RestExceptionView.class, token);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getMessage()).isEqualTo("뿌린 사람만 조회 가능합니다.");
    }
}
