package com.ssemi.consolemvc.repository;

import com.ssemi.consolemvc.model.Sample;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemorySampleRepository implements SampleRepository {

    private final Map<String, Sample> store = new LinkedHashMap<>();

    @Override
    public void save(Sample sample) {
        store.put(sample.getSampleId(), sample);
    }

    @Override
    public List<Sample> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Sample> findById(String sampleId) {
        return Optional.ofNullable(store.get(sampleId));
    }

    @Override
    public List<Sample> findByName(String keyword) {
        return store.values().stream()
                .filter(s -> s.getName().contains(keyword))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String sampleId) {
        return store.containsKey(sampleId);
    }
}
