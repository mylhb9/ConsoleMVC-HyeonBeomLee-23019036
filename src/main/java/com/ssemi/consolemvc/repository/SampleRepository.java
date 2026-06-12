package com.ssemi.consolemvc.repository;

import com.ssemi.consolemvc.model.Sample;

import java.util.List;
import java.util.Optional;

/**
 * 시료 저장소 인터페이스
 *
 * <p>저장 매체(InMemory, 파일, DB 등)와 무관하게 일관된 접근 방식을 제공한다.
 * 구현체 교체 시 이 인터페이스를 그대로 유지하여 Service 레이어 변경을 최소화한다.
 */
public interface SampleRepository {

    /**
     * 시료를 저장한다.
     *
     * @param sample 저장할 시료 객체
     */
    void save(Sample sample);

    /**
     * 전체 시료 목록을 반환한다.
     *
     * @return 저장된 모든 시료 리스트 (없으면 빈 리스트)
     */
    List<Sample> findAll();

    /**
     * ID로 시료를 조회한다.
     *
     * @param sampleId 조회할 시료 식별자
     * @return 시료가 존재하면 Optional로 감싼 값, 없으면 Optional.empty()
     */
    Optional<Sample> findById(String sampleId);

    /**
     * 시료명에 키워드가 포함된 시료 목록을 반환한다.
     *
     * @param keyword 검색 키워드
     * @return 조건에 일치하는 시료 리스트 (없으면 빈 리스트)
     */
    List<Sample> findByName(String keyword);

    /**
     * 주어진 ID의 시료가 저장소에 존재하는지 확인한다.
     *
     * @param sampleId 확인할 시료 식별자
     * @return 존재하면 true, 없으면 false
     */
    boolean existsById(String sampleId);
}
