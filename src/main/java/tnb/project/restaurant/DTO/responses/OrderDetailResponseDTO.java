package tnb.project.restaurant.DTO.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDetailResponseDTO {
    private Long id;
    private Long dishId;
    private String dishName;
    private int quantity;
    private String note;
    private String status;
    private BigDecimal price;
}
