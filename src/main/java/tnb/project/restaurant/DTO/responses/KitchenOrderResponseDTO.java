package tnb.project.restaurant.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class KitchenOrderResponseDTO {
    private String tableName;
    private String status;
    private List<OrderDetailResponseDTO> orderDetails;

}
