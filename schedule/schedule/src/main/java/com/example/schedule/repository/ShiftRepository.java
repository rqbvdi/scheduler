package com.example.schedule.repository;

import com.example.schedule.entity.Employee;
import com.example.schedule.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByEmployee(Employee employee);

    List<Shift> findByDateBetween(LocalDate start, LocalDate end);

    List<Shift> findByEmployeeAndDateBetween(Employee employee, LocalDate start, LocalDate end);
}
