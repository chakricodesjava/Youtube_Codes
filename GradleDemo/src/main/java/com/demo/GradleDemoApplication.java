package com.demo;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
@RestController
public class GradleDemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GradleDemoApplication.class, args);
	}

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World! "+ LocalDate.now()+ " Day: "+LocalDate.now().getDayOfWeek();
    }

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started with command-line arguments: "
                + String.join(" ", args));
        System.out.println("Hello, World! " + LocalDate.now() +
                " Day: " + LocalDate.now().getDayOfWeek());

        // Auto-insert 20 employees if not already present
        if (employeeRepository.count() < 20) {
            List<String> roles = Arrays.asList("Developer", "Manager", "QA", "DevOps");
            IntStream.rangeClosed(1, 20).forEach(i -> {
                Employee emp = new Employee("Employee" + i, roles.get(i % roles.size()));
                employeeRepository.save(emp);
            });
            System.out.println("20 Employees inserted!");
        }

        // Example custom query usage
        System.out.println("Find all Developers:");
        employeeRepository.findByRole("Developer").forEach(System.out::println);

       System.out.println("Employee3 count : "+employeeRepository.countByNameNative("Employee3"));

    }
}
