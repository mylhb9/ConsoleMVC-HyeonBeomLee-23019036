# PLAN: ConsoleMVC PoC

> 기반 PRD: `docs/PRD.md` (ConsoleMVC PoC v1.0.0)
> 작성일: 2026-06-12
> 총 개발 기간: 1주 (5일, 버퍼 20% 적용 시 실효 4일)
> 팀 규모: 1명

---

## Executive Summary

콘솔 기반 Java 애플리케이션에서 MVC 패턴의 구조적 타당성을 검증하는 PoC다.
시료(Sample) 도메인의 등록·조회 흐름을 통해 View → Controller → Service → Repository → Model
의존성 방향을 강제하고, 추후 DataPersistence PoC에서 Repository 구현체만 교체할 수 있는 구조를 확립한다.
**모든 구현은 `example-skills:cra-test-driven-development` 스킬의 TDD 사이클을 따른다.**

---

## 기술 스택

| 레이어 | 기술 | 선택 이유 |
|--------|------|---------|
| 언어 | Java 17 | LTS, 람다/스트림 활용 |
| 빌드 | Gradle | 의존성 관리 편의 |
| 아키텍처 | Layered MVC (순수 Java) | Spring 없이 구조 검증 |
| UI | Console (CLI) | PoC 범위 |
| 데이터 저장 | InMemory (LinkedHashMap) | DataPersistence PoC 전 검증용 |
| 테스트 프레임워크 | JUnit 5 | 표준 단위 테스트 |
| Assertion 라이브러리 | AssertJ | 가독성 높은 단정문, 예외 검증 |
| Mock 라이브러리 | Mockito | 외부 의존성 격리 (Repository, View) |
| 커버리지 | JaCoCo | 100% Line/Branch 커버리지 강제 |

---

## TDD 개발 프로세스

### 절대 법칙

```
실패하는 테스트 없이 프로덕션 코드를 작성하지 말 것
TDD Plan 승인 없이 테스트 코드를 작성하지 말 것
사람의 REVIEW 완료 전에 다음 사이클을 시작하지 말 것
```

### Red-Green-Review 사이클

모든 FEATURE 태스크는 아래 4단계를 반드시 순서대로 거친다.

```
[TDD Plan 작성]
      ↓
⚠️  사람 검토 / 승인     ← 승인 없이 테스트 작성 금지
      ↓
[RED]  실패하는 테스트 작성 → ./gradlew test 로 실패 직접 확인
      ↓
[GREEN]  통과시킬 최소 구현 → ./gradlew test 로 통과 직접 확인
      ↓
⚠️  [REVIEW]  코드 품질 검토 → 사람 승인   ← 승인 없이 다음 사이클 착수 금지
      ↓
   다음 사이클 TDD Plan 작성
```

### Human-in-the-loop 체크포인트

| 시점 | 요청 내용 | 가능한 응답 |
|------|---------|----------|
| **TDD Plan 작성 후** | "테스트 계획이 맞는지, 빠진 케이스가 있는지 검토해주세요. 승인하시면 테스트 코드를 작성하겠습니다." | 승인 / 수정 요청 |
| **GREEN 완료 후 (REVIEW)** | "GREEN 구현이 완료되었습니다. 코드를 검토해주시고, 다음 사이클 진행 여부를 알려주세요." | 다음 사이클 진행 / 추가 검토 / 완료 |

### TDD Plan 작성 양식

각 FEATURE 태스크 착수 전 아래 형식으로 TDD Plan을 작성한다.

```markdown
# TDD Plan — [기능명]

## 이번 사이클 목표
- 구현하려는 동작을 한 문장으로

## 테스트 대상
- 클래스: com.ssemi.consolemvc.[패키지].[클래스명]
- 메서드: [메서드 시그니처]

## 작성할 테스트
| 테스트명 | 검증 동작 | 예상 실패 이유 |
|---------|---------|-------------|
| [테스트명] | [검증 동작] | [구현 미완료] |

## 엣지 케이스
- [경계값, 예외 상황]

## 제외 범위
- [이번 사이클에서 의도적으로 다루지 않는 것]
```

### Gradle 테스트 실행 명령어

