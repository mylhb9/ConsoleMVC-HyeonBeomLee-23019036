package com.ssemi.consolemvc.service;

import com.ssemi.consolemvc.dto.SampleCreateRequest;
import com.ssemi.consolemvc.dto.SampleResponse;
import com.ssemi.consolemvc.exception.DuplicateSampleIdException;
import com.ssemi.consolemvc.exception.SampleNotFoundException;
import com.ssemi.consolemvc.model.Sample;
import com.ssemi.consolemvc.repository.SampleRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SampleService {

    private final SampleRepository sampleRepository;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public SampleResponse registerSample(SampleCreateRequest request) {
        if (sampleRepository.existsById(request.sampleId())) {
            throw new DuplicateSampleIdException(request.sampleId());
        }
        Sample sample = new Sample(
                request.sampleId(),
                request.name(),
                request.avgProdTime(),
                request.yield(),
                request.stock()
        );
        sampleRepository.save(sample);
        return SampleResponse.fromSample(sample);
    }

    public List<SampleResponse> findAllSamples() {
        return sampleRepository.findAll().stream()
                .map(SampleResponse::fromSample)
                .collect(Collectors.toList());
    }

    public SampleResponse findSampleById(String sampleId) {
        return sampleRepository.findById(sampleId)
                .map(SampleResponse::fromSample)
                .orElseThrow(() -> new SampleNotFoundException(sampleId));
    }

    public List<SampleResponse> searchByName(String keyword) {
        return sampleRepository.findByName(keyword).stream()
                .map(SampleResponse::fromSample)
                .collect(Collectors.toList());
    }
}
