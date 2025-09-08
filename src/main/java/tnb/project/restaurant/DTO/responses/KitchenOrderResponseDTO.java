package tnb.project.restaurant.DTO.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class KitchenOrderResponseDTO {
    private String tableName;
    private String status;
    private List<OrderDetailResponseDTO> orderDetails;

    public KitchenOrderResponseDTO() {}
    public KitchenOrderResponseDTO(String tableName, String status, List<OrderDetailResponseDTO> orderDetails) {
        this.tableName = tableName;
        this.status = status;
        this.orderDetails = orderDetails;
    }

}