```bash
# 단일 테스트 메서드 실행 (RED/GREEN 검증 시)
./gradlew test --tests "com.ssemi.consolemvc.[클래스명].[테스트메서드명]"

# 단일 클래스 실행
./gradlew test --tests "com.ssemi.consolemvc.[클래스명]"

# 전체 테스트 실행 (GREEN 통과 후 회귀 확인)
./gradlew test

# 캐시 무시하고 재실행
./gradlew test --rerun-tasks

# JaCoCo 커버리지 리포트 생성 (100% 미달 시 빌드 실패)
./gradlew test jacocoTestReport jacocoTestCoverageVerification

# 테스트 리포트 확인
# build/reports/tests/test/index.html
# build/reports/jacoco/test/html/index.html
```

---

### Java TDD 컨벤션 규칙

> `docs/rules/tdd-convention.md` 준수. 이하 규칙은 모든 테스트 클래스에 강제 적용된다.

#### 테스트 클래스·메서드 명명

| 항목 | 규칙 | 예시 |
|------|------|------|
| 클래스명 | `{대상클래스명}Test` | `SampleServiceTest` |
| 메서드명 | `{메서드명}_{조건/상황}_{기대결과}` | `registerSample_duplicateId_throwsDuplicateSampleIdException` |
| `@DisplayName` (클래스) | 한글 의도 설명 | `"시료 서비스 테스트"` |
| `@DisplayName` (메서드) | `"~하면 ~한다"` 형식 한글 | `"중복 ID 등록 시 예외가 발생한다"` |

#### 테스트 구조 템플릿

```java
@DisplayName("시료 서비스 테스트")
class SampleServiceTest {

    @Mock
    private SampleRepository sampleRepository;          // 외부 의존성만 Mock

    @InjectMocks
    private SampleService sampleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("시료 등록")
    class RegisterSample {

        @Test
        @DisplayName("정상 요청 시 시료가 저장된다")
        void registerSample_validRequest_savesSample() {
            // given
            SampleCreateRequest request = givenSampleCreateRequest("S-001", "실리콘웨이퍼-8인치", 10.0, 0.9, 100);
            given(sampleRepository.existsById("S-001")).willReturn(false);

            // when
            SampleResponse response = sampleService.registerSample(request);

            // then
            assertThat(response.sampleId()).isEqualTo("S-001");
            then(sampleRepository).should().save(any(Sample.class));
        }
    }

    // 테스트 픽스처 — given... 헬퍼 메서드 분리
    private SampleCreateRequest givenSampleCreateRequest(
            String id, String name, double avgProdTime, double yield, int stock) {
        return new SampleCreateRequest(id, name, avgProdTime, yield, stock);
    }
}
```

#### 핵심 규칙 요약

| 규칙 | 내용 |
|------|------|
| **Given-When-Then** | 모든 테스트 본문에 `// given`, `// when`, `// then` 주석 필수 |
| **AssertJ** | `assertThat(...)` 기본 사용. 예외 검증은 `assertThatThrownBy` 또는 `assertThatExceptionOfType` |
| **단정문 수** | 핵심 검증 1~3개 이내 |
| **@Nested** | 같은 메서드 시나리오는 `@Nested` 클래스로 그룹화 |
| **Mockito** | `@Mock`, `@InjectMocks` 사용. Repository·View 등 외부 의존성만 Mock |
| **Fixture** | 테스트 데이터는 `given...` 헬퍼 메서드 또는 별도 Fixture 클래스로 분리 |
| **매직 넘버** | 의미 있는 상수명 사용 (`private static final int STOCK_100 = 100;`) |

#### JaCoCo 100% 커버리지 설정 (`build.gradle`)

```kotlin
plugins {
    java
    jacoco
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation("org.mockito:mockito-core:5.+")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports { xml.required = true }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "1.0".toBigDecimal()   // 100% 강제
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "1.0".toBigDecimal()
            }
        }
    }
    // View(입출력), DTO, Exception 선언 클래스는 커버리지 측정 제외
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(
                "**/view/**",
                "**/dto/**",
                "**/exception/**",
                "**/Application.class"
            )
        }
    )
}

tasks.check { dependsOn(tasks.jacocoTestCoverageVerification) }
```

