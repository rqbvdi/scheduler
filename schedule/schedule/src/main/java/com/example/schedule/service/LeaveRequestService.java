package com.example.schedule.service;

import com.example.schedule.entity.Employee;
import com.example.schedule.entity.LeaveRequest;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.entity.enums.RequestStatus;
import com.example.schedule.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository repository;

    public List<LeaveRequest> getAll(Employee currentUser) {
        if (currentUser.getRole() == Role.ADMIN) {
            return repository.findAll();
        } else {
            return repository.findByEmployee(currentUser);
        }
    }

    public LeaveRequest getById(Long id, Employee currentUser) {
        LeaveRequest request = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LeaveRequest not found"));

        if (currentUser.getRole() != Role.ADMIN && !request.getEmployee().getId().equals(currentUser.getId())) {
            throw new SecurityException("Not allowed");
        }

        return request;
    }

    public LeaveRequest create(LeaveRequest request) {
        request.setStatus(RequestStatus.PENDING);
        return repository.save(request);
    }

    public LeaveRequest approve(Long id, Employee adminUser) {
        if (adminUser.getRole() != Role.ADMIN) throw new SecurityException("Only admin can approve");
        LeaveRequest request = getById(id, adminUser);
        request.setStatus(RequestStatus.APPROVED);
        return repository.save(request);
    }

    public LeaveRequest reject(Long id, Employee adminUser) {
        if (adminUser.getRole() != Role.ADMIN) throw new SecurityException("Only admin can reject");
        LeaveRequest request = getById(id, adminUser);
        request.setStatus(RequestStatus.REJECTED);
        return repository.save(request);
    }
}
