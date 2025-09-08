package tnb.project.restaurant.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private String id;
    private String orderId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionCode;
    private LocalDateTime paidAt;
    private String cashierId;
}

