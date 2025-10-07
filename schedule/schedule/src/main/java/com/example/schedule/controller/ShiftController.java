package com.example.schedule.controller;

import com.example.schedule.dto.ShiftDto;
import com.example.schedule.entity.Employee;
import com.example.schedule.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    // --- GET ALL SHIFTS ---
    @GetMapping
    public ResponseEntity<List<ShiftDto>> getAll(@AuthenticationPrincipal Employee user) {
        try {
            return ResponseEntity.ok(shiftService.getAll(user));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- GET SHIFT BY ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDto> getById(@AuthenticationPrincipal Employee user,
                                            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(shiftService.getById(id, user));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- GET SHIFTS FOR EMPLOYEE ---
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ShiftDto>> getByEmployee(@AuthenticationPrincipal Employee user,
                                                        @PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(shiftService.getByEmployee(employeeId, user));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- GET SHIFTS IN DATE RANGE ---
    @GetMapping("/range")
    public ResponseEntity<List<ShiftDto>> getInDateRange(@AuthenticationPrincipal Employee user,
                                                         @RequestParam("start") String start,
                                                         @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        try {
            return ResponseEntity.ok(shiftService.getInDateRange(startDate, endDate, user));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- CREATE SHIFT ---
    @PostMapping
    public ResponseEntity<ShiftDto> create(@AuthenticationPrincipal Employee user,
                                           @RequestBody ShiftDto dto) {
        try {
            ShiftDto created = shiftService.create(dto, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- UPDATE SHIFT ---
    @PutMapping("/{id}")
    public ResponseEntity<ShiftDto> update(@AuthenticationPrincipal Employee user,
                                           @PathVariable Long id,
                                           @RequestBody ShiftDto dto) {
        try {
            return ResponseEntity.ok(shiftService.update(id, dto, user));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- DELETE SHIFT ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Employee user,
                                       @PathVariable Long id) {
        try {
            shiftService.delete(id, user);
            return ResponseEntity.noContent().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