---

## 아키텍처

### 패키지 구조

```
com.ssemi.consolemvc
├── model
│   └── Sample.java                     # sampleId(PK), name, avgProdTime, yield, stock
├── dto
│   ├── SampleCreateRequest.java        # 시료 등록 입력 DTO
│   └── SampleResponse.java             # 시료 응답 DTO (fromSample 정적 팩토리)
├── exception
│   ├── BusinessException.java          # 최상위 비즈니스 예외 (RuntimeException)
│   ├── DuplicateSampleIdException.java
│   └── SampleNotFoundException.java
├── repository
│   ├── SampleRepository.java           # 인터페이스 (저장 방식 추상화)
│   └── InMemorySampleRepository.java   # LinkedHashMap 기반 구현체
├── service
│   └── SampleService.java              # 도메인 로직 집중
├── controller
│   └── SampleController.java           # 흐름 제어 (입출력 로직 직접 포함 금지)
├── view
│   ├── MainView.java                   # 메인 메뉴 루프
│   └── SampleView.java                 # 시료 화면 입출력
└── Application.java                    # Composition Root (의존성 조립)
```

### 의존성 방향 (절대 준수)

```
View → Controller → Service → Repository → Model
                                              ↑
                              Model은 어떤 레이어도 의존하지 않음

Application.main() — 유일한 의존성 조립 지점 (Composition Root)
  └── new InMemorySampleRepository()
  └── new SampleService(repository)
  └── new SampleView(scanner)
  └── new SampleController(service, sampleView)
  └── new MainView(scanner, sampleController)
```

### 레이어별 핵심 메서드 시그니처

**Model**

```java
Sample(String sampleId, String name, double avgProdTime, double yield, int stock)
getSampleId() / getName() / getAvgProdTime() / getYield() / getStock()
equals(Object) / hashCode()   // sampleId 기준
```

**Repository**

```java
// SampleRepository 인터페이스 — DataPersistence PoC에서 구현체만 교체
void save(Sample sample)
List<Sample> findAll()
Optional<Sample> findById(String sampleId)
List<Sample> findByName(String keyword)
boolean existsById(String sampleId)
```

**Service**

```java
SampleService(SampleRepository sampleRepository)              // 생성자 주입
SampleResponse registerSample(SampleCreateRequest request)    // 중복 ID 검증 후 저장
List<SampleResponse> findAllSamples()
SampleResponse findSampleById(String sampleId)                // 없으면 SampleNotFoundException
List<SampleResponse> searchByName(String keyword)
```

**Controller**

```java
SampleController(SampleService sampleService, SampleView sampleView)
void handleSampleMenu()       // 시료 관리 서브 메뉴 루프
void handleRegisterSample()   // View 입력 → DTO → Service → View 출력
void handleListSamples()      // Service 조회 → View 출력
```

**View**

```java
SampleView(Scanner scanner)
SampleCreateRequest readSampleCreateRequest()
void printSampleList(List<SampleResponse> samples)
void printSuccessMessage(String message)
void printErrorMessage(String message)
```

---

## 주차별 상세 계획

### 1주차: ConsoleMVC PoC 구현

**목표**: 콘솔에서 시료 등록 → 목록 조회 흐름 정상 동작 + MVC 의존성 방향 검증

---

#### 단계 1 — SCAFFOLD (Day 1 오전, TDD 사이클 시작 전 선행 완료)

> SCAFFOLD는 TDD 사이클 대상이 아니다. 테스트할 동작이 없는 환경 설정이기 때문이다.

| ID | 태스크 | 레이어 | 일 수 | 의존성 | 구현 가이드 |
|----|--------|--------|------|--------|------------|
| ENV-01 | Gradle 프로젝트 초기화 | 환경설정 | 0.5일 | - | `build.gradle`: Java 17, JUnit 5 의존성 추가, `useJUnitPlatform()` 설정 |
| SCAFFOLD-01 | 패키지 디렉토리 구조 생성 | SCAFFOLD | 0.25일 | ENV-01 | `com.ssemi.consolemvc` 하위 model/dto/exception/repository/service/controller/view 패키지 |
| SCAFFOLD-02 | Application.java 진입점 스켈레톤 | SCAFFOLD | 0.25일 | SCAFFOLD-01 | main() + Composition Root 뼈대 (컴파일만 통과, 동작 구현 없음) |

