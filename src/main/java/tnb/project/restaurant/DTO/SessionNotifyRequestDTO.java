package tnb.project.restaurant.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionNotifyRequestDTO {
    private String sessionId;
    private String paymentMethod;
}

