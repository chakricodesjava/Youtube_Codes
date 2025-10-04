package com.demo.service;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> findByName(String name) {
        return employeeRepository.findByName(name);
    }

    public List<Employee> findByRole(String role) {
        return employeeRepository.findByRole(role);
    }

    public List<Employee> findByNamePattern(String pattern) {
        return employeeRepository.findByNamePattern(pattern);
    }

    public List<Employee> findByRoleNative(String role) {
        return employeeRepository.findByRoleNative(role);
    }

    public int countByNameNative(String name) {
        return employeeRepository.countByNameNative(name);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
}

