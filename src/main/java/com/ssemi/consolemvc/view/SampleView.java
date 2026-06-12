package com.ssemi.consolemvc.view;

import com.ssemi.consolemvc.dto.SampleCreateRequest;
import com.ssemi.consolemvc.dto.SampleResponse;

import java.util.List;
import java.util.Scanner;

public class SampleView {

    private final Scanner scanner;

    public SampleView(Scanner scanner) {
        this.scanner = scanner;
    }

    public SampleCreateRequest readSampleCreateRequest() {
        System.out.print("시료 ID: ");
        String sampleId = scanner.nextLine().trim();
        System.out.print("시료명: ");
        String name = scanner.nextLine().trim();
        System.out.print("평균 생산시간(분/ea): ");
        double avgProdTime = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("수율(0.0~1.0): ");
        double yield = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("재고 수량(ea): ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        return new SampleCreateRequest(sampleId, name, avgProdTime, yield, stock);
    }

    public void printSampleList(List<SampleResponse> samples) {
        if (samples.isEmpty()) {
            System.out.println("등록된 시료가 없습니다.");
            return;
        }
        System.out.println("┌────────┬──────────────────────┬──────────┬──────┬──────┐");
        System.out.println("│ ID     │ 시료명               │ 생산시간 │ 수율 │ 재고 │");
        System.out.println("├────────┼──────────────────────┼──────────┼──────┼──────┤");
        for (SampleResponse s : samples) {
            System.out.printf("│ %-6s │ %-20s │ %6.1f분 │ %4.0f%% │ %4d │%n",
                    s.sampleId(), s.name(), s.avgProdTime(), s.yield() * 100, s.stock());
        }
        System.out.println("└────────┴──────────────────────┴──────────┴──────┴──────┘");
    }

    public void printSuccessMessage(String message) {
        System.out.println("[완료] " + message);
    }

    public void printErrorMessage(String message) {
        System.out.println("[오류] " + message);
    }
}
