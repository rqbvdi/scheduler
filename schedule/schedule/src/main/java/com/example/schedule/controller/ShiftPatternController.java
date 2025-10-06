package com.example.schedule.controller;

import com.example.schedule.entity.ShiftPattern;
import com.example.schedule.service.ShiftPatternService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shift-patterns")
public class ShiftPatternController {

    private final ShiftPatternService service;

    public ShiftPatternController(ShiftPatternService service) {
        this.service = service;
    }

    // Get all patterns
    @GetMapping
    public ResponseEntity<List<ShiftPattern>> getAllPatterns() {
        List<ShiftPattern> patterns = service.getAllPatterns();
        return ResponseEntity.ok(patterns);
    }

    // Get a pattern by id
    @GetMapping("/{id}")
    public ResponseEntity<ShiftPattern> getPattern(@PathVariable Long id) {
        try {
            ShiftPattern pattern = service.getPattern(id);
            return ResponseEntity.ok(pattern);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new pattern
    @PostMapping
    public ResponseEntity<ShiftPattern> createPattern(@RequestBody ShiftPattern pattern) {
        ShiftPattern created = service.createPattern(pattern);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Update a pattern
    @PutMapping("/{id}")
    public ResponseEntity<ShiftPattern> updatePattern(@PathVariable Long id, @RequestBody ShiftPattern pattern) {
        try {
            ShiftPattern updated = service.updatePattern(id, pattern);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a pattern
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePattern(@PathVariable Long id) {
        try {
            service.deletePattern(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
