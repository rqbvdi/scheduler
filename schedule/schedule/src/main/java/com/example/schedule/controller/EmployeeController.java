package com.example.schedule.controller;

import com.example.schedule.entity.Employee;
import com.example.schedule.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // Get all employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = service.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // Get employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = service.getEmployee(id);
        return ResponseEntity.ok(employee);
    }

    // Create a new employee
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using Spring Security
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee created = service.createEmployee(employee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Update an existing employee
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using Spring Security
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        Employee updated = service.updateEmployee(id, employee);
        return ResponseEntity.ok(updated);
    }

    // Delete an employee
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment if using Spring Security
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
