## Git Commit Convention

### 커밋 메시지 형식
```
<type>(<scope>): <subject>

<body>

<footer>
```

### Type
- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `refactor`: 코드 리팩토링 (기능 변경 없음)
- `style`: 코드 포맷팅, 세미콜론, 들여쓰기 등 (로직 변경 없음)
- `docs`: 문서 수정 (README, Javadoc 등)
- `test`: 테스트 코드 추가/수정
- `chore`: 빌드 설정, 패키지 매니저 설정 (gradle, maven 등)
- `perf`: 성능 개선
- `ci`: CI/CD 설정 변경
- `build`: 빌드 시스템 또는 외부 종속성 변경
- `revert`: 이전 커밋 되돌리기

### Scope (선택)
변경 범위를 명시. 보통 패키지명, 모듈명, 레이어명 사용
- 예: `feat(controller)`, `fix(service)`, `refactor(repository)`

### Subject 규칙
- 50자 이내
- 명령형, 현재 시제 사용 ("추가", "수정")
- 마지막에 마침표 없음

### Body (선택)
- 무엇을 왜 변경했는지 설명 (어떻게는 코드로 확인 가능)
- 72자마다 줄바꿈 권장
- 여러 변경사항은 `- ` 불릿으로 나열

### Footer (선택)
- 이슈 트래커 연동: `Resolves: #123`, `Related to: #456`
- Breaking Change 명시: `BREAKING CHANGE: <설명>`

### 브랜치 전략
```
main / master              - 배포 가능한 상태
develop                     - 다음 릴리즈 개발 브랜치
feature/{이슈번호}-{설명}    - 기능 개발 (예: feature/123-add-login-api)
fix/{이슈번호}-{설명}        - 버그 수정
hotfix/{설명}               - 운영 긴급 수정
release/{버전}              - 릴리즈 준비
```

### 예시
```
feat(auth): JWT 기반 로그인 기능 추가

- AuthController에 /login, /refresh 엔드포인트 추가
- JwtTokenProvider 구현
- SecurityConfig에 JWT 필터 등록

Resolves: #45
```

```
fix(order): @Transactional 누락으로 인한 재고 동시성 이슈 수정

OrderService.placeOrder()에 @Transactional 추가하여
재고 차감과 주문 생성이 원자적으로 처리되도록 수정

Resolves: #87
```

### 추가 규칙
- 커밋은 작은 단위로 자주 (한 커밋 = 한 가지 논리적 변경)
- WIP 커밋은 squash 후 PR
- Merge는 `--no-ff` 사용 (히스토리 보존)
- 빌드/설정 파일(pom.xml, build.gradle)과 코드 변경은 분리 커밋 권장
