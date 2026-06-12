# PRD — ConsoleMVC (MVC 스켈레톤 코드 PoC)

> **프로젝트명:** ConsoleMVC  
> **분류:** PoC #1 — MVC 스켈레톤 코드  
> **버전:** 1.0.0  
> **작성일:** 2026-06-12  
> **상위 PRD:** `TOTAL_PRD.md` (전체 총괄)

---

## 목차

1. [배경 및 목적](#1-배경-및-목적)
2. [목표 및 성공 지표](#2-목표-및-성공-지표)
3. [기능 요구사항](#3-기능-요구사항)
4. [아키텍처 설계](#4-아키텍처-설계)
5. [비기능 요구사항](#5-비기능-요구사항)
6. [개발 주안점 (절대 준수 사항)](#6-개발-주안점-절대-준수-사항)
7. [완료 기준 (Definition of Done)](#7-완료-기준-definition-of-done)
8. [용어 정의](#8-용어-정의)

---

## 1. 배경 및 목적

### 1.1 PoC 목적

반도체 시료 생산주문관리 시스템(SampleOrderSystem) 개발에 앞서, **콘솔 기반 MVC 아키텍처의 구조적 타당성**을 검증한다. 본 PoC는 Model / Controller / View의 역할 분리가 실제로 동작하는지 확인하는 것을 목표로 한다.

### 1.2 핵심 검증 항목

> "콘솔 기반 Java 애플리케이션에서 MVC 패턴이 올바르게 적용되고, 각 레이어의 역할이 명확하게 분리되는가?"

### 1.3 In-Scope / Out-of-Scope

**In-Scope:**
- Model / Controller / View 패키지 구조 구현
- 각 레이어의 역할 분리 및 의존성 방향 확인
- 시료(Sample) 도메인을 활용한 간단한 CRUD 스켈레톤 구현
- 콘솔 입출력 흐름 구현

**Out-of-Scope:**
- 데이터 영속성 (파일/DB 저장) — DataPersistence PoC 담당
- 실제 비즈니스 로직 전체 구현 — SampleOrderSystem 담당
- 테스트 데이터 자동 생성 — DummyDataGenerator PoC 담당

---

## 2. 목표 및 성공 지표

### 2.1 성공 기준

| 항목 | 기준 |
|------|------|
| 패키지 구조 | `model`, `view`, `controller` 패키지가 명확히 분리 |
| 의존성 방향 | Controller → Model (단방향), View → Controller 또는 Model 직접 접근 금지 |
| 동작 확인 | 콘솔에서 시료 등록 → 조회 흐름이 에러 없이 동작 |
| 코드 분리 | View에 비즈니스 로직 0건, Model에 UI 로직 0건 |

### 2.2 KPI

- MVC 레이어 간 의존성 위반 건수: **0건**
- 콘솔 입출력 정상 동작률: **100%**
- 단위 테스트 통과율: **100%**

---

## 3. 기능 요구사항

### 3.1 Must Have

#### 3.1.1 시료 도메인 Model 구현

```
As a 개발자,
I want to Sample 도메인 Model 클래스를 정의하고,
So that 비즈니스 데이터 구조를 명확하게 표현할 수 있다.

Acceptance Criteria:
  - [ ] Sample 클래스: sampleId, name, avgProdTime, yield, stock 필드 포함
  - [ ] 불변 필드는 final 선언
  - [ ] getter 메서드 구현 (setter 최소화)
  - [ ] equals/hashCode 구현 (sampleId 기준)
```

#### 3.1.2 View 레이어 구현 (콘솔 입출력)

```
As a 주문 담당자,
I want to 콘솔에서 메뉴를 선택하고 입력할 수 있고,
So that 시스템과 상호작용할 수 있다.

Acceptance Criteria:
  - [ ] 메인 메뉴 출력 기능 구현
  - [ ] 사용자 입력 수신 기능 구현
  - [ ] 결과 출력 기능 구현
  - [ ] View 클래스에 비즈니스 로직 포함 금지
  - [ ] 입력 오류 시 재입력 유도 메시지 출력
```

#### 3.1.3 Controller 레이어 구현

```
As a 시스템,
I want to View의 요청을 받아 적절한 처리 흐름을 제어하고,
So that View와 Model 사이의 결합도를 낮출 수 있다.

Acceptance Criteria:
  - [ ] SampleController 클래스 구현
  - [ ] View → Controller → Model 의존성 방향 준수
  - [ ] Controller에서 View 출력 로직 직접 포함 금지
  - [ ] 시료 등록, 조회 기능 흐름 제어
```

#### 3.1.4 Repository 레이어 구현 (인메모리)

```
As a 개발자,
I want to 인메모리 저장소로 CRUD를 구현하고,
So that DataPersistence PoC 연동 전 구조를 검증할 수 있다.

Acceptance Criteria:
  - [ ] SampleRepository 인터페이스 정의
  - [ ] InMemorySampleRepository 구현체 작성
  - [ ] save, findAll, findById, findByName 메서드 구현
  - [ ] 추후 영속성 저장소로 교체 가능한 구조 (인터페이스 분리)
```

### 3.2 Should Have

- Service 레이어 추가 (Controller와 Repository 사이 비즈니스 로직 분리)
- 메뉴 번호 입력 유효성 검증

### 3.3 Won't Have (이번 버전 제외)

- 실제 파일/DB 연동 — 이유: DataPersistence PoC 범위
- 전체 주문 처리 흐름 — 이유: SampleOrderSystem 범위

---

## 4. 아키텍처 설계

### 4.1 패키지 구조

```
com.ssemi.consolemvc
├── model
│   └── Sample.java            # 도메인 엔티티
├── repository
│   ├── SampleRepository.java  # 인터페이스
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

### 4.2 의존성 방향

```
View → Controller → Service → Repository → Model
                                              ↑
                              (Model은 어떤 레이어도 의존하지 않음)
```

### 4.3 콘솔 화면 흐름

```
[메인 메뉴]
  [1] 시료 관리
  [0] 종료

[시료 관리]
  [1] 시료 등록
  [2] 시료 목록 조회
  [0] 뒤로
```

---

## 5. 비기능 요구사항

### 5.1 성능

- 콘솔 입출력 응답: **즉시 (1초 이내)**

### 5.2 코드 품질

- 메서드 길이: **20줄 이하** 권장
- 클래스당 단일 책임 원칙 준수
- 매직 넘버 상수화

### 5.3 확장성

- Repository 인터페이스를 통해 저장 방식 교체 용이
- View와 Controller 분리를 통해 화면 변경 시 비즈니스 로직 불변

---

## 6. 개발 주안점 (절대 준수 사항)

| # | 주안점 | 적용 방법 |
|---|-------|---------|
| 1 | **문서 관리** | 이 PRD.md를 프로젝트 시작 전 완성, 구현 중 변경사항 즉시 반영 |
| 2 | **Harness 도입** | TDD 스킬 활용 — 구현 전 테스트 코드 먼저 작성 |
| 3 | **Test(Unit, E2E)** | SampleService, SampleController 단위 테스트 필수 (JUnit 5) |
| 4 | **CleanCode** | 레이어별 역할 명확화, 의미 있는 변수명 사용 |
| 5 | **Commit 이력** | `feat:`, `test:`, `refactor:` 등 컨벤셔널 커밋 준수 |

---

## 7. 완료 기준 (Definition of Done)

- [ ] `model`, `controller`, `view`, `repository`, `service` 패키지 구조 완성
- [ ] 콘솔에서 시료 등록 → 시료 목록 조회 흐름 정상 동작
- [ ] View ↔ Model 직접 의존 없음 (Controller 경유)
- [ ] 단위 테스트 1개 이상 통과
- [ ] 컨벤셔널 커밋 메시지로 최소 3회 이상 커밋

---

## 8. 용어 정의

| 용어 | 설명 |
|------|------|
| Model | 도메인 데이터 구조 및 비즈니스 규칙을 담당하는 레이어 |
| View | 사용자에게 정보를 표시하고 입력을 받는 콘솔 UI 레이어 |
| Controller | View의 요청을 받아 Model과 Repository를 조율하는 레이어 |
| Repository | 데이터 저장소에 대한 CRUD 접근을 추상화하는 레이어 |
| PoC | Proof of Concept — 기능 검증을 위한 소규모 선행 구현 |

---

*변경 이력*

| 버전 | 날짜 | 변경 내용 | 작성자 |
|-----|------|---------|-------|
| 1.0.0 | 2026-06-12 | 최초 작성 | - |
