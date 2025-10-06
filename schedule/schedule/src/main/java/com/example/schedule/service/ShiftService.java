package com.example.schedule.service;

import com.example.schedule.dto.ShiftDto;
import com.example.schedule.entity.Employee;
import com.example.schedule.entity.Shift;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.repository.ShiftRepository;
import com.example.schedule.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    // --- GET ALL SHIFTS (ADMIN only) ---
    public List<ShiftDto> getAll(Employee requester) {
        if (requester.getRole() != Role.ADMIN) {
            throw new SecurityException("Access denied: Only admins can view all shifts");
        }
        return shiftRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- GET SHIFT BY ID (Admins or the owner) ---
    public ShiftDto getById(Long id, Employee requester) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found"));

        if (requester.getRole() != Role.ADMIN && !shift.getEmployee().getId().equals(requester.getId())) {
            throw new SecurityException("Access denied: You can only view your own shifts");
        }
        return toDto(shift);
    }

    // --- GET SHIFTS FOR EMPLOYEE (Admins can query any, Users only themselves) ---
    public List<ShiftDto> getByEmployee(Long employeeId, Employee requester) {
        if (requester.getRole() != Role.ADMIN && !requester.getId().equals(employeeId)) {
            throw new SecurityException("Access denied: You can only view your own shifts");
        }

        return shiftRepository.findByEmployeeId(employeeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- GET SHIFTS IN DATE RANGE ---
    public List<ShiftDto> getInDateRange(LocalDate start, LocalDate end, Employee requester) {
        List<Shift> shifts;

        if (requester.getRole() == Role.ADMIN) {
            shifts = shiftRepository.findByShiftDateBetween(start, end);
        } else {
            shifts = shiftRepository.findByEmployeeIdAndShiftDateBetween(
                    requester.getId(), start, end
            );
        }

        return shifts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- CREATE SHIFT ---
    public ShiftDto create(ShiftDto dto, Employee requester) {
        if (requester.getRole() != Role.ADMIN && !dto.getEmployeeId().equals(requester.getId())) {
            throw new SecurityException("Users can only create shifts for themselves");
        }

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Shift shift = toEntity(dto);
        shift.setEmployee(employee);
        shift.setCreatedBy(requester); // assuming Employee has email
        Shift saved = shiftRepository.save(shift);

        return toDto(saved);
    }

    // --- UPDATE SHIFT ---
    public ShiftDto update(Long id, ShiftDto dto, Employee requester) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found"));

        if (requester.getRole() != Role.ADMIN && !shift.getEmployee().getId().equals(requester.getId())) {
            throw new SecurityException("Access denied: You can only update your own shifts");
        }

        shift.setShiftDate(dto.getShiftDate());
        shift.setStartTime(dto.getStartTime());
        shift.setEndTime(dto.getEndTime());
        shift.setStatus(dto.getStatus());
        shift.setDayType(dto.getDayType());
        shift.setNotes(dto.getNotes());

        return toDto(shiftRepository.save(shift));
    }

    // --- DELETE SHIFT ---
    public void delete(Long id, Employee requester) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found"));

        if (requester.getRole() != Role.ADMIN && !shift.getEmployee().getId().equals(requester.getId())) {
            throw new SecurityException("Access denied: You can only delete your own shifts");
        }

        shiftRepository.delete(shift);
    }

    // --- Mapping Helpers ---
    private ShiftDto toDto(Shift shift) {
        ShiftDto dto = new ShiftDto();
        dto.setId(shift.getId());
        dto.setShiftDate(shift.getShiftDate());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        dto.setStatus(shift.getStatus());
        dto.setDayType(shift.getDayType());
        dto.setEmployeeId(shift.getEmployee().getId());
        dto.setShiftPatternId(
                shift.getShiftPattern() != null ? shift.getShiftPattern().getId() : null
        );
        dto.setNotes(shift.getNotes());
        dto.setCreatedBy(shift.getCreatedBy());
        dto.setCreatedAt(shift.getCreatedAt());
        dto.setUpdatedAt(shift.getUpdatedAt());
        return dto;
    }

    private Shift toEntity(ShiftDto dto) {
        Shift shift = new Shift();
        shift.setId(dto.getId());
        shift.setShiftDate(dto.getShiftDate());
        shift.setStartTime(dto.getStartTime());
        shift.setEndTime(dto.getEndTime());
        shift.setStatus(dto.getStatus());
        shift.setDayType(dto.getDayType());
        shift.setNotes(dto.getNotes());
        return shift;
    }
}
