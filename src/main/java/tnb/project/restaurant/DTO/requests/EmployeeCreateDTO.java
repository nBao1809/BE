package tnb.project.restaurant.DTO.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCreateDTO {
    private String username;
    private String password;
    private String fullName;
    private String role;
}

