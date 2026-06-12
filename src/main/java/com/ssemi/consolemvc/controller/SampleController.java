package com.ssemi.consolemvc.controller;

import com.ssemi.consolemvc.dto.SampleCreateRequest;
import com.ssemi.consolemvc.dto.SampleResponse;
import com.ssemi.consolemvc.exception.BusinessException;
import com.ssemi.consolemvc.service.SampleService;
import com.ssemi.consolemvc.view.SampleView;

import java.util.List;

public class SampleController {

    private final SampleService sampleService;
    private final SampleView sampleView;

    public SampleController(SampleService sampleService, SampleView sampleView) {
        this.sampleService = sampleService;
        this.sampleView = sampleView;
    }

    public void handleRegisterSample() {
        SampleCreateRequest request = sampleView.readSampleCreateRequest();
        try {
            SampleResponse response = sampleService.registerSample(request);
            sampleView.printSuccessMessage("시료 등록 완료: " + response.sampleId());
        } catch (BusinessException e) {
            sampleView.printErrorMessage(e.getMessage());
        }
    }

    public void handleListSamples() {
        List<SampleResponse> samples = sampleService.findAllSamples();
        sampleView.printSampleList(samples);
    }
}
