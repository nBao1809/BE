package tnb.project.restaurant.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequestDTO {
    private String orderId;
    private String cashierId;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private Long loyaltyRedeemPoints;
    private String customerId;
}


