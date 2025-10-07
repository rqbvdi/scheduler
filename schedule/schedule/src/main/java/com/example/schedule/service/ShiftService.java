package com.example.schedule.service;

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

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    public List<Shift> getAll(Employee currentUser) {
        if (currentUser.getRole() == Role.ADMIN) {
            return shiftRepository.findAll();
        } else {
            return shiftRepository.findByEmployee(currentUser);
        }
    }

    public Shift getById(Long id, Employee currentUser) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found"));

        if (currentUser.getRole() != Role.ADMIN && !shift.getEmployee().getId().equals(currentUser.getId())) {
            throw new SecurityException("Not allowed");
        }

        return shift;
    }

    public Shift create(Shift shift) {
        return shiftRepository.save(shift);
    }

    public Shift update(Long id, Shift updatedShift, Employee currentUser) {
        Shift shift = getById(id, currentUser);
        shift.setStartTime(updatedShift.getStartTime());
        shift.setEndTime(updatedShift.getEndTime());
        shift.setStatus(updatedShift.getStatus());
        return shiftRepository.save(shift);
    }

    public void delete(Long id, Employee currentUser) {
        Shift shift = getById(id, currentUser);
        shiftRepository.delete(shift);
    }

    public List<Shift> getShiftsInRange(LocalDate start, LocalDate end, Employee currentUser) {
        if (currentUser.getRole() == Role.ADMIN) {
            return shiftRepository.findByDateBetween(start, end);
        } else {
            return shiftRepository.findByEmployeeAndDateBetween(currentUser, start, end);
        }
    }
}
