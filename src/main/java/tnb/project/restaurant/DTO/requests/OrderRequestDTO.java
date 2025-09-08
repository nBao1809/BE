package tnb.project.restaurant.DTO.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderRequestDTO {
    private String sessionId;
    private List<OrderDetailRequestDTO> orderDetails;

}
