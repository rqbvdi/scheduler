package com.example.schedule.controller;

import com.example.schedule.dto.SwapRequestDto;
import com.example.schedule.entity.Employee;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.service.SwapRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swap-requests")
@RequiredArgsConstructor
public class SwapRequestController {

    private final SwapRequestService swapRequestService;

    private Employee getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Employee) {
            return (Employee) principal;
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            // adapt if your UserDetails wraps Employee
            org.springframework.security.core.userdetails.UserDetails userDetails =
                    (org.springframework.security.core.userdetails.UserDetails) principal;
            // assume Employee object can be retrieved by username
            throw new RuntimeException("Employee retrieval not implemented. Use CustomUserDetails to get Employee");
        }
        throw new RuntimeException("Unauthorized");
    }

    @GetMapping
    public ResponseEntity<List<SwapRequestDto>> getAll() {
        Employee user = getCurrentUser();
        boolean isAdmin = user.getRole() == Role.ADMIN;
        List<SwapRequestDto> requests = swapRequestService.getAll(user, isAdmin);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SwapRequestDto> getById(@PathVariable Long id) {
        Employee user = getCurrentUser();
        boolean isAdmin = user.getRole() == Role.ADMIN;

        SwapRequestDto dto = swapRequestService.getById(id, user, isAdmin);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SwapRequestDto> create(@RequestParam Long requestedShiftId,
                                                 @RequestParam Long offeredShiftId,
                                                 @RequestParam Long employeeFromId,
                                                 @RequestParam Long employeeToId,
                                                 @RequestParam String reason) {
        Employee user = getCurrentUser();

        SwapRequestDto dto = swapRequestService.create(requestedShiftId, offeredShiftId,
                employeeFromId, employeeToId, reason, user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<SwapRequestDto> approve(@PathVariable Long id) {
        Employee user = getCurrentUser();
        if (user.getRole() != Role.ADMIN) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        SwapRequestDto dto = swapRequestService.approve(id, user, true);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<SwapRequestDto> reject(@PathVariable Long id) {
        Employee user = getCurrentUser();
        if (user.getRole() != Role.ADMIN) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        SwapRequestDto dto = swapRequestService.reject(id, user, true);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SwapRequestDto> cancel(@PathVariable Long id) {
        Employee user = getCurrentUser();
        boolean isAdmin = user.getRole() == Role.ADMIN;

        SwapRequestDto dto = swapRequestService.cancel(id, user, isAdmin);
        return ResponseEntity.ok(dto);
    }
}
