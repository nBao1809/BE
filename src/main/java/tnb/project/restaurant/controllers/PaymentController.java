package tnb.project.restaurant.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.CashPaymentRequestDTO;
import tnb.project.restaurant.DTO.SessionNotifyRequestDTO;
import tnb.project.restaurant.DTO.VnpayPaymentRequestDTO;
import tnb.project.restaurant.entities.Payment;
import tnb.project.restaurant.DTO.PaymentDTO;
import tnb.project.restaurant.services.PaymentService;
import tnb.project.restaurant.DTO.RevenueStatisticDTO;
import tnb.project.restaurant.DTO.requests.UpdateStatusDTO;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final SimpMessagingTemplate messagingTemplate;

    public PaymentController(PaymentService paymentService, SimpMessagingTemplate messagingTemplate) {
        this.paymentService = paymentService;
        this.messagingTemplate = messagingTemplate;
    }


    @GetMapping
    ResponseEntity<List<PaymentDTO>> getPayments() {
        List<PaymentDTO> payments = paymentService.getPaymentDTOs();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    ResponseEntity<PaymentDTO> getPayment(@PathVariable String paymentId) {
        PaymentDTO payment = paymentService.getPaymentDTO(paymentId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping
    ResponseEntity<Payment> createVNPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    @PatchMapping("/{paymentId}")
    ResponseEntity<Payment> updatePayment(@PathVariable String paymentId, @RequestBody Payment payment) {
        Payment updatedPayment = paymentService.updatePayment(paymentId, payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @PatchMapping("/{paymentId}/status")
    public ResponseEntity<Payment> updateStatus(@PathVariable String paymentId, @RequestBody UpdateStatusDTO dto) {
        Payment updated = paymentService.updateStatus(paymentId, dto.getStatus());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{paymentId}")
    ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics/revenue")
    public ResponseEntity<List<RevenueStatisticDTO>> getRevenueStatistics(
            @RequestParam String type,
            @RequestParam String from,
            @RequestParam String to) {
        LocalDateTime fromDate = LocalDateTime.parse(from);
        LocalDateTime toDate = LocalDateTime.parse(to);
        List<RevenueStatisticDTO> result;
        switch (type.toLowerCase()) {
            case "day" -> result = paymentService.getRevenueByDay(fromDate, toDate);
            case "month" -> result = paymentService.getRevenueByMonth(fromDate, toDate);
            case "year" -> result = paymentService.getRevenueByYear(fromDate, toDate);
            default -> throw new IllegalArgumentException("Invalid type. Must be 'day', 'week', 'month' or 'year'.");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cash")
    public ResponseEntity<?> payByCash(@RequestBody CashPaymentRequestDTO cashPaymentRequestDTO) {

        PaymentDTO payment= paymentService.processCashPayment(cashPaymentRequestDTO);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/pay")
    public ResponseEntity<?> createQRPayment(@RequestBody VnpayPaymentRequestDTO paymentDTO, HttpServletRequest request) {
        try {
            String paymentUrl = paymentService.processQRPayment(paymentDTO, request);
            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error creating VNPay payment URL: " + e.getMessage());
        }
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<String> vnpayIpn(HttpServletRequest request) {
        String result = paymentService.processVnpayIpn(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/session-notify")
    public ResponseEntity<?> notifyCashierSession(@RequestBody SessionNotifyRequestDTO request) {
        String sessionId = request.getSessionId();
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("sessionId không hợp lệ");
        }
        messagingTemplate.convertAndSend("/topic/cashier/session-payment", request);
        return ResponseEntity.ok("SessionId đã được gửi tới cashier qua WebSocket");
    }
}
