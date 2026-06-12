package com.ssemi.consolemvc.exception;

/**
 * 존재하지 않는 시료를 조회하거나 조작하려 할 때 발생하는 예외
 *
 * <p>저장소에 해당 sampleId의 시료가 없을 때 던진다.
 */
public class SampleNotFoundException extends BusinessException {

    /**
     * 시료 미발견 예외를 생성한다.
     *
     * @param sampleId 조회 대상 시료 식별자
     */
    public SampleNotFoundException(String sampleId) {
        super("시료를 찾을 수 없습니다: " + sampleId);
    }
}
