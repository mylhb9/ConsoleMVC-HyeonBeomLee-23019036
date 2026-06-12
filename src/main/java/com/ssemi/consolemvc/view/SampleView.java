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
        // TODO: UI-01에서 구현
        throw new UnsupportedOperationException("미구현");
    }

    public void printSampleList(List<SampleResponse> samples) {
        // TODO: UI-01에서 구현
        throw new UnsupportedOperationException("미구현");
    }

    public void printSuccessMessage(String message) {
        // TODO: UI-01에서 구현
        throw new UnsupportedOperationException("미구현");
    }

    public void printErrorMessage(String message) {
        // TODO: UI-01에서 구현
        throw new UnsupportedOperationException("미구현");
    }
}