**Definition of Done:**
- [ ] `./gradlew build` 성공
- [ ] 패키지 디렉토리 생성 완료
- [ ] `./gradlew test` 실행 시 "0 tests" 통과 (테스트 없음 상태)

---

#### 단계 2 — SHARED (Day 1 오후 ~ Day 2 오전, SCAFFOLD 완료 후)

> SHARED 중 `Sample`, `DTO`, `Exception`은 순수 데이터 구조로 TDD 사이클 적용이 선택적이다.
> `SampleRepository` 인터페이스는 계약 정의이므로 TDD 사이클 대상 아님.
> **단, `Sample.equals/hashCode`는 TDD 사이클을 적용한다.**

| ID | 태스크 | TDD 적용 | 레이어 | 일 수 | 의존성 |
|----|--------|---------|--------|------|--------|
| SHARED-01 | Sample 도메인 모델 구현 | ✅ (equals/hashCode) | MODEL | 0.5일 | SCAFFOLD-01 |
| SHARED-02 | DTO 클래스 구현 | ❌ (순수 데이터 구조) | DTO | 0.25일 | SHARED-01 |
| SHARED-03 | 예외 클래스 계층 구현 | ❌ (선언만) | EXCEPTION | 0.25일 | SCAFFOLD-01 |
| SHARED-04 | SampleRepository 인터페이스 정의 | ❌ (계약 정의) | REPOSITORY | 0.25일 | SHARED-01 |

**SHARED-01 TDD 사이클 대상 테스트**

> `@Nested`로 `Equals`, `HashCode` 그룹화.

| `@DisplayName` (한글) | 메서드명 | `@Nested` 그룹 | 검증 동작 | 파일 |
|----------------------|---------|--------------|---------|------|
| `"동일한 sampleId를 가진 두 Sample은 같다"` | `equals_sameSampleId_returnsTrue` | `Equals` | equals — sampleId 기준 | `SampleTest.java` |
| `"다른 sampleId를 가진 두 Sample은 다르다"` | `equals_differentSampleId_returnsFalse` | `Equals` | equals — sampleId 기준 | `SampleTest.java` |
| `"동일한 sampleId의 hashCode는 같다"` | `hashCode_sameSampleId_isConsistent` | `HashCode` | hashCode 일관성 | `SampleTest.java` |

**Definition of Done:**
- [ ] `./gradlew test --tests "*.SampleTest"` 통과
- [ ] SampleRepository 인터페이스 메서드 계약 확정
- [ ] 예외 클래스 계층 컴파일 성공

---

#### 단계 3 — FEATURE (Day 2 오후 ~ Day 4, SHARED 완료 후)

> **TDD 필수 구간.** 각 태스크는 반드시 `TDD Plan 작성 → 사람 승인 → RED → GREEN → REVIEW` 순서를 따른다.
> 테스트 없이 구현 코드를 먼저 작성했다면 즉시 삭제하고 Plan부터 다시 시작한다.

---

##### TDD Cycle 1 — InMemorySampleRepository (Day 2 오후)

```
⚠️ TDD Plan 작성 → 사람 승인
       ↓
[RED]   TEST-01 작성 → ./gradlew test (실패 확인)
       ↓
[GREEN] DB-01 구현 → ./gradlew test (통과 확인)
       ↓
⚠️ REVIEW → 사람 승인 → 다음 사이클
```

**TEST-01 작성 테스트 목록 (TDD Plan 참고)**

> `@Nested`로 메서드별 그룹화. 100% 커버리지 기준으로 브랜치(분기)까지 모두 포함.

