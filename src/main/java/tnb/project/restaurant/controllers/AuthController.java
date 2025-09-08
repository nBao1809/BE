package tnb.project.restaurant.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.EmployeeDTO;
import tnb.project.restaurant.entities.Employee;
import tnb.project.restaurant.services.EmployeeService;
import tnb.project.restaurant.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        Employee employee = employeeService.login(username, password);
        String token;
        try {
            token = JwtUtils.generateToken(employee.getUsername(), employee.getRole());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating token");
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("employee", new EmployeeDTO(employee));
        return ResponseEntity.ok(resp);
    }
}
