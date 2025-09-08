package tnb.project.restaurant.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import tnb.project.restaurant.entities.Employee;
@Getter
@Setter
@NoArgsConstructor
public class EmployeeDTO {
    private String id;
    private String username;
    private String fullName;
    private String role;

    public EmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.username = employee.getUsername();
        this.fullName = employee.getFullName();
        this.role = employee.getRole();
    }

}

