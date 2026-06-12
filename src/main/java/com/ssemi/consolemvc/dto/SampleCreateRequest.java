package com.ssemi.consolemvc.dto;

/**
 * 시료 등록 요청 DTO
 *
 * <p>View 레이어에서 수집한 입력값을 Controller를 거쳐 Service로 전달하는 데이터 객체.
 * record 타입으로 불변성을 보장한다.
 */
public record SampleCreateRequest(
        /** 시료 고유 식별자 (예: "S-001") */
        String sampleId,
        /** 시료명 */
        String name,
        /** 평균 생산시간 (분/ea) */
        double avgProdTime,
        /** 수율 (0.0 ~ 1.0) */
        double yield,
        /** 재고 수량 (ea) */
        int stock
) {}
