package tnb.project.restaurant.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tnb.project.restaurant.entities.Category;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishListDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private String status;
    private Category category;
}

