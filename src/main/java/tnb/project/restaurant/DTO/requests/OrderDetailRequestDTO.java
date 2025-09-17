package tnb.project.restaurant.DTO.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDetailRequestDTO {
    private Long dishId;
    private int quantity;
    private List<Long> optionDetailIds;
    private String note;

}