| `@DisplayName` (한글) | 메서드명 | `@Nested` 그룹 | 검증 동작 |
|----------------------|---------|--------------|---------|
| `"저장 후 findById로 조회 가능하다"` | `save_validSample_canBeFoundById` | `Save` | save → findById 연동 |
| `"중복 ID 저장 시 기존 항목을 덮어쓴다"` | `save_duplicateId_overwritesExistingEntry` | `Save` | 동일 ID 재저장 동작 |
| `"저장소가 비어있으면 빈 리스트를 반환한다"` | `findAll_emptyRepository_returnsEmptyList` | `FindAll` | 빈 컬렉션 반환 |
| `"여러 건 저장 시 등록 순서를 보장한다"` | `findAll_multipleItems_returnsInInsertionOrder` | `FindAll` | LinkedHashMap 순서 보장 |
| `"존재하는 ID로 조회하면 Optional에 담아 반환한다"` | `findById_existingId_returnsNonEmptyOptional` | `FindById` | Optional.of(sample) |
| `"없는 ID로 조회하면 빈 Optional을 반환한다"` | `findById_nonExistingId_returnsEmptyOptional` | `FindById` | Optional.empty() |
| `"키워드를 이름에 포함한 시료를 반환한다"` | `findByName_keywordMatch_returnsSamples` | `FindByName` | contains 부분 검색 |
| `"키워드와 일치하는 시료가 없으면 빈 리스트를 반환한다"` | `findByName_noMatch_returnsEmptyList` | `FindByName` | 빈 컬렉션 반환 |
| `"존재하는 ID는 true를 반환한다"` | `existsById_existingId_returnsTrue` | `ExistsById` | 존재 여부 확인 |
| `"없는 ID는 false를 반환한다"` | `existsById_nonExistingId_returnsFalse` | `ExistsById` | 미존재 여부 확인 |

| ID | 태스크 | 순서 | 레이어 | 일 수 | 의존성 |
|----|--------|------|--------|------|--------|
| TEST-01 | InMemorySampleRepositoryTest 작성 | 1st (RED) | TEST | 0.25일 | SHARED-04 |
| DB-01 | InMemorySampleRepository 구현 | 2nd (GREEN) | REPOSITORY | 0.25일 | TEST-01 |

---

##### TDD Cycle 2 — SampleService (Day 3 오전)

```
⚠️ TDD Plan 작성 → 사람 승인
       ↓
[RED]   TEST-02 작성 → ./gradlew test (실패 확인)
       ↓
[GREEN] SVC-01 구현 → ./gradlew test (통과 확인)
       ↓
⚠️ REVIEW → 사람 승인 → 다음 사이클
```

**TEST-02 작성 테스트 목록 (TDD Plan 참고)**

> `SampleRepository`는 Mockito `@Mock`으로 격리. `@Nested`로 메서드별 그룹화.

| `@DisplayName` (한글) | 메서드명 | `@Nested` 그룹 | 검증 동작 |
|----------------------|---------|--------------|---------|
| `"정상 요청 시 시료가 저장된다"` | `registerSample_validRequest_savesSample` | `RegisterSample` | save 1회 호출 + 반환값 검증 |
| `"중복 ID 등록 시 DuplicateSampleIdException이 발생한다"` | `registerSample_duplicateId_throwsDuplicateSampleIdException` | `RegisterSample` | 예외 타입 및 메시지 |
| `"등록된 시료가 없으면 빈 리스트를 반환한다"` | `findAllSamples_noSamples_returnsEmptyList` | `FindAllSamples` | null이 아닌 빈 List |
| `"등록된 시료가 있으면 전체 목록을 반환한다"` | `findAllSamples_hasSamples_returnsSampleList` | `FindAllSamples` | 저장된 건수 = 반환 건수 |
| `"존재하는 ID로 조회하면 시료를 반환한다"` | `findSampleById_existingId_returnsSample` | `FindSampleById` | 정상 조회 + 필드 검증 |
| `"없는 ID로 조회하면 SampleNotFoundException이 발생한다"` | `findSampleById_nonExistingId_throwsSampleNotFoundException` | `FindSampleById` | 예외 타입 검증 |
| `"이름 키워드로 검색하면 포함 시료를 반환한다"` | `searchByName_keywordMatch_returnsMatchingSamples` | `SearchByName` | 부분 검색 결과 |
| `"키워드와 일치하는 시료가 없으면 빈 리스트를 반환한다"` | `searchByName_noMatch_returnsEmptyList` | `SearchByName` | 빈 컬렉션 반환 |

