package tnb.project.restaurant.DTO.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateServiceRequestDTO {
    private String sessionId;
    private Long serviceTypeId;
}

