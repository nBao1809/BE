package tnb.project.restaurant.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class VnpayPaymentRequestDTO extends CashPaymentRequestDTO {
    private String bankCode;
    private String language;
}

