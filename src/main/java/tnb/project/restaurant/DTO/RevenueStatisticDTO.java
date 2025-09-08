package tnb.project.restaurant.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RevenueStatisticDTO {
    private String label; // Ngày, tháng, hoặc năm
    private BigDecimal total;
}

