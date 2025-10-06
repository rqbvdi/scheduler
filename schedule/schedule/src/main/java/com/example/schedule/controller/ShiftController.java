package com.example.schedule.controller;

import com.example.schedule.dto.ShiftDto;
import com.example.schedule.entity.Employee;
import com.example.schedule.service.ShiftService;
import com.example.schedule.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;
    private final EmployeeRepository employeeRepository;

    // Helper to get the authenticated employee (stub, replace with actual auth)
    private Employee getRequester() {
        // TODO: Replace with actual authentication logic (e.g., Spring Security principal)
        return employeeRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Requester not found"));
    }

    // --- GET ALL SHIFTS (ADMIN only) ---
    @GetMapping
    public ResponseEntity<List<ShiftDto>> getAll() {
        try {
            return ResponseEntity.ok(shiftService.getAll(getRequester()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- GET SHIFT BY ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shiftService.getById(id, getRequester()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- GET SHIFTS FOR EMPLOYEE ---
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ShiftDto>> getByEmployee(@PathVariable Long employeeId) {
        try {
            return ResponseEntity.ok(shiftService.getByEmployee(employeeId, getRequester()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- GET SHIFTS IN DATE RANGE ---
    @GetMapping("/range")
    public ResponseEntity<List<ShiftDto>> getInDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end
    ) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        try {
            return ResponseEntity.ok(shiftService.getInDateRange(startDate, endDate, getRequester()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- CREATE SHIFT ---
    @PostMapping
    public ResponseEntity<ShiftDto> create(@RequestBody ShiftDto dto) {
        try {
            ShiftDto created = shiftService.create(dto, getRequester());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- UPDATE SHIFT ---
    @PutMapping("/{id}")
    public ResponseEntity<ShiftDto> update(@PathVariable Long id, @RequestBody ShiftDto dto) {
        try {
            return ResponseEntity.ok(shiftService.update(id, dto, getRequester()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- DELETE SHIFT ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            shiftService.delete(id, getRequester());
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
