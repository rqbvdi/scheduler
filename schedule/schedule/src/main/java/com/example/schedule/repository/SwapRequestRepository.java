package com.example.schedule.repository;

import com.example.schedule.entity.SwapRequest;
import com.example.schedule.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
    List<SwapRequest> findByEmployeeFromOrEmployeeTo(Employee from, Employee to); // swaps involving employee
}
