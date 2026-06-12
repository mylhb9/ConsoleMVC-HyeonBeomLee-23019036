package com.ssemi.consolemvc.view;

import com.ssemi.consolemvc.controller.SampleController;

import java.util.Scanner;

public class MainView {

    private final Scanner scanner;
    private final SampleController sampleController;

    public MainView(Scanner scanner, SampleController sampleController) {
        this.scanner = scanner;
        this.sampleController = sampleController;
    }

    public void run() {
        while (true) {
            printMainMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> handleSampleMenu();
                case "0" -> {
                    System.out.println("시스템을 종료합니다.");
                    return;
                }
                default -> System.out.println("[오류] 올바른 메뉴를 선택하세요.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== 시료 주문 관리 시스템 ===");
        System.out.println("[1] 시료 관리");
        System.out.println("[0] 종료");
        System.out.print("선택: ");
    }

    private void handleSampleMenu() {
        while (true) {
            printSampleMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> sampleController.handleRegisterSample();
                case "2" -> sampleController.handleListSamples();
                case "0" -> { return; }
                default -> System.out.println("[오류] 올바른 메뉴를 선택하세요.");
            }
        }
    }

    private void printSampleMenu() {
        System.out.println("\n--- 시료 관리 ---");
        System.out.println("[1] 시료 등록");
        System.out.println("[2] 시료 목록 조회");
        System.out.println("[0] 뒤로");
        System.out.print("선택: ");
    }
}
