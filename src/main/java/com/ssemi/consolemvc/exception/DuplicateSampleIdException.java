package com.ssemi.consolemvc.exception;

/**
 * 중복된 시료 ID로 등록을 시도할 때 발생하는 예외
 *
 * <p>이미 저장소에 존재하는 sampleId로 새 시료를 등록하려 할 때 던진다.
 */
public class DuplicateSampleIdException extends BusinessException {

    /**
     * 중복 시료 ID 예외를 생성한다.
     *
     * @param sampleId 중복된 시료 식별자
     */
    public DuplicateSampleIdException(String sampleId) {
        super("이미 존재하는 시료 ID입니다: " + sampleId);
    }
}
