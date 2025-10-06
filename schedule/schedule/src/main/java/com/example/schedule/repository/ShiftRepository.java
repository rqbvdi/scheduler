package com.example.schedule.repository;

import com.example.schedule.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    // Find all shifts for an employee
    List<Shift> findByEmployeeId(Long employeeId);

    // Find all shifts within a date range
    List<Shift> findByShiftDateBetween(LocalDate startDate, LocalDate endDate);

    // Find all shifts for an employee in a date range
    List<Shift> findByEmployeeIdAndShiftDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