| ID | 태스크 | 순서 | 레이어 | 일 수 | 의존성 |
|----|--------|------|--------|------|--------|
| TEST-02 | SampleServiceTest 작성 | 1st (RED) | TEST | 0.25일 | DB-01, SHARED-02, SHARED-03 |
| SVC-01 | SampleService 구현 | 2nd (GREEN) | SERVICE | 0.25일 | TEST-02 |

---

##### TDD Cycle 3 — SampleController (Day 3 오후)

```
⚠️ TDD Plan 작성 → 사람 승인
       ↓
[RED]   TEST-03 작성 → ./gradlew test (실패 확인)
       ↓
[GREEN] CTL-01 구현 → ./gradlew test (통과 확인)
       ↓
⚠️ REVIEW → 사람 승인 → 다음 사이클
```

**TEST-03 작성 테스트 목록 (TDD Plan 참고)**

> `SampleService`, `SampleView`는 Mockito `@Mock`으로 격리. `@InjectMocks`로 Controller에 주입.

| `@DisplayName` (한글) | 메서드명 | `@Nested` 그룹 | 검증 동작 |
|----------------------|---------|--------------|---------|
| `"시료 등록 요청 시 서비스의 등록 메서드를 호출한다"` | `handleRegisterSample_validInput_callsServiceRegister` | `HandleRegisterSample` | `SampleService.registerSample` 1회 호출 |
| `"시료 등록 중 예외 발생 시 뷰의 에러 메시지 출력을 호출한다"` | `handleRegisterSample_serviceThrows_callsViewError` | `HandleRegisterSample` | `SampleView.printErrorMessage` 1회 호출 |
| `"시료 목록 조회 시 서비스 조회 후 뷰 출력을 호출한다"` | `handleListSamples_afterServiceQuery_callsViewPrint` | `HandleListSamples` | `findAllSamples` + `printSampleList` 각 1회 호출 |

| ID | 태스크 | 순서 | 레이어 | 일 수 | 의존성 |
|----|--------|------|--------|------|--------|
| TEST-03 | SampleControllerTest 작성 | 1st (RED) | TEST | 0.25일 | SVC-01, SHARED-02 |
| CTL-01 | SampleController 구현 | 2nd (GREEN) | CONTROLLER | 0.25일 | TEST-03 |

---

##### View 및 통합 조립 (Day 4)

> View는 콘솔 입출력 담당으로 자동화 단위 테스트 범위 밖이다.
> App-01 이후 수동 통합 테스트(콘솔 실행)로 검증한다.

| ID | 태스크 | 레이어 | 일 수 | 의존성 | 구현 가이드 |
|----|--------|--------|------|--------|------------|
| UI-01 | SampleView 구현 | VIEW | 0.5일 | SHARED-02 | readSampleCreateRequest(): Scanner 입력; printSampleList(): 표 형식 출력 |
| UI-02 | MainView 구현 | VIEW | 0.25일 | UI-01 | run(): while 루프 + switch; printMainMenu() |
| APP-01 | Application.java 의존성 조립 완성 | SCAFFOLD | 0.25일 | CTL-01, UI-02 | Composition Root 완성 → `./gradlew run` 으로 수동 통합 검증 |

**수동 통합 검증 시나리오**

```
1. ./gradlew run 실행
2. 메인 메뉴 [1] 시료 관리 선택
3. [1] 시료 등록 → S-001, 실리콘웨이퍼-8인치, 10.0, 0.9, 100 입력
4. [2] 시료 목록 조회 → S-001 표시 확인
5. [0] 뒤로 → [0] 종료 확인
```

---

## 의존성 맵 (TDD 순서 반영)

```
ENV-01
  └── SCAFFOLD-01
        ├── SCAFFOLD-02
        ├── SHARED-01 (+ SampleTest TDD)
        │     ├── SHARED-02
        │     └── SHARED-04
        │           └── TEST-01 (RED) → DB-01 (GREEN)
        │                               └── TEST-02 (RED) → SVC-01 (GREEN)
        │                                                     └── TEST-03 (RED) → CTL-01 (GREEN)
        │                                                                           └── APP-01
        └── SHARED-03 ──────────────────────────────────────► TEST-02

UI-01 → CTL-01 (SampleView를 Controller에 주입)
UI-02 → APP-01 (MainView를 Composition Root에서 조립)
```

