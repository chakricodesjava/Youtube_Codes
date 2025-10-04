package com.demo.repository;

import com.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Find employees by name (JPQL)
    List<Employee> findByName(String name);

    // Find employees by role (JPQL)
    List<Employee> findByRole(String role);

    // JPQL custom query
    @Query("SELECT e FROM Employee e WHERE e.name LIKE %:pattern%")
    List<Employee> findByNamePattern(String pattern);

    // Native SQL query: Find all employees with a specific role
    @Query(value = "SELECT * FROM employee_demo WHERE role = ?1", nativeQuery = true)
    List<Employee> findByRoleNative(String role);

    // Native SQL query: Count employees with a specific name
    @Query(value = "SELECT COUNT(*) FROM employee_demo WHERE name = ?1", nativeQuery = true)
    int countByNameNative(String name);
}
