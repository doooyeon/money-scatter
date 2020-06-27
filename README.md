# money-scatter

### 머니 뿌리기
- 사용자는 다수의 친구들이 있는 대화방에서 뿌릴 금액과 받아갈 대상의 숫자를
입력하여 뿌리기 요청을 보낼 수 있다.
- 요청 시 자신의 잔액이 감소되고 대화방에는 뿌리기 메세지가 발송된다.
- 대화방에 있는 다른 사용자들은 위에 발송된 메세지를 클릭하여 금액을 무작위로
받아가게 된다.

---

## Project Build & Run
- **Pre-Requirements**
    - [java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
    - [git](https://git-scm.com/downloads)
    - [gradle](https://gradle.org/install/)

- **gradle build & jar run**
    1. project clone
    ```
    ~$ git clone https://github.com/doooyeon/money-scatter.git 
    ```
    2. 위치 이동
    ```
    ~$ cd money-scatter
    ```
    3. build
    ```
    ~/money-scatter$ gradle build
    ```
    4. BUILD SUCCESSFUL 후 jar 파일 실행
    ```
  ~/money-scatter$ java -jar -Dspring.profiles.active=prod build/libs/money-scatter-1.0.0.jar
    ```
    5. health check
    ```
    GET http://localhost:8080/health
    ```

---

## REST API
- **돈 뿌리기**
    - Request
        ```
        POST /money
        ```
        - header
            - `X-USER-ID`: 사용자 ID
            - `X-ROOM-ID`: 방 ID
        - body
            - `money`: 뿌릴 금액
            - `divisionCount`: 받아갈 인원
    - Response
        - `201 CREATED` : 뿌리기 성공
            - body
                - `token`: 생성된 토큰
- **돈 받기**
    - Request
        ```
        GET /money?token={token}
        ```
        - header
            - `X-USER-ID`: 사용자 ID
            - `X-ROOM-ID`: 방 ID
        - query
            - `token`: 발급된 토큰
    - Response
        - `200 OK`: 돈 받기 성공
            - body
                - `money`: 받은 금액
        - `404 NOT_FOUND`: 존재하지 않는 토큰
        - `401 UNAUTHORIZED`: 토큰 사용 유효시간 만료
        - `401 UNAUTHORIZED`: 돈 뿌린 사람의 요청
        - `401 UNAUTHORIZED`: 잘못된 방 id
- **돈 뿌리기 내역 조회**
    - Request
        ```
        GET /money/history?token={token}
        ```
        - header
            - `X-USER-ID`: 사용자 ID
            - `X-ROOM-ID`: 방 ID
        - query
            - `token`: 발급된 토큰
    - Response
        - `200 OK` : 돈 뿌리기 내역 조회 성공
            - body
                - `scatteredAt`: 뿌린 시간
                - `scatteredMoney`: 뿌린 금액
                - `assignedMoney`: 할당된 금액
                - `assignedHistory`: 할당 내역
                    - `assignor`: 할당 받은 사용자 id
                    - `money`: 할당 받은 금액
        - `404 NOT_FOUND`: 존재하지 않는 토큰
        - `401 UNAUTHORIZED`: 토큰 읽기 유효시간 만료
        - `401 UNAUTHORIZED`: 돈 뿌린 사람 외 요청
        - `401 UNAUTHORIZED`: 잘못된 방 id

---

## Troubleshooting Strategies
- **돈 뿌리기**
    - 토큰 생성
        - 생성 전략
            - 랜덤 알파벳 값 생성하여 `token` 저장 
        - 유효 기간
            - 사용 유효 기간
                - 토큰 생성 시간 + 10분 설정
            - 읽기 유효 기간
                - 토큰 생성 시간 + 7일 설정
    - 뿌린 돈 분배
        - 분배 전략
            - 인원 수에 맞게 동일하게 분배하여 인원 수 만큼 `scatteredMoney` 저장
- **돈 받기**
    - 뿌린 돈 할당
        - 할당 받지 않은 `scatteredMoney` 조회 후 첫 번째 돈 할당
        - 할당 시간과 할당자 id 지정하여 `assignedMoney` 저장
- **돈 뿌리기 내역 조회**
    - 토큰으로 `scatteredMoney`, `assignedMoney` 조회

---

## Schema
```sql
CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `value` varchar(10) COLLATE utf8mb4_bin NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL,

  `created_at` datetime(6) NOT NULL,
  `using_expired_at` datetime(6) NOT NULL,
  `reading_expired_at` datetime(6) NOT NULL,

  PRIMARY KEY (`id`),
  KEY `token_idx` (`value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `scattered_money` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `value` bigint(20) NOT NULL,
  `token_id` bigint(20) NOT NULL,

  `assigned` bit(1) DEFAULT FALSE NOT NULL,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`token_id`) REFERENCES token (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `assigned_money` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `scattered_money_id` bigint(20) NOT NULL,
  `token_id` bigint(20) NOT NULL,

  `assignor` bigint(20) DEFAULT NULL,
  `assigned_at` datetime(6) DEFAULT NULL,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`scattered_money_id`) REFERENCES scattered_money (`id`),
  FOREIGN KEY (`token_id`) REFERENCES token (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

---

## TO-BE
- Redis cache 사용
- 토큰 중복 처리
- JPA Entity mapping, fetch type, ...

---

## Development Environment
- Java11
- Spring Boot 2.3.1
- Spring Data JPA
- JUnit4
- MySQL8
