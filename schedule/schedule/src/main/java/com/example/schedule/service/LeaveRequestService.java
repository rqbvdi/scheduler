package com.example.schedule.service;

import com.example.schedule.entity.LeaveRequest;
import com.example.schedule.entity.Employee;
import com.example.schedule.entity.enums.RequestStatus;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository repository;

    public LeaveRequestService(LeaveRequestRepository repository) {
        this.repository = repository;
    }

    public List<LeaveRequest> getAllRequests() {
        return repository.findAll();
    }

    public LeaveRequest getRequest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));
    }

    @Transactional
    public LeaveRequest createRequest(LeaveRequest request) {
        request.setStatus(RequestStatus.PENDING);
        return repository.save(request);
    }

    @Transactional
    public LeaveRequest approveRequest(Long id, Long approverId, Role role) {
        LeaveRequest request = getRequest(id);

        if (role != Role.ADMIN) throw new SecurityException("Only admin can approve requests");

        request.setStatus(RequestStatus.APPROVED);
        request.setApprovedBy(approverId);
        request.setApprovedAt(ZonedDateTime.now());
        return repository.save(request);
    }

    @Transactional
    public LeaveRequest rejectRequest(Long id, Long approverId, Role role) {
        LeaveRequest request = getRequest(id);

        if (role != Role.ADMIN) throw new SecurityException("Only admin can reject requests");

        request.setStatus(RequestStatus.REJECTED);
        request.setApprovedBy(approverId);
        request.setApprovedAt(ZonedDateTime.now());
        return repository.save(request);
    }

    @Transactional
    public LeaveRequest cancelRequest(Long id, Employee employee, Role role) {
        LeaveRequest request = getRequest(id);

        if (role != Role.ADMIN && !request.getEmployee().getId().equals(employee.getId())) {
            throw new SecurityException("Cannot cancel others' requests");
        }

        request.setStatus(RequestStatus.CANCELLED);
        request.setApprovedBy(role == Role.ADMIN ? employee.getId() : null);
        request.setApprovedAt(ZonedDateTime.now());
        return repository.save(request);
    }
}
