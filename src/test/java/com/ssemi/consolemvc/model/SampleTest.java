package com.ssemi.consolemvc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("시료 도메인 모델 테스트")
class SampleTest {

    @Nested
    @DisplayName("동등성 비교")
    class Equals {

        @Test
        @DisplayName("동일한 sampleId를 가진 두 Sample은 같다")
        void equals_sameSampleId_returnsTrue() {
            // given
            Sample sample1 = givenSample("S-001");
            Sample sample2 = givenSample("S-001");

            // when
            boolean result = sample1.equals(sample2);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("다른 sampleId를 가진 두 Sample은 다르다")
        void equals_differentSampleId_returnsFalse() {
            // given
            Sample sample1 = givenSample("S-001");
            Sample sample2 = givenSample("S-002");

            // when
            boolean result = sample1.equals(sample2);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("동일한 참조는 자기 자신과 같다")
        void equals_sameReference_returnsTrue() {
            // given
            Sample sample = givenSample("S-001");

            // when & then
            assertThat(sample.equals(sample)).isTrue();
        }

        @Test
        @DisplayName("null과 비교하면 다르다")
        void equals_null_returnsFalse() {
            // given
            Sample sample = givenSample("S-001");

            // when & then
            assertThat(sample.equals(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("해시코드 일관성")
    class HashCode {

        @Test
        @DisplayName("동일한 sampleId의 hashCode는 같다")
        void hashCode_sameSampleId_isConsistent() {
            // given
            Sample sample1 = givenSample("S-001");
            Sample sample2 = givenSample("S-001");

            // when & then
            assertThat(sample1.hashCode()).isEqualTo(sample2.hashCode());
        }
    }

    private Sample givenSample(String sampleId) {
        return new Sample(sampleId, "실리콘웨이퍼-8인치", 10.0, 0.9, 100);
    }
}
