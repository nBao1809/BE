package tnb.project.restaurant.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.PageResponse;
import tnb.project.restaurant.DTO.requests.EmployeeCreateDTO;
import tnb.project.restaurant.DTO.EmployeeDTO;
import tnb.project.restaurant.entities.Employee;
import tnb.project.restaurant.repositorires.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepo, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepo = employeeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeDTO getEmployee(String id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        return new EmployeeDTO(employee);
    }

    public List<EmployeeDTO> getEmployees() {
        return employeeRepo.findAll().stream().map(EmployeeDTO::new).collect(Collectors.toList());
    }

    public EmployeeDTO createEmployee(EmployeeCreateDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty");
        }
        if (dto.getRole() == null || dto.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be empty");
        }
        if (employeeRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        Employee employee = new Employee();
        employee.setUsername(dto.getUsername());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setFullName(dto.getFullName());
        employee.setRole(dto.getRole());
        Employee saved = employeeRepo.save(employee);
        return new EmployeeDTO(saved);
    }

    public EmployeeDTO updateEmployee(String id, Employee employee) {
        Employee existingEmployee = employeeRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với id: " + id));
        if (employee.getUsername() != null && !employee.getUsername().trim().isEmpty()) {
            if (employeeRepo.findByUsername(employee.getUsername())
                    .filter(e -> !e.getId().equals(id)).isPresent()) {
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
            }
            existingEmployee.setUsername(employee.getUsername());
        }
        if (employee.getFullName() != null && !employee.getFullName().trim().isEmpty()) {
            existingEmployee.setFullName(employee.getFullName());
        }
        if (employee.getRole() != null && !employee.getRole().trim().isEmpty()) {
            existingEmployee.setRole(employee.getRole());
        }
        if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
            existingEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        Employee updated = employeeRepo.save(existingEmployee);
        return new EmployeeDTO(updated);
    }

    public void deleteEmployee(String id) {
        if (!employeeRepo.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên với id: " + id);
        }
        employeeRepo.deleteById(id);
    }

    public Employee login(String username, String password) {
        Employee employee = employeeRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng"));
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng");
        }
        return employee;
    }

    public PageResponse<EmployeeDTO> getEmployeesPage(Integer page, Integer size) {
        if (page != null && size != null) {
            Page<Employee> employeePage = employeeRepo.findAll(PageRequest.of(page, size));
            List<EmployeeDTO> content = employeePage.getContent().stream().map(EmployeeDTO::new).collect(Collectors.toList());
            return new PageResponse<>(content, employeePage.getNumber(), employeePage.getSize(), employeePage.getTotalElements(), employeePage.getTotalPages());
        } else {
            List<EmployeeDTO> employees = employeeRepo.findAll().stream().map(EmployeeDTO::new).collect(Collectors.toList());
            return new PageResponse<>(employees, 0, employees.size(), employees.size(), 1);
        }
    }
}
