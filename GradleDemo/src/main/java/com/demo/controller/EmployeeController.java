package com.demo.controller;

import com.demo.model.Employee;
import com.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/name/{name}")
    public List<Employee> getEmployeesByName(@PathVariable String name) {
        return employeeService.findByName(name);
    }

    @GetMapping("/role/{role}")
    public List<Employee> getEmployeesByRole(@PathVariable String role) {
        return employeeService.findByRole(role);
    }

    @GetMapping("/pattern/{pattern}")
    public List<Employee> getEmployeesByNamePattern(@PathVariable String pattern) {
        return employeeService.findByNamePattern(pattern);
    }

    @GetMapping("/native/role/{role}")
    public List<Employee> getEmployeesByRoleNative(@PathVariable String role) {
        return employeeService.findByRoleNative(role);
    }

    @GetMapping("/native/count/{name}")
    public int countEmployeesByNameNative(@PathVariable String name) {
        return employeeService.countByNameNative(name);
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
    }
}

