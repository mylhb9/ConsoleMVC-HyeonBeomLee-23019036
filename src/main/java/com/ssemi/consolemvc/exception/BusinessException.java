package com.ssemi.consolemvc.exception;

/**
 * 비즈니스 규칙 위반 시 발생하는 최상위 예외 클래스
 *
 * <p>도메인 로직에서 발생하는 모든 비즈니스 예외의 부모 클래스.
 * 구체적인 예외 상황에 따라 하위 클래스를 정의하여 사용한다.
 */
public class BusinessException extends RuntimeException {

    /**
     * 비즈니스 예외를 생성한다.
     *
     * @param message 사용자에게 전달할 예외 메시지
     */
    public BusinessException(String message) {
        super(message);
    }
}
