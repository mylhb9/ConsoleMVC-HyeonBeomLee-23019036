package com.ssemi.consolemvc.model;

import java.util.Objects;

/**
 * 시료(Sample) 도메인 모델
 *
 * <p>불변 객체로 설계되어 한번 생성된 이후 상태가 변경되지 않는다.
 * 동일성 비교는 sampleId 기준으로 수행한다.
 */
public class Sample {

    /** 시료 고유 식별자 (예: "S-001") */
    private final String sampleId;

    /** 시료명 (예: "실리콘 웨이퍼-8인치") */
    private final String name;

    /** 평균 생산시간 (단위: 분/ea) */
    private final double avgProdTime;

    /** 수율 (0.0 ~ 1.0 범위) */
    private final double yield;

    /** 현재 재고 수량 (단위: ea) */
    private final int stock;

    public Sample(String sampleId, String name, double avgProdTime, double yield, int stock) {
        this.sampleId = sampleId;
        this.name = name;
        this.avgProdTime = avgProdTime;
        this.yield = yield;
        this.stock = stock;
    }

    public String getSampleId() { return sampleId; }
    public String getName() { return name; }
    public double getAvgProdTime() { return avgProdTime; }
    public double getYield() { return yield; }
    public int getStock() { return stock; }

    /** sampleId 기준 동일성 비교 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sample)) return false;
        Sample sample = (Sample) o;
        return Objects.equals(sampleId, sample.sampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId);
    }
}
