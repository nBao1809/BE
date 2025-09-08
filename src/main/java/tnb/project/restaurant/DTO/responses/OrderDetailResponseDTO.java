package tnb.project.restaurant.DTO.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponseDTO {
    private Long dishId;
    private String dishName;
    private int quantity;
    private String note;
    private String status;
}
