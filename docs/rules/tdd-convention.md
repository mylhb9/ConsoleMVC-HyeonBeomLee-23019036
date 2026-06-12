# TDD 개발 컨벤션 (Java)

## 1. 기본 원칙

- Red → Green → Refactor 순서를 반드시 지킨다.
- 실패하는 테스트를 먼저 작성하고, 최소한의 코드로 통과시킨 뒤 리팩토링한다.
- 한 번에 하나의 동작(behavior)만 검증한다.

## 2. 테스트 클래스 작성 규칙

- 테스트 클래스명은 `{대상클래스명}Test` 형식을 따른다. (예: `UserServiceTest`)
- `@DisplayName`을 클래스와 메서드 모두에 작성하여 테스트 의도를 한글로 명확히 설명한다.

```java
@DisplayName("회원 서비스 테스트")
class UserServiceTest {

    @Test
    @DisplayName("이메일이 중복되면 회원가입 시 예외가 발생한다")
    void registerUser_duplicateEmail_throwsException() {
        // given

        // when

        // then
    }
}
```

## 3. 테스트 메서드 명명 규칙

- `{메서드명}_{조건/상황}_{기대결과}` 형식을 권장한다.
  - 예: `withdraw_balanceInsufficient_throwsIllegalStateException`
- `@DisplayName`은 "~하면 ~한다" 형식의 자연어 문장으로 작성한다.
  - 예: `"잔액이 부족하면 인출 시 예외가 발생한다"`

## 4. Given-When-Then 구조

- 모든 테스트 메서드 본문은 `// given`, `// when`, `// then` 주석으로 구분한다.
- 각 영역은 한 줄 이상의 공백으로 분리해 가독성을 높인다.

## 5. Assertion 규칙

- AssertJ를 기본 사용한다 (`assertThat(...)`).
- 예외 검증은 `assertThatThrownBy` 또는 `assertThatExceptionOfType`을 사용한다.
- 단정문은 한 테스트에 핵심 검증 1~3개 이내로 제한한다.

## 6. 테스트 그룹화

- 같은 메서드에 대한 여러 시나리오는 `@Nested` 클래스로 그룹화한다.
- `@Nested` 클래스에도 `@DisplayName`을 작성한다.

```java
@Nested
@DisplayName("인출 기능")
class Withdraw {

    @Test
    @DisplayName("잔액이 충분하면 정상적으로 인출된다")
    void withdraw_sufficientBalance_success() {
        // given
        // when
        // then
    }

    @Test
    @DisplayName("잔액이 부족하면 예외가 발생한다")
    void withdraw_insufficientBalance_throwsException() {
        // given
        // when
        // then
    }
}
```

## 7. 테스트 데이터

- 테스트 픽스처는 `given...` 형태의 private 헬퍼 메서드 또는 별도 Fixture 클래스로 분리한다.
- 매직 넘버 대신 의미 있는 상수명을 사용한다.

## 8. Mocking 규칙

- Mockito를 사용하며, `@Mock`, `@InjectMocks`를 활용한다.
- 외부 의존성(Repository, 외부 API 클라이언트)만 mocking하고, 도메인 로직은 mocking하지 않는다.

## 9. 커버리지 및 범위

- 단위 테스트: 도메인 로직, 서비스 로직 중심으로 작성한다.
- 통합 테스트: `@SpringBootTest` 또는 `@DataJpaTest`를 사용하며 클래스명에 `IT` 접미사를 붙인다. (예: `OrderServiceIT`)

## 10. 커밋 규칙 (TDD 사이클 연동)

- `test:` 실패하는 테스트 작성 (Red)
- `feat:` 테스트를 통과시키는 최소 구현 (Green)
- `refactor:` 동작 변경 없는 구조 개선 (Refactor)
