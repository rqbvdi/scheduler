package com.example.schedule.service;

import com.example.schedule.entity.ShiftPattern;
import com.example.schedule.repository.ShiftPatternRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShiftPatternService {

    private final ShiftPatternRepository repository;

    public ShiftPatternService(ShiftPatternRepository repository) {
        this.repository = repository;
    }

    public List<ShiftPattern> getAllPatterns() {
        return repository.findAll();
    }

    public ShiftPattern getPattern(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pattern not found"));
    }

    @Transactional
    public ShiftPattern createPattern(ShiftPattern pattern) {
        return repository.save(pattern);
    }

    @Transactional
    public ShiftPattern updatePattern(Long id, ShiftPattern pattern) {
        ShiftPattern existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pattern not found"));

        existing.setName(pattern.getName());
        existing.setShifts(pattern.getShifts());
        return repository.save(existing);
    }

    @Transactional
    public void deletePattern(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Pattern not found");
        repository.deleteById(id);
    }
}
