package com.example.schedule.service;

import com.example.schedule.dto.SwapRequestDto;
import com.example.schedule.entity.*;
import com.example.schedule.entity.enums.RequestStatus;
import com.example.schedule.repository.SwapRequestRepository;
import com.example.schedule.repository.ShiftRepository;
import com.example.schedule.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SwapRequestService {

    private final SwapRequestRepository swapRequestRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;

    // ✅ Admin: all, User: only own
    @Transactional(readOnly = true)
    public List<SwapRequestDto> getAll(Employee currentUser, boolean isAdmin) {
        List<SwapRequest> requests;
        if (isAdmin) {
            requests = swapRequestRepository.findAll();
        } else {
            requests = swapRequestRepository.findByEmployeeFromOrEmployeeTo(currentUser, currentUser);
        }
        return requests.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ✅ Admin: all, User: only own
    @Transactional(readOnly = true)
    public SwapRequestDto getById(Long id, Employee currentUser, boolean isAdmin) {
        SwapRequest request = swapRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SwapRequest not found"));

        if (!isAdmin && !request.getEmployeeFrom().getId().equals(currentUser.getId())
                && !request.getEmployeeTo().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed to view this request");
        }
        return toDto(request);
    }

    // ✅ User can create
    public SwapRequestDto create(Long requestedShiftId, Long offeredShiftId,
                                 Long employeeFromId, Long employeeToId,
                                 String reason, Employee currentUser) {
        if (!currentUser.getId().equals(employeeFromId)) {
            throw new AccessDeniedException("Users can only create requests on their own behalf");
        }

        Shift requestedShift = shiftRepository.findById(requestedShiftId)
                .orElseThrow(() -> new EntityNotFoundException("Requested shift not found"));
        Shift offeredShift = shiftRepository.findById(offeredShiftId)
                .orElseThrow(() -> new EntityNotFoundException("Offered shift not found"));
        Employee employeeFrom = employeeRepository.findById(employeeFromId)
                .orElseThrow(() -> new EntityNotFoundException("Employee from not found"));
        Employee employeeTo = employeeRepository.findById(employeeToId)
                .orElseThrow(() -> new EntityNotFoundException("Employee to not found"));

        SwapRequest request = new SwapRequest();
        request.setRequestedShift(requestedShift);
        request.setOfferedShift(offeredShift);
        request.setEmployeeFrom(employeeFrom);
        request.setEmployeeTo(employeeTo);
        request.setStatus(RequestStatus.PENDING);
        request.setReason(reason);

        return toDto(swapRequestRepository.save(request));
    }

    // ✅ Admin only
    public SwapRequestDto approve(Long id, Employee adminUser, boolean isAdmin) {
        if (!isAdmin) throw new AccessDeniedException("Only admins can approve requests");

        SwapRequest request = swapRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SwapRequest not found"));

        request.setStatus(RequestStatus.APPROVED);
        request.setApprovedBy(adminUser);
        request.setApprovedAt(ZonedDateTime.now());

        return toDto(swapRequestRepository.save(request));
    }

    // ✅ Admin only
    public SwapRequestDto reject(Long id, Employee adminUser, boolean isAdmin) {
        if (!isAdmin) throw new AccessDeniedException("Only admins can reject requests");

        SwapRequest request = swapRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SwapRequest not found"));

        request.setStatus(RequestStatus.REJECTED);
        request.setApprovedBy(adminUser);
        request.setApprovedAt(ZonedDateTime.now());

        return toDto(swapRequestRepository.save(request));
    }

    // ✅ User: cancel own pending; Admin: cancel any
    public SwapRequestDto cancel(Long id, Employee currentUser, boolean isAdmin) {
        SwapRequest request = swapRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SwapRequest not found"));

        if (!isAdmin && !request.getEmployeeFrom().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Users can only cancel their own requests");
        }

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be canceled");
        }

        request.setStatus(RequestStatus.CANCELLED);
        return toDto(swapRequestRepository.save(request));
    }

    // --- DTO Mapper ---
    private SwapRequestDto toDto(SwapRequest request) {
        SwapRequestDto dto = new SwapRequestDto();
        dto.setId(request.getId());
        dto.setRequestedShiftId(request.getRequestedShift().getId());
        dto.setOfferedShiftId(request.getOfferedShift().getId());
        dto.setEmployeeFromId(request.getEmployeeFrom().getId());
        dto.setEmployeeToId(request.getEmployeeTo().getId());
        dto.setStatus(request.getStatus());
        dto.setReason(request.getReason());
        dto.setApprovedById(request.getApprovedBy() != null ? request.getApprovedBy().getId() : null);
        dto.setApprovedAt(request.getApprovedAt());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
}
