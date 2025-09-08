package tnb.project.restaurant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.requests.EmployeeCreateDTO;
import tnb.project.restaurant.DTO.EmployeeDTO;
import tnb.project.restaurant.entities.Employee;
import tnb.project.restaurant.services.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    ResponseEntity<List<EmployeeDTO>> getEmployees() {
        List<EmployeeDTO> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}")
    ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String employeeId) {
        EmployeeDTO employee = employeeService.getEmployee(employeeId);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeCreateDTO employeeCreateDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PatchMapping("/{employeeId}")
    ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable String employeeId, @RequestBody Employee employee) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeId, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}")
    ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}
