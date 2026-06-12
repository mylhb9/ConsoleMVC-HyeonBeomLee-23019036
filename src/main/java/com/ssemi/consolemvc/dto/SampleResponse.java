package com.ssemi.consolemvc.dto;

import com.ssemi.consolemvc.model.Sample;

/**
 * 시료 응답 DTO
 *
 * <p>Service 레이어에서 반환하는 데이터 객체로, View 레이어가 Model을
 * 직접 참조하지 않도록 중간 계층 역할을 수행한다.
 * {@link #fromSample(Sample)} 팩터리 메서드로 도메인 모델에서 변환한다.
 */
public record SampleResponse(
        /** 시료 고유 식별자 */
        String sampleId,
        /** 시료명 */
        String name,
        /** 평균 생산시간 (분/ea) */
        double avgProdTime,
        /** 수율 (0.0 ~ 1.0) */
        double yield,
        /** 재고 수량 (ea) */
        int stock
) {
    /**
     * Sample 도메인 객체를 SampleResponse로 변환한다.
     *
     * @param sample 변환 대상 도메인 객체
     * @return 변환된 응답 DTO
     */
    public static SampleResponse fromSample(Sample sample) {
        return new SampleResponse(
                sample.getSampleId(),
                sample.getName(),
                sample.getAvgProdTime(),
                sample.getYield(),
                sample.getStock()
        );
    }
}
