package tnb.project.restaurant.DTO.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDetailQuantityStatusDTO {
    private Integer quantity;
    private String status;

}
