# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 프로젝트 개요

**ConsoleMVC**는 반도체 시료 생산주문관리 시스템(SampleOrderSystem) 개발 전, 콘솔 기반 MVC 아키텍처의 구조적 타당성을 검증하는 **PoC #1** 프로젝트다. 시료(Sample) 도메인을 활용한 CRUD 스켈레톤 구현이 목표이며, 데이터 영속성은 Out-of-Scope다.

## 빌드 및 실행 명령어

```bash
# 빌드
./gradlew build

# 전체 테스트 실행
./gradlew test

# 단일 테스트 클래스 실행
./gradlew test --tests "com.ssemi.consolemvc.service.SampleServiceTest"

# 단일 테스트 메서드 실행
./gradlew test --tests "com.ssemi.consolemvc.service.SampleServiceTest.특정_메서드명"

# 애플리케이션 실행
./gradlew run

# 빌드 결과물 정리
./gradlew clean
```

## 아키텍처

### 의존성 방향 (절대 준수)

```
View → Controller → Service → Repository → Model
```

- **View**: UI 로직만 담당. 비즈니스 로직 포함 금지. Model 직접 접근 금지
- **Controller**: 흐름 제어만 담당. View 출력 로직 직접 포함 금지
- **Service**: 도메인 로직 수행 (Controller와 Repository 사이)
- **Repository**: 인터페이스로 추상화. 현재는 InMemory 구현체 사용
- **Model**: 어떤 레이어도 의존하지 않음

### 패키지 구조

```
com.ssemi.consolemvc
├── model
│   └── Sample.java            # sampleId, name, avgProdTime, yield, stock
├── repository
│   ├── SampleRepository.java  # 인터페이스 (save, findAll, findById, findByName)
│   └── InMemorySampleRepository.java
├── service
│   └── SampleService.java
├── controller
│   └── SampleController.java
├── view
│   ├── MainView.java          # 메인 메뉴 출력
│   └── SampleView.java        # 시료 관련 화면 출력
└── Application.java           # 진입점
```

### 핵심 엔티티

```java
// Sample 필드
String sampleId;       // 예: "S-001"
String name;           // 예: "실리콘 웨이퍼-8인치"
double avgProdTime;    // 평균 생산시간 (분/ea)
double yield;          // 수율 (0.0 ~ 1.0)
int stock;             // 현재 재고 수량 (ea)
```

## 개발 필수 원칙

| # | 원칙 | 적용 방법 |
|---|------|---------|
| 1 | **TDD 필수** | 구현 전 테스트 코드 먼저 작성 (JUnit 5). `example-skills:cra-test-driven-development` 스킬 활용 |
| 2 | **MVC 의존성 방향 준수** | View ↔ Model 직접 의존 건수: 0건 |
| 3 | **메서드 길이** | 20줄 이하 권장 |
| 4 | **Repository 인터페이스 분리** | 추후 파일/DB 저장소로 교체 가능한 구조 유지 |
| 5 | **컨벤셔널 커밋** | `feat:`, `test:`, `refactor:`, `fix:` 접두어 사용 (한국어 메시지) |

## 콘솔 화면 흐름

```
[메인 메뉴]
  [1] 시료 관리
  [0] 종료

[시료 관리]
  [1] 시료 등록
  [2] 시료 목록 조회
  [0] 뒤로
```

## 완료 기준 (Definition of Done)

- [ ] `model`, `controller`, `view`, `repository`, `service` 패키지 구조 완성
- [ ] 콘솔에서 시료 등록 → 시료 목록 조회 흐름 정상 동작
- [ ] View ↔ Model 직접 의존 없음 (Controller 경유)
- [ ] 단위 테스트 1개 이상 통과
- [ ] 컨벤셔널 커밋 최소 3회 이상

## 관련 문서

- `docs/PRD.md` — ConsoleMVC PoC 세부 요구사항
- `docs/TOTAL_PRD.md` — 전체 시스템(SampleOrderSystem) 총괄 PRD
- `docs/rules/java-code-convention.md` — Java 코드 컨벤션
- `docs/rules/git-convention.md` — Git 커밋/브랜치 전략
