package tnb.project.restaurant.mapper;

import tnb.project.restaurant.DTO.PaymentDTO;
import tnb.project.restaurant.entities.Payment;

public class PaymentMapper {
    public static PaymentDTO toDTO(Payment payment) {
        if (payment == null) return null;
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder() != null ? payment.getOrder().getId() : null);
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionCode(payment.getTransactionCode());
        dto.setPaidAt(payment.getPaidAt());
        if (payment.getCashier() != null) {
            dto.setCashierId(payment.getCashier().getId());
        }
        return dto;
    }

    public static Payment toEntity(PaymentDTO dto) {
        if (dto == null) return null;
        Payment payment = new Payment();
        payment.setId(dto.getId());
        // Chỉ set order, cashier nếu cần, thường sẽ set ở service
        payment.setAmount(dto.getAmount());
        payment.setMethod(dto.getMethod());
        payment.setStatus(dto.getStatus());
        payment.setTransactionCode(dto.getTransactionCode());
        payment.setPaidAt(dto.getPaidAt());
        return payment;
    }
}

