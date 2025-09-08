    package tnb.project.restaurant.DTO.responses;

    import lombok.Getter;
    import lombok.Setter;
    import java.time.LocalDateTime;

    @Getter
    @Setter
    public class ServiceResponseDTO {
        private Long id;
        private String sessionId;
        private Long serviceTypeId;
        private LocalDateTime createdAt;
        private LocalDateTime handleTime;
        private String status;
        private String serviceTypeName;
        private Integer tableNumber;

    }
