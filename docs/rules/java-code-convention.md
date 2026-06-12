## Java Code Convention

### 네이밍 규칙
- 클래스/인터페이스: PascalCase (예: `UserService`, `OrderRepository`)
- 메서드/변수: camelCase (예: `findUserById`, `totalPrice`)
- 상수: UPPER_SNAKE_CASE (예: `MAX_RETRY_COUNT`)
- 패키지명: 모두 소문자, 점으로 구분 (예: `com.example.order.service`)
- Boolean 변수/메서드: `is`, `has`, `can` 접두사 사용 (예: `isValid`, `hasPermission`)

### 패키지 구조
```
com.example.project
 ├── controller    - API 엔드포인트
 ├── service       - 비즈니스 로직
 ├── repository    - 데이터 접근 계층
 ├── domain/entity - 엔티티 클래스
 ├── dto           - 요청/응답 DTO
 ├── config        - 설정 클래스
 ├── exception     - 커스텀 예외 및 핸들러
 └── util          - 공통 유틸리티
```

### 클래스 작성 규칙
- 클래스 길이는 300줄 이내 권장 (단일 책임 원칙)
- 필드 순서: static 필드 → final 필드 → 일반 필드
- 생성자는 필드 선언 직후에 배치
- Lombok 사용 시 `@Getter`, `@Builder`, `@RequiredArgsConstructor` 우선 사용
  - `@Data`는 엔티티 클래스에 지양 (equals/hashCode 이슈)
- 의존성 주입은 생성자 주입(`@RequiredArgsConstructor`) 사용, 필드 주입(`@Autowired`) 지양

### 메서드 작성 규칙
- 메서드 길이는 가능한 30줄 이내
- 메서드 하나는 한 가지 역할만 수행
- 파라미터는 4개 이하 권장, 초과 시 DTO/객체로 묶기
- 반환 타입은 가능한 인터페이스 사용 (`List`, `Map` 등 구현체 노출 지양)
- null 반환 대신 `Optional` 사용 (단, 필드/파라미터 타입으로는 사용하지 않음)

### 예외 처리
- 비즈니스 예외는 커스텀 예외 클래스로 정의 (예: `UserNotFoundException`)
- `RuntimeException`을 직접 던지지 말고 의미 있는 예외 클래스 사용
- 예외는 `@RestControllerAdvice`에서 전역 처리
- catch한 예외는 무시하지 말고 로깅 또는 재처리

### Spring 관련 규칙
- `@Transactional`은 서비스 계층에만 적용, 읽기 전용 메서드는 `@Transactional(readOnly = true)`
- Controller는 요청/응답 처리만 담당, 비즈니스 로직 포함 금지
- DTO와 Entity는 분리, Entity를 API 응답으로 직접 반환하지 않음
- 검증은 `@Valid` + Bean Validation 어노테이션(`@NotNull`, `@Size` 등) 사용

### 주석 및 문서화
- public 메서드/클래스는 Javadoc 작성 (역할, 파라미터, 반환값, 예외)
- 코드로 의도를 표현할 수 있다면 주석 최소화 (주석은 "왜"를 설명, "무엇"은 코드로)
- TODO/FIXME 주석은 이슈 번호와 함께 작성 (예: `// TODO: #123 - 캐싱 적용 필요`)

### 포맷팅
- 들여쓰기: 4 spaces (tab 금지)
- 한 줄 최대 길이: 120자
- 중괄호는 K&R 스타일 (여는 중괄호는 줄바꿈 없이)
- import는 와일드카드(`*`) 사용 금지, 미사용 import 제거
- 빈 줄로 논리적 블록 구분 (필드/생성자/메서드 그룹별)

### 테스트
- 테스트 클래스명: `{클래스명}Test` (예: `UserServiceTest`)
- 메서드명은 `given_when_then` 또는 한글 설명 패턴 사용 가능
  - 예: `findUserById_존재하지않으면_예외를던진다`
- 단위 테스트는 Mock 사용, 통합 테스트는 `@SpringBootTest`로 분리
- 테스트 코드도 프로덕션 코드와 동일한 컨벤션 적용

### 정적 분석
- Checkstyle / SonarLint 등 정적 분석 도구 통과 권장
- 컴파일 경고(unchecked, deprecation 등) 최소화
