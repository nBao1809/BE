package tnb.project.restaurant.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SessionDTO {
    private String id;
    private Long tableId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}