**Critical Path**: `ENV-01 → SCAFFOLD-01 → SHARED-01 → SHARED-04 → TEST-01 → DB-01 → TEST-02 → SVC-01 → TEST-03 → CTL-01 → APP-01`

> ⚠️ Critical Path 상의 모든 FEATURE 태스크 앞에 TEST(RED) 태스크가 선행된다.

---

## 전체 태스크 Master Backlog

> TDD 순서에 따라 TEST 태스크가 해당 FEATURE 구현 태스크보다 반드시 앞에 온다.

| ID | 태스크 | TDD 단계 | 레이어 | 일 수 | 단계 |
|----|--------|---------|--------|------|------|
| ENV-01 | Gradle 프로젝트 초기화 | - | 환경설정 | 0.5일 | SCAFFOLD |
| SCAFFOLD-01 | 패키지 디렉토리 구조 생성 | - | SCAFFOLD | 0.25일 | SCAFFOLD |
| SCAFFOLD-02 | Application.java 스켈레톤 | - | SCAFFOLD | 0.25일 | SCAFFOLD |
| SHARED-01 | Sample 도메인 모델 구현 | GREEN (equals/hashCode) | MODEL | 0.5일 | SHARED |
| SHARED-02 | DTO 클래스 구현 | - | DTO | 0.25일 | SHARED |
| SHARED-03 | 예외 클래스 계층 구현 | - | EXCEPTION | 0.25일 | SHARED |
| SHARED-04 | SampleRepository 인터페이스 정의 | - | REPOSITORY | 0.25일 | SHARED |
| **TEST-01** | **InMemorySampleRepositoryTest 작성** | **RED** | TEST | 0.25일 | FEATURE |
| DB-01 | InMemorySampleRepository 구현 | GREEN | REPOSITORY | 0.25일 | FEATURE |
| **TEST-02** | **SampleServiceTest 작성** | **RED** | TEST | 0.25일 | FEATURE |
| SVC-01 | SampleService 구현 | GREEN | SERVICE | 0.25일 | FEATURE |
| **TEST-03** | **SampleControllerTest 작성** | **RED** | TEST | 0.25일 | FEATURE |
| CTL-01 | SampleController 구현 | GREEN | CONTROLLER | 0.25일 | FEATURE |
| UI-01 | SampleView 구현 | - | VIEW | 0.5일 | FEATURE |
| UI-02 | MainView 구현 | - | VIEW | 0.25일 | FEATURE |
| APP-01 | Application.java 조립 완성 | - | SCAFFOLD | 0.25일 | FEATURE |

**총 일 수**: 5.0일 (버퍼 20% 적용 실효 4.0일 → 1주 내 완료 가능)

---

## 리스크 대응 계획

| 리스크 | 심각도 | 대응 방안 |
|--------|--------|---------|
| 구현 코드 선착수 (TDD 위반) | 🔴 높음 | 위반 발견 즉시 해당 코드 삭제 → TDD Plan부터 재시작. "참고용 보관" 합리화 금지 |
| Repository 인터페이스 계약 불일치 | 🔴 높음 | `Optional` 반환, Oracle 친화적 시그니처로 SHARED-04에서 확정 |
| View ↔ Model 의존성 침투 | 🔴 높음 | Controller 경유 원칙 강제, 매 커밋 전 의존성 방향 체크 |
| JaCoCo 커버리지 미달 | 🔴 높음 | `./gradlew check` 빌드 실패로 즉시 감지. 미달 브랜치 확인 후 누락 테스트 추가 |
| TDD Plan 없이 테스트 작성 | 🟡 중간 | Plan 없이 작성된 테스트는 의도 불명. 테스트 삭제 후 Plan부터 재작성 |
| REVIEW 생략 후 다음 사이클 착수 | 🟡 중간 | REVIEW 없이 착수 시 설계 부채 누적. GREEN 완료 직후 반드시 사람 검토 요청 |
| 테스트 컨벤션 위반 (메서드명·구조) | 🟡 중간 | `@DisplayName` 누락, 메서드명 형식 불일치는 REVIEW 단계에서 즉시 수정 |

