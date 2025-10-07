package com.example.schedule.controller.swap;

import com.example.schedule.dto.SwapRequestDto;
import com.example.schedule.entity.Employee;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.service.SwapRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swap-requests")
@RequiredArgsConstructor
public class SwapRequestController {

    private final SwapRequestService swapRequestService;

    @GetMapping
    public ResponseEntity<List<SwapRequestDto>> getAll(@AuthenticationPrincipal Employee user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        List<SwapRequestDto> requests = swapRequestService.getAll(user, isAdmin);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SwapRequestDto> getById(@AuthenticationPrincipal Employee user,
                                                  @PathVariable Long id) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        SwapRequestDto dto = swapRequestService.getById(id, user, isAdmin);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SwapRequestDto> create(@AuthenticationPrincipal Employee user,
                                                 @RequestParam Long requestedShiftId,
                                                 @RequestParam Long offeredShiftId,
                                                 @RequestParam Long employeeFromId,
                                                 @RequestParam Long employeeToId,
                                                 @RequestParam String reason) {
        SwapRequestDto dto = swapRequestService.create(requestedShiftId, offeredShiftId,
                employeeFromId, employeeToId, reason, user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<SwapRequestDto> approve(@AuthenticationPrincipal Employee user,
                                                  @PathVariable Long id) {
        if (user.getRole() != Role.ADMIN) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        SwapRequestDto dto = swapRequestService.approve(id, user, true);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<SwapRequestDto> reject(@AuthenticationPrincipal Employee user,
                                                 @PathVariable Long id) {
        if (user.getRole() != Role.ADMIN) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        SwapRequestDto dto = swapRequestService.reject(id, user, true);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SwapRequestDto> cancel(@AuthenticationPrincipal Employee user,
                                                 @PathVariable Long id) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        SwapRequestDto dto = swapRequestService.cancel(id, user, isAdmin);
        return ResponseEntity.ok(dto);
    }
}
