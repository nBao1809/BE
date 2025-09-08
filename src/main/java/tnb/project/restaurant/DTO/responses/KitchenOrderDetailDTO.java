package tnb.project.restaurant.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KitchenOrderDetailDTO {
    private Long orderDetailId;
    private String dishName;
    private Integer quantity;
    private String note;
    private String status;
    private String tableName;
    private String orderId;
    private String orderStatus;
}