---

## 완료 기준 (Definition of Done)

### 기능 완료 기준

- [ ] `model`, `controller`, `view`, `repository`, `service` 패키지 구조 완성
- [ ] 콘솔에서 시료 등록 → 시료 목록 조회 흐름 정상 동작
- [ ] View ↔ Model 직접 의존 없음 (Controller/Service 경유)
- [ ] `./gradlew test` 전체 통과 (0 failures)
- [ ] 컨벤셔널 커밋 최소 3회 이상 (`feat:`, `test:`, `refactor:` 등)

### 테스트 품질 기준

- [ ] `./gradlew jacocoTestCoverageVerification` 통과 — Line/Branch 100% (View·DTO·Exception 제외)
- [ ] 모든 테스트 클래스에 `@DisplayName` 작성 (클래스 + 메서드)
- [ ] 모든 테스트 메서드에 `// given / // when / // then` 구조 적용
- [ ] 메서드명 형식 `{메서드명}_{조건/상황}_{기대결과}` 준수
- [ ] `@DisplayName`은 `"~하면 ~한다"` 형식 한글 문장
- [ ] 같은 메서드 시나리오는 `@Nested` 클래스로 그룹화
- [ ] AssertJ(`assertThat`) 사용, 예외 검증은 `assertThatThrownBy`
- [ ] 테스트 픽스처는 `given...` 헬퍼 메서드 또는 Fixture 클래스로 분리
- [ ] Repository·View 등 외부 의존성만 Mockito로 Mock 처리

### TDD 프로세스 준수 기준

- [ ] 모든 FEATURE 태스크에 TDD Plan이 사전 작성되었다
- [ ] 모든 TDD Plan이 사람의 승인을 받았다
- [ ] 각 테스트가 구현 전에 실패하는 것을 직접 확인했다
- [ ] 각 테스트가 "기능 미구현"으로 실패했다 (오타/컴파일 오류가 아님)
- [ ] 각 GREEN 구현이 테스트를 통과시킬 최소 코드였다
- [ ] 모든 FEATURE 사이클이 REVIEW를 거쳤다
- [ ] 도메인 로직은 Mock 없이 실제 코드로 테스트했다

---

## 용어 정의

| 용어 | 설명 |
|------|------|
| SCAFFOLD | 프로젝트 초기화, 디렉토리 구조, 환경설정 — TDD 사이클 대상 아님 |
| SHARED | 재사용 도메인/인터페이스/공통 예외 — FEATURE 착수 전 완료 필수, 일부 TDD 적용 |
| FEATURE | 기능별 구현체 — 모든 항목에 TDD 사이클 필수 |
| RED | TDD 1단계: 실패하는 테스트 작성 (구현 코드 없음) |
| GREEN | TDD 2단계: 테스트를 통과시킬 최소 구현 |
| REVIEW | TDD 3단계: 코드 품질 검토 + 다음 사이클 결정 (사람 승인 필수) |
| TDD Plan | 테스트 코드 작성 전 사람 승인을 받는 테스트 계획 문서 |
| Composition Root | 모든 의존성이 조립되는 단일 진입점 (Application.java) |
| Critical Path | 지연 시 전체 일정에 영향을 주는 태스크 체인 |

---

*변경 이력*

| 버전 | 날짜 | 변경 내용 |
|-----|------|---------|
| 1.0.0 | 2026-06-12 | 최초 작성 |
| 1.1.0 | 2026-06-12 | ConsoleMVC PoC 범위로 축소 (SampleOrderSystem 전체 계획 제거) |
| 1.2.0 | 2026-06-12 | TDD 개발 프로세스 반영 (Red-Green-Review 사이클, Human-in-the-loop 체크포인트, 태스크 순서 재배치) |
| 1.3.0 | 2026-06-12 | Java TDD 컨벤션 반영 — 테스트 메서드명 영어 형식 통일, @Nested 그룹화, AssertJ/Mockito 명시, JaCoCo 100% 커버리지 설정, 누락 테스트 케이스 추가 (findById·findAll·findByName 분기, findAllSamples·searchByName 분기, handleRegisterSample 예외 경로) |
