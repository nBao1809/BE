package tnb.project.restaurant.DTO.responses;

import java.math.BigDecimal;
import java.util.List;

public class CategoryWithDishesDTO {
    public Long id;
    public String name;
    public List<DishShortDTO> dishes;

    public static class DishShortDTO {
        public Long id;
        public String name;
        public BigDecimal price;
        public String imageUrl;
        public String status;
    }
}

