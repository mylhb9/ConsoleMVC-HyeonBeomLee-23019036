package com.ssemi.consolemvc.controller;

import com.ssemi.consolemvc.dto.SampleCreateRequest;
import com.ssemi.consolemvc.dto.SampleResponse;
import com.ssemi.consolemvc.exception.DuplicateSampleIdException;
import com.ssemi.consolemvc.service.SampleService;
import com.ssemi.consolemvc.view.SampleView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

@DisplayName("시료 컨트롤러 테스트")
class SampleControllerTest {

    @Mock
    private SampleService sampleService;

    @Mock
    private SampleView sampleView;

    @InjectMocks
    private SampleController sampleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("시료 등록 처리")
    class HandleRegisterSample {

        @Test
        @DisplayName("정상 입력 시 서비스의 등록 메서드를 호출한다")
        void handleRegisterSample_validInput_callsServiceRegister() {
            // given
            SampleCreateRequest request = givenSampleCreateRequest("S-001", "실리콘웨이퍼-8인치");
            SampleResponse response = givenSampleResponse("S-001", "실리콘웨이퍼-8인치");
            given(sampleView.readSampleCreateRequest()).willReturn(request);
            given(sampleService.registerSample(request)).willReturn(response);

            // when
            sampleController.handleRegisterSample();

            // then
            then(sampleService).should(times(1)).registerSample(any(SampleCreateRequest.class));
            then(sampleView).should(times(1)).printSuccessMessage(any(String.class));
        }

        @Test
        @DisplayName("서비스 예외 발생 시 뷰의 에러 메시지 출력을 호출한다")
        void handleRegisterSample_serviceThrows_callsViewError() {
            // given
            SampleCreateRequest request = givenSampleCreateRequest("S-001", "실리콘웨이퍼-8인치");
            given(sampleView.readSampleCreateRequest()).willReturn(request);
            willThrow(new DuplicateSampleIdException("S-001"))
                    .given(sampleService).registerSample(any(SampleCreateRequest.class));

            // when
            sampleController.handleRegisterSample();

            // then
            then(sampleView).should(times(1)).printErrorMessage(any(String.class));
        }
    }

    @Nested
    @DisplayName("시료 목록 조회 처리")
    class HandleListSamples {

        @Test
        @DisplayName("시료 목록 조회 시 서비스 조회 후 뷰 출력을 호출한다")
        void handleListSamples_afterServiceQuery_callsViewPrint() {
            // given
            List<SampleResponse> samples = List.of(givenSampleResponse("S-001", "실리콘웨이퍼-8인치"));
            given(sampleService.findAllSamples()).willReturn(samples);

            // when
            sampleController.handleListSamples();

            // then
            then(sampleService).should(times(1)).findAllSamples();
            then(sampleView).should(times(1)).printSampleList(anyList());
        }
    }

    private SampleCreateRequest givenSampleCreateRequest(String sampleId, String name) {
        return new SampleCreateRequest(sampleId, name, 10.0, 0.9, 100);
    }

    private SampleResponse givenSampleResponse(String sampleId, String name) {
        return new SampleResponse(sampleId, name, 10.0, 0.9, 100);
    }
}
