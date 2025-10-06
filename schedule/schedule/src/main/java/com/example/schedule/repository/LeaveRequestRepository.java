package com.example.schedule.repository;

import com.example.schedule.entity.LeaveRequest;
import com.example.schedule.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee(Employee employee); // optional, get employee's leaves
}
