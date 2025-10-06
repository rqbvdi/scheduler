package com.example.schedule.controller;

import com.example.schedule.dto.SwapRequestDto;
import com.example.schedule.entity.Employee;
import com.example.schedule.service.SwapRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swap-requests")
@RequiredArgsConstructor
public class SwapRequestController {

    private final SwapRequestService swapRequestService;

    // Get all swap requests
    @GetMapping
    public ResponseEntity<List<SwapRequestDto>> getAll(
            @AuthenticationPrincipal Employee currentUser,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        List<SwapRequestDto> requests = swapRequestService.getAll(currentUser, isAdmin);
        return ResponseEntity.ok(requests);
    }

    // Get swap request by ID
    @GetMapping("/{id}")
    public ResponseEntity<SwapRequestDto> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal Employee currentUser,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        SwapRequestDto dto = swapRequestService.getById(id, currentUser, isAdmin);
        return ResponseEntity.ok(dto);
    }

    // Create a new swap request
    @PostMapping
    public ResponseEntity<SwapRequestDto> create(
            @RequestParam Long requestedShiftId,
            @RequestParam Long offeredShiftId,
            @RequestParam Long employeeFromId,
            @RequestParam Long employeeToId,
            @RequestParam String reason,
            @AuthenticationPrincipal Employee currentUser
    ) {
        SwapRequestDto dto = swapRequestService.create(
                requestedShiftId, offeredShiftId, employeeFromId, employeeToId, reason, currentUser
        );
        return ResponseEntity.ok(dto);
    }

    // Approve a swap request (Admin only)
    @PostMapping("/{id}/approve")
    public ResponseEntity<SwapRequestDto> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal Employee adminUser,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        SwapRequestDto dto = swapRequestService.approve(id, adminUser, isAdmin);
        return ResponseEntity.ok(dto);
    }

    // Reject a swap request (Admin only)
    @PostMapping("/{id}/reject")
    public ResponseEntity<SwapRequestDto> reject(
            @PathVariable Long id,
            @AuthenticationPrincipal Employee adminUser,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        SwapRequestDto dto = swapRequestService.reject(id, adminUser, isAdmin);
        return ResponseEntity.ok(dto);
    }

    // Cancel a swap request (User own or Admin)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<SwapRequestDto> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal Employee currentUser,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        SwapRequestDto dto = swapRequestService.cancel(id, currentUser, isAdmin);
        return ResponseEntity.ok(dto);
    }
}
