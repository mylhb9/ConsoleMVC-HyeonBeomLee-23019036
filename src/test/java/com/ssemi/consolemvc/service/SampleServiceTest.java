package com.ssemi.consolemvc.service;

import com.ssemi.consolemvc.dto.SampleCreateRequest;
import com.ssemi.consolemvc.dto.SampleResponse;
import com.ssemi.consolemvc.exception.DuplicateSampleIdException;
import com.ssemi.consolemvc.exception.SampleNotFoundException;
import com.ssemi.consolemvc.model.Sample;
import com.ssemi.consolemvc.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@DisplayName("시료 서비스 테스트")
class SampleServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private SampleService sampleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("시료 등록")
    class RegisterSample {

        @Test
        @DisplayName("정상 요청 시 시료가 저장된다")
        void registerSample_validRequest_savesSample() {
            // given
            SampleCreateRequest request = givenSampleCreateRequest("S-001", "실리콘웨이퍼-8인치");
            given(sampleRepository.existsById("S-001")).willReturn(false);

            // when
            SampleResponse response = sampleService.registerSample(request);

            // then
            assertThat(response.sampleId()).isEqualTo("S-001");
            assertThat(response.name()).isEqualTo("실리콘웨이퍼-8인치");
            then(sampleRepository).should().save(any(Sample.class));
        }

        @Test
        @DisplayName("중복 ID 등록 시 DuplicateSampleIdException이 발생한다")
        void registerSample_duplicateId_throwsDuplicateSampleIdException() {
            // given
            SampleCreateRequest request = givenSampleCreateRequest("S-001", "실리콘웨이퍼-8인치");
            given(sampleRepository.existsById("S-001")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> sampleService.registerSample(request))
                    .isInstanceOf(DuplicateSampleIdException.class)
                    .hasMessageContaining("S-001");
            then(sampleRepository).should(never()).save(any(Sample.class));
        }
    }

    @Nested
    @DisplayName("전체 시료 조회")
    class FindAllSamples {

        @Test
        @DisplayName("등록된 시료가 없으면 빈 리스트를 반환한다")
        void findAllSamples_noSamples_returnsEmptyList() {
            // given
            given(sampleRepository.findAll()).willReturn(List.of());

            // when
            List<SampleResponse> result = sampleService.findAllSamples();

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("등록된 시료가 있으면 전체 목록을 반환한다")
        void findAllSamples_hasSamples_returnsSampleList() {
            // given
            List<Sample> samples = List.of(
                    givenSample("S-001", "실리콘웨이퍼-8인치"),
                    givenSample("S-002", "실리콘웨이퍼-12인치")
            );
            given(sampleRepository.findAll()).willReturn(samples);

            // when
            List<SampleResponse> result = sampleService.findAllSamples();

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).sampleId()).isEqualTo("S-001");
            assertThat(result.get(1).sampleId()).isEqualTo("S-002");
        }
    }

    @Nested
    @DisplayName("ID로 단건 조회")
    class FindSampleById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 시료를 반환한다")
        void findSampleById_existingId_returnsSample() {
            // given
            given(sampleRepository.findById("S-001"))
                    .willReturn(Optional.of(givenSample("S-001", "실리콘웨이퍼-8인치")));

            // when
            SampleResponse response = sampleService.findSampleById("S-001");

            // then
            assertThat(response.sampleId()).isEqualTo("S-001");
            assertThat(response.name()).isEqualTo("실리콘웨이퍼-8인치");
        }

        @Test
        @DisplayName("없는 ID로 조회하면 SampleNotFoundException이 발생한다")
        void findSampleById_nonExistingId_throwsSampleNotFoundException() {
            // given
            given(sampleRepository.findById("S-999")).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sampleService.findSampleById("S-999"))
                    .isInstanceOf(SampleNotFoundException.class)
                    .hasMessageContaining("S-999");
        }
    }

    @Nested
    @DisplayName("이름으로 시료 검색")
    class SearchByName {

        @Test
        @DisplayName("이름 키워드로 검색하면 포함 시료를 반환한다")
        void searchByName_keywordMatch_returnsMatchingSamples() {
            // given
            List<Sample> matched = List.of(
                    givenSample("S-001", "실리콘웨이퍼-8인치"),
                    givenSample("S-002", "실리콘웨이퍼-12인치")
            );
            given(sampleRepository.findByName("실리콘")).willReturn(matched);

            // when
            List<SampleResponse> result = sampleService.searchByName("실리콘");

            // then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(SampleResponse::name)
                    .allMatch(name -> name.contains("실리콘"));
        }

        @Test
        @DisplayName("키워드와 일치하는 시료가 없으면 빈 리스트를 반환한다")
        void searchByName_noMatch_returnsEmptyList() {
            // given
            given(sampleRepository.findByName("없는키워드")).willReturn(List.of());

            // when
            List<SampleResponse> result = sampleService.searchByName("없는키워드");

            // then
            assertThat(result).isEmpty();
        }
    }

    private SampleCreateRequest givenSampleCreateRequest(String sampleId, String name) {
        return new SampleCreateRequest(sampleId, name, 10.0, 0.9, 100);
    }

    private Sample givenSample(String sampleId, String name) {
        return new Sample(sampleId, name, 10.0, 0.9, 100);
    }
}
