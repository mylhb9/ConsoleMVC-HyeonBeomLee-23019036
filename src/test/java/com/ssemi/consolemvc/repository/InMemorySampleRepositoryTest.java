package com.ssemi.consolemvc.repository;

import com.ssemi.consolemvc.model.Sample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인메모리 시료 저장소 테스트")
class InMemorySampleRepositoryTest {

    private InMemorySampleRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySampleRepository();
    }

    @Nested
    @DisplayName("시료 저장")
    class Save {

        @Test
        @DisplayName("저장 후 findById로 조회 가능하다")
        void save_validSample_canBeFoundById() {
            // given
            Sample sample = givenSample("S-001", "실리콘웨이퍼-8인치");

            // when
            repository.save(sample);

            // then
            Optional<Sample> found = repository.findById("S-001");
            assertThat(found).isPresent();
            assertThat(found.get().getSampleId()).isEqualTo("S-001");
        }

        @Test
        @DisplayName("Repository는 중복 ID 저장 시 기존 항목을 덮어쓴다 (중복 방지는 Service 책임)")
        void save_duplicateId_overwritesExistingEntry() {
            // given
            Sample original = givenSample("S-001", "원본");
            Sample updated = givenSample("S-001", "갱신본");
            repository.save(original);

            // when
            repository.save(updated);

            // then
            Optional<Sample> found = repository.findById("S-001");
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("갱신본");
        }
    }

    @Nested
    @DisplayName("전체 조회")
    class FindAll {

        @Test
        @DisplayName("저장소가 비어있으면 빈 리스트를 반환한다")
        void findAll_emptyRepository_returnsEmptyList() {
            // given (빈 저장소)

            // when
            List<Sample> result = repository.findAll();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("여러 건 저장 시 등록 순서를 보장한다")
        void findAll_multipleItems_returnsInInsertionOrder() {
            // given
            repository.save(givenSample("S-001", "첫번째"));
            repository.save(givenSample("S-002", "두번째"));
            repository.save(givenSample("S-003", "세번째"));

            // when
            List<Sample> result = repository.findAll();

            // then
            assertThat(result).hasSize(3);
            assertThat(result.get(0).getSampleId()).isEqualTo("S-001");
            assertThat(result.get(1).getSampleId()).isEqualTo("S-002");
            assertThat(result.get(2).getSampleId()).isEqualTo("S-003");
        }
    }

    @Nested
    @DisplayName("ID로 단건 조회")
    class FindById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 Optional에 담아 반환한다")
        void findById_existingId_returnsNonEmptyOptional() {
            // given
            repository.save(givenSample("S-001", "실리콘웨이퍼-8인치"));

            // when
            Optional<Sample> result = repository.findById("S-001");

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getSampleId()).isEqualTo("S-001");
        }

        @Test
        @DisplayName("없는 ID로 조회하면 빈 Optional을 반환한다")
        void findById_nonExistingId_returnsEmptyOptional() {
            // given (저장된 시료 없음)

            // when
            Optional<Sample> result = repository.findById("S-999");

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("이름 키워드 검색")
    class FindByName {

        @Test
        @DisplayName("키워드를 이름에 포함한 시료를 반환한다")
        void findByName_keywordMatch_returnsSamples() {
            // given
            repository.save(givenSample("S-001", "실리콘웨이퍼-8인치"));
            repository.save(givenSample("S-002", "실리콘웨이퍼-12인치"));
            repository.save(givenSample("S-003", "갈륨비소웨이퍼"));

            // when
            List<Sample> result = repository.findByName("실리콘");

            // then
            assertThat(result).hasSize(2);
            assertThat(result).extracting(Sample::getSampleId)
                    .containsExactlyInAnyOrder("S-001", "S-002");
        }

        @Test
        @DisplayName("키워드와 일치하는 시료가 없으면 빈 리스트를 반환한다")
        void findByName_noMatch_returnsEmptyList() {
            // given
            repository.save(givenSample("S-001", "실리콘웨이퍼-8인치"));

            // when
            List<Sample> result = repository.findByName("존재하지않는키워드");

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("ID 존재 여부 확인")
    class ExistsById {

        @Test
        @DisplayName("존재하는 ID는 true를 반환한다")
        void existsById_existingId_returnsTrue() {
            // given
            repository.save(givenSample("S-001", "실리콘웨이퍼-8인치"));

            // when
            boolean result = repository.existsById("S-001");

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("없는 ID는 false를 반환한다")
        void existsById_nonExistingId_returnsFalse() {
            // given (저장된 시료 없음)

            // when
            boolean result = repository.existsById("S-999");

            // then
            assertThat(result).isFalse();
        }
    }

    private Sample givenSample(String sampleId, String name) {
        return new Sample(sampleId, name, 10.0, 0.9, 100);
    }
}
