package com.ssemi.consolemvc.integration;

import com.ssemi.consolemvc.controller.SampleController;
import com.ssemi.consolemvc.dto.SampleResponse;
import com.ssemi.consolemvc.repository.InMemorySampleRepository;
import com.ssemi.consolemvc.repository.SampleRepository;
import com.ssemi.consolemvc.service.SampleService;
import com.ssemi.consolemvc.view.MainView;
import com.ssemi.consolemvc.view.SampleView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("시료 관리 시스템 통합 테스트")
class SampleFlowIntegrationTest {

    private ByteArrayOutputStream outputCapture;
    private PrintStream originalOut;

    @BeforeEach
    void setUpOutputStream() {
        outputCapture = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputCapture));
    }

    @AfterEach
    void restoreOutputStream() {
        System.setOut(originalOut);
    }

    private MainView buildSystem(String simulatedInput) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        SampleRepository repository = new InMemorySampleRepository();
        SampleService service = new SampleService(repository);
        SampleView sampleView = new SampleView(scanner);
        SampleController controller = new SampleController(service, sampleView);
        return new MainView(scanner, controller);
    }

    private SampleView buildSampleView(String simulatedInput) {
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedInput.getBytes()));
        return new SampleView(scanner);
    }

    @Nested
    @DisplayName("전체 흐름 통합 테스트")
    class FullFlow {

        @Test
        @DisplayName("시료 등록 후 목록 조회 전체 흐름이 정상 동작한다")
        void registerSample_thenListSamples_fullFlowWorks() {
            // given: 메인→시료관리→등록→목록조회→뒤로→종료 시나리오
            String input = String.join("\n",
                    "1",                // 메인 메뉴: [1] 시료 관리
                    "1",                // 시료 메뉴: [1] 시료 등록
                    "S-001",            // 시료 ID
                    "실리콘웨이퍼-8인치",   // 시료명
                    "10.0",             // 평균 생산시간
                    "0.9",              // 수율
                    "100",              // 재고
                    "2",                // 시료 메뉴: [2] 목록 조회
                    "0",                // 뒤로
                    "0"                 // 종료
            );

            // when
            buildSystem(input).run();

            // then
            String output = outputCapture.toString();
            assertThat(output).contains("[완료]");
            assertThat(output).contains("S-001");
            assertThat(output).contains("실리콘웨이퍼-8인치");
            assertThat(output).contains("90%");
            assertThat(output).contains("시스템을 종료합니다.");
        }

        @Test
        @DisplayName("시료가 없을 때 목록 조회 시 안내 메시지를 출력한다")
        void listSamples_whenEmpty_printsEmptyMessage() {
            // given: 시료 없이 목록 조회
            String input = String.join("\n", "1", "2", "0", "0");

            // when
            buildSystem(input).run();

            // then
            assertThat(outputCapture.toString()).contains("등록된 시료가 없습니다.");
        }

        @Test
        @DisplayName("중복 ID 등록 시 오류 메시지를 출력하고 첫 번째 시료는 유지된다")
        void registerSample_duplicateId_printsErrorAndKeepsOriginal() {
            // given: 동일 ID로 두 번 등록 시도
            String input = String.join("\n",
                    "1",
                    "1", "S-001", "실리콘웨이퍼-8인치", "10.0", "0.9", "100",
                    "1", "S-001", "중복시료", "5.0", "0.8", "50",
                    "2",
                    "0", "0"
            );

            // when
            buildSystem(input).run();

            // then
            String output = outputCapture.toString();
            assertThat(output).contains("[오류]");
            assertThat(output).contains("실리콘웨이퍼-8인치");
            assertThat(output).doesNotContain("중복시료");
        }

        @Test
        @DisplayName("잘못된 메인 메뉴 선택 시 오류 메시지를 출력한다")
        void mainMenu_invalidInput_printsErrorMessage() {
            // given: 잘못된 선택 후 종료
            String input = String.join("\n", "9", "0");

            // when
            buildSystem(input).run();

            // then
            assertThat(outputCapture.toString()).contains("[오류] 올바른 메뉴를 선택하세요.");
        }

        @Test
        @DisplayName("잘못된 시료 메뉴 선택 시 오류 메시지를 출력한다")
        void sampleMenu_invalidInput_printsErrorMessage() {
            // given: 시료 관리 진입 후 잘못된 선택, 뒤로, 종료
            String input = String.join("\n", "1", "9", "0", "0");

            // when
            buildSystem(input).run();

            // then
            assertThat(outputCapture.toString()).contains("[오류] 올바른 메뉴를 선택하세요.");
        }
    }

    @Nested
    @DisplayName("SampleView I/O 단위 테스트")
    class SampleViewIO {

        @Test
        @DisplayName("readSampleCreateRequest가 입력을 올바르게 파싱한다")
        void readSampleCreateRequest_validInput_parsesCorrectly() {
            // given
            String input = String.join("\n", "S-002", "게르마늄웨이퍼", "15.5", "0.85", "50");
            SampleView sampleView = buildSampleView(input);

            // when
            var request = sampleView.readSampleCreateRequest();

            // then
            assertThat(request.sampleId()).isEqualTo("S-002");
            assertThat(request.name()).isEqualTo("게르마늄웨이퍼");
            assertThat(request.avgProdTime()).isEqualTo(15.5);
            assertThat(request.yield()).isEqualTo(0.85);
            assertThat(request.stock()).isEqualTo(50);
        }

        @Test
        @DisplayName("printSampleList가 빈 목록일 때 안내 메시지를 출력한다")
        void printSampleList_emptyList_printsEmptyMessage() {
            // given
            SampleView sampleView = buildSampleView("");

            // when
            sampleView.printSampleList(List.of());

            // then
            assertThat(outputCapture.toString()).contains("등록된 시료가 없습니다.");
        }

        @Test
        @DisplayName("printSampleList가 시료 정보를 테이블 형식으로 출력한다")
        void printSampleList_hasSamples_printsTableWithCorrectData() {
            // given
            SampleView sampleView = buildSampleView("");
            List<SampleResponse> samples = List.of(
                    new SampleResponse("S-001", "실리콘웨이퍼-8인치", 10.0, 0.9, 100)
            );

            // when
            sampleView.printSampleList(samples);

            // then
            String output = outputCapture.toString();
            assertThat(output).contains("S-001");
            assertThat(output).contains("실리콘웨이퍼-8인치");
            assertThat(output).contains("90%");
            assertThat(output).contains("100");
        }

        @Test
        @DisplayName("printSuccessMessage가 [완료] 접두어와 함께 메시지를 출력한다")
        void printSuccessMessage_printsWithPrefix() {
            // given
            SampleView sampleView = buildSampleView("");

            // when
            sampleView.printSuccessMessage("시료가 등록되었습니다.");

            // then
            assertThat(outputCapture.toString()).contains("[완료] 시료가 등록되었습니다.");
        }

        @Test
        @DisplayName("printErrorMessage가 [오류] 접두어와 함께 메시지를 출력한다")
        void printErrorMessage_printsWithPrefix() {
            // given
            SampleView sampleView = buildSampleView("");

            // when
            sampleView.printErrorMessage("중복된 시료 ID입니다.");

            // then
            assertThat(outputCapture.toString()).contains("[오류] 중복된 시료 ID입니다.");
        }
    }
}
