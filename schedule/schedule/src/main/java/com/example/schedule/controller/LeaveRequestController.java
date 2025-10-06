package com.example.schedule.controller;

import com.example.schedule.entity.Employee;
import com.example.schedule.entity.LeaveRequest;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.service.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    // Get all leave requests (admin only, but can be filtered later)
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllRequests() {
        List<LeaveRequest> requests = leaveRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    // Get leave request by ID
    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getRequest(@PathVariable Long id) {
        LeaveRequest request = leaveRequestService.getRequest(id);
        return ResponseEntity.ok(request);
    }

    // Create a new leave request
    @PostMapping
    public ResponseEntity<LeaveRequest> createRequest(@RequestBody LeaveRequest request) {
        LeaveRequest created = leaveRequestService.createRequest(request);
        return ResponseEntity.ok(created);
    }

    // Approve a leave request (admin only)
    @PostMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveRequest(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam Role role
    ) {
        LeaveRequest approved = leaveRequestService.approveRequest(id, approverId, role);
        return ResponseEntity.ok(approved);
    }

    // Reject a leave request (admin only)
    @PostMapping("/{id}/reject")
    public ResponseEntity<LeaveRequest> rejectRequest(
            @PathVariable Long id,
            @RequestParam Long approverId,
            @RequestParam Role role
    ) {
        LeaveRequest rejected = leaveRequestService.rejectRequest(id, approverId, role);
        return ResponseEntity.ok(rejected);
    }

    // Cancel a leave request (admin or the employee who created it)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<LeaveRequest> cancelRequest(
            @PathVariable Long id,
            @RequestBody Employee employee,
            @RequestParam Role role
    ) {
        LeaveRequest cancelled = leaveRequestService.cancelRequest(id, employee, role);
        return ResponseEntity.ok(cancelled);
    }
}
