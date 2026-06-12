# ConsoleMVC — 반도체 시료 관리 콘솔 애플리케이션

> **PoC #1** — 반도체 시료 생산주문관리 시스템(SampleOrderSystem) 개발 전, 콘솔 기반 MVC 아키텍처의 구조적 타당성을 검증하는 선행 구현 프로젝트

---

## 프로젝트 개요

| 항목 | 내용 |
|------|------|
| **분류** | PoC #1 — MVC 스켈레톤 |
| **도메인** | 반도체 시료(Sample) 관리 |
| **언어** | Java 17 |
| **빌드** | Gradle 8.10 |
| **테스트** | JUnit 5 + AssertJ + Mockito |
| **커버리지** | JaCoCo |

---

## 아키텍처

### 의존성 방향

```
View → Controller → Service → Repository → Model
```

각 레이어는 단방향 의존성을 준수하며, View는 Model에 직접 접근하지 않는다.

| 레이어 | 역할 |
|--------|------|
| **Model** | 도메인 엔티티(Sample). 어떤 레이어도 의존하지 않음 |
| **Repository** | 인터페이스로 추상화된 데이터 접근 계층. 현재 InMemory 구현체 사용 |
| **Service** | 도메인 비즈니스 로직 (중복 검증, 조회 등) |
| **Controller** | View 요청 수신 → Service 호출 → View 응답 위임 |
| **View** | 콘솔 입출력만 담당. 비즈니스 로직 포함 금지 |

### 패키지 구조

```
src/main/java/com/ssemi/consolemvc
├── model
│   └── Sample.java
├── repository
│   ├── SampleRepository.java          # 인터페이스
│   └── InMemorySampleRepository.java  # 구현체
├── service
│   └── SampleService.java
├── controller
│   └── SampleController.java
├── view
│   ├── MainView.java                  # 메인 메뉴
│   └── SampleView.java                # 시료 관련 화면
├── dto
│   ├── SampleCreateRequest.java
│   └── SampleResponse.java
├── exception
│   ├── BusinessException.java
│   ├── DuplicateSampleIdException.java
│   └── SampleNotFoundException.java
└── Application.java                   # 진입점 (Composition Root)
```

---

## 빌드 및 실행

### 사전 조건

- Java 17 이상
- Gradle Wrapper 포함 (`./gradlew` 사용)

### 명령어

```bash
# 애플리케이션 실행
./gradlew run

# 전체 테스트 + 커버리지 리포트 생성
./gradlew test

# 커버리지 검증 포함 전체 빌드
./gradlew build

# 빌드 결과물 정리
./gradlew clean
```

### 테스트 리포트 위치

```
build/reports/tests/test/index.html       # JUnit 테스트 결과
build/reports/jacoco/test/html/index.html # JaCoCo 커버리지
```

---

## 콘솔 화면 흐름

```
=== 시료 주문 관리 시스템 ===
[1] 시료 관리
[0] 종료
선택: 1

--- 시료 관리 ---
[1] 시료 등록
[2] 시료 목록 조회
[0] 뒤로
선택: 1

시료 ID: S-001
시료명: 실리콘웨이퍼-8인치
평균 생산시간(분/ea): 10.0
수율(0.0~1.0): 0.9
재고 수량(ea): 100
[완료] 시료가 등록되었습니다. (S-001)

선택: 2
┌────────┬──────────────────────┬──────────┬──────┬──────┐
│ ID     │ 시료명               │ 생산시간 │ 수율 │ 재고 │
├────────┼──────────────────────┼──────────┼──────┼──────┤
│ S-001  │ 실리콘웨이퍼-8인치   │   10.0분 │  90% │  100 │
└────────┴──────────────────────┴──────────┴──────┴──────┘
```

---

## 테스트 현황

| 레이어 | 테스트 종류 | 개수 | 라인 커버리지 |
|--------|------------|------|-------------|
| Model | 단위 | 5 | 100% |
| Repository | 단위 | 8 | 100% |
| Service | 단위 | 8 | 100% |
| Controller | 단위 | 3 | 100% |
| View (통합) | 통합 | 10 | 100% |
| **합계** | | **36** | |

> `Application.java`(진입점 배선 코드)는 커버리지 검증 대상에서 제외

### 테스트 실행

```bash
# 단일 클래스 실행
./gradlew test --tests "com.ssemi.consolemvc.service.SampleServiceTest"

# 통합 테스트만 실행
./gradlew test --tests "com.ssemi.consolemvc.integration.*"
```

---

## 핵심 엔티티

```java
// Sample 필드
String sampleId;       // 예: "S-001"
String name;           // 예: "실리콘웨이퍼-8인치"
double avgProdTime;    // 평균 생산시간 (분/ea)
double yield;          // 수율 (0.0 ~ 1.0)
int stock;             // 현재 재고 수량 (ea)
```

---

## 커밋 히스토리

| 커밋 | 설명 |
|------|------|
| `🎉 chore` | Gradle 프로젝트 초기화 |
| `📝 docs` | 개발 컨벤션 문서 추가 |
| `📝 docs` | PRD(제품 요구사항 문서) 추가 |
| `🧑‍💻 dx` | CLAUDE.md 프로젝트 가이드 추가 |
| `🏗️ chore` | SCAFFOLD·SHARED 단계 구현 |
| `✅ test` | [Cycle 1] InMemorySampleRepository RED+GREEN |
| `✅ test` | [Cycle 2] SampleService RED+GREEN |
| `✅ test` | [Cycle 3] SampleController RED+GREEN |
| `✨ feat` | UI 레이어 및 Composition Root 완성 |
| `✅ test` | Sample equals/hashCode 테스트 추가 |
| `✅ test` | MainView·SampleView 통합 테스트 추가 |

---

## 관련 문서

- [`docs/PRD.md`](docs/PRD.md) — ConsoleMVC PoC 세부 요구사항
- [`docs/TOTAL_PRD.md`](docs/TOTAL_PRD.md) — 전체 시스템 총괄 PRD
- [`docs/rules/java-code-convention.md`](docs/rules/java-code-convention.md) — Java 코드 컨벤션
- [`docs/rules/git-convention.md`](docs/rules/git-convention.md) — Git 커밋/브랜치 전략

