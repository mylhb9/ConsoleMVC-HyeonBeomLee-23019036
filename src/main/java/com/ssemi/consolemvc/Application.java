package com.ssemi.consolemvc;

/**
 * ConsoleMVC 애플리케이션 진입점
 *
 * <p>Composition Root: 모든 의존성 조립은 이 클래스에서 수행한다.
 * 각 레이어의 구현체를 직접 생성하여 상위 레이어에 주입함으로써
 * View → Controller → Service → Repository → Model 의존성 방향을 유지한다.
 */
public class Application {

    public static void main(String[] args) {
        // TODO: Composition Root — 의존성 조립 예정
        // 예시 조립 순서:
        //   1. Repository 구현체 생성
        //   2. Service 생성 (Repository 주입)
        //   3. Controller 생성 (Service 주입)
        //   4. View 생성 (Controller 주입)
        //   5. 애플리케이션 루프 시작
    }
}
