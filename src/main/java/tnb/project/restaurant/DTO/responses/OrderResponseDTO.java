package tnb.project.restaurant.DTO.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponseDTO {
    private String id;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String status;
    private List<OrderDetailResponseDTO> orderDetails;
}

