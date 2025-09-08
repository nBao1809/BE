package tnb.project.restaurant.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CashPaymentRequestDTO {
    private String customerId;
    private Long loyaltyPoints;
    private Long loyaltyRedeemPoints;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String orderId;
    private String cashierId;
}

