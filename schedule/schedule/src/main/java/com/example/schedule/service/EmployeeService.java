package com.example.schedule.service;

import com.example.schedule.entity.Employee;
import com.example.schedule.entity.enums.Role;
import com.example.schedule.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee getEmployee(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        return repository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        existing.setName(employee.getName());
        existing.setSurname(employee.getSurname());
        existing.setRole(employee.getRole());

        return repository.save(existing);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!repository.existsById(id)) throw new IllegalArgumentException("Employee not found");
        repository.deleteById(id);
    }
}
