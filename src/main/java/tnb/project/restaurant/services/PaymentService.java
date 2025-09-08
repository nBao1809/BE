package tnb.project.restaurant.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.CashPaymentRequestDTO;
import tnb.project.restaurant.DTO.VnpayPaymentRequestDTO;
import tnb.project.restaurant.config.VnpayConfig;
import tnb.project.restaurant.entities.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import tnb.project.restaurant.repositorires.PaymentRepository;
import tnb.project.restaurant.repositorires.OrdersRepository;
import tnb.project.restaurant.repositorires.EmployeeRepository;
import tnb.project.restaurant.DTO.PaymentDTO;
import tnb.project.restaurant.mapper.PaymentMapper;

import java.util.stream.Collectors;

import tnb.project.restaurant.DTO.RevenueStatisticDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final EmployeeRepository employeeRepository;
    private final OrdersService ordersService;
    private final CustomerService customerService;
    public final SessionService sessionService;

    public PaymentService(PaymentRepository paymentRepository, OrdersRepository ordersRepository, EmployeeRepository employeeRepository, OrdersService ordersService, CustomerService customerService, SessionService sessionService) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
        this.employeeRepository = employeeRepository;
        this.ordersService = ordersService;
        this.customerService = customerService;
        this.sessionService = sessionService;
    }

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán với id: " + paymentId));
    }

    public Payment createPayment(Payment payment) {
        // Kiểm tra order
        if (payment.getOrder() == null || payment.getOrder().getId() == null) {
            throw new IllegalArgumentException("Phải chọn đơn hàng");
        }
        Orders order = ordersRepository.findById(payment.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        payment.setOrder(order);
        // Kiểm tra cashier nếu có
        if (payment.getCashier() != null && payment.getCashier().getId() != null) {
            Employee cashier = employeeRepository.findById(payment.getCashier().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thu ngân"));
            payment.setCashier(cashier);
        } else {
            payment.setCashier(null);
        }
        // Kiểm tra amount
        if (payment.getAmount() == null) {
            throw new IllegalArgumentException("Số tiền không được để trống");
        }
        // Kiểm tra method
        if (payment.getMethod() == null || payment.getMethod().isEmpty()) {
            throw new IllegalArgumentException("Phương thức thanh toán không được để trống");
        }
        // Kiểm tra status
        if (payment.getStatus() == null || payment.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }
        // Kiểm tra paidAt
        if (payment.getPaidAt() == null) {
            throw new IllegalArgumentException("Thời gian thanh toán không được để trống");
        }
        return paymentRepository.save(payment);
    }

    public Payment updatePayment(String paymentId, Payment payment) {
        Payment existing = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán với id: " + paymentId));
        // Update order nếu có
        if (payment.getOrder() != null && payment.getOrder().getId() != null) {
            Orders order = ordersRepository.findById(payment.getOrder().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
            existing.setOrder(order);
        }
        // Update cashier nếu có
        if (payment.getCashier() != null && payment.getCashier().getId() != null) {
            Employee cashier = employeeRepository.findById(payment.getCashier().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thu ngân"));
            existing.setCashier(cashier);
        }
        if (payment.getAmount() != null) {
            existing.setAmount(payment.getAmount());
        }
        if (payment.getMethod() != null && !payment.getMethod().isEmpty()) {
            existing.setMethod(payment.getMethod());
        }
        if (payment.getStatus() != null && !payment.getStatus().isEmpty()) {
            existing.setStatus(payment.getStatus());
        }
        if (payment.getPaidAt() != null) {
            existing.setPaidAt(payment.getPaidAt());
        }
        if (payment.getTransactionCode() != null && !payment.getTransactionCode().isEmpty()) {
            existing.setTransactionCode(payment.getTransactionCode());
        }
        return paymentRepository.save(existing);
    }

    public void deletePayment(String paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new IllegalArgumentException("Không tìm thấy thanh toán với id: " + paymentId);
        }
        paymentRepository.deleteById(paymentId);
    }

    public java.util.List<PaymentDTO> getPaymentDTOs() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO getPaymentDTO(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return PaymentMapper.toDTO(payment);
    }

    public List<RevenueStatisticDTO> getRevenueByDay(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = paymentRepository.getRevenueByDay(from, to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return results.stream()
                .map(obj -> new RevenueStatisticDTO(
                        obj[0] != null ? obj[0].toString() : "",
                        obj[1] != null ? (BigDecimal) obj[1] : BigDecimal.ZERO
                ))
                .toList();
    }


    public List<RevenueStatisticDTO> getRevenueByYear(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = paymentRepository.getRevenueByYear(from, to);
        return results.stream()
                .map(obj -> new RevenueStatisticDTO(
                        obj[0] != null ? obj[0].toString() : "",
                        obj[1] != null ? (BigDecimal) obj[1] : BigDecimal.ZERO
                ))
                .toList();
    }

    public List<RevenueStatisticDTO> getRevenueByMonth(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = paymentRepository.getRevenueByMonth(from, to);
        return results.stream()
                .map(obj -> new RevenueStatisticDTO(obj[1] + "/" + obj[0],
                        obj[2] != null ? (BigDecimal) obj[2] : BigDecimal.ZERO
                ))
                .toList();
    }

    public Payment updateStatus(String paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán với id: " + paymentId));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }


    public Payment completePayment(String paymentId, String status, String transactionCode, LocalDateTime paidAt) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }
        if (transactionCode == null || transactionCode.isEmpty()) {
            throw new IllegalArgumentException("Mã giao dịch không được để trống");
        }
        if (paidAt == null) {
            throw new IllegalArgumentException("Thời gian thanh toán không được để trống");
        }
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán với id: " + paymentId));
        payment.setStatus(status);
        payment.setTransactionCode(transactionCode);
        payment.setPaidAt(paidAt);
        return paymentRepository.save(payment);
    }

    @Transactional
    public PaymentDTO processCashPayment(CashPaymentRequestDTO cashDTO) {
        // Validate đầu vào
        if (cashDTO == null) throw new IllegalArgumentException("Dữ liệu thanh toán không được để trống");
        if (cashDTO.getOrderId() == null || cashDTO.getOrderId().isEmpty())
            throw new IllegalArgumentException("Phải chọn đơn hàng");
        if (cashDTO.getCashierId() == null || cashDTO.getCashierId().isEmpty())
            throw new IllegalArgumentException("Phải chọn thu ngân");
        if (cashDTO.getTotalAmount() == null) throw new IllegalArgumentException("Tổng tiền không được để trống");

        // Lấy order và cashier
        Orders order = ordersRepository.findById(cashDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        Employee cashier = employeeRepository.findById(cashDTO.getCashierId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thu ngân"));

        // Nếu có customerId, set customer cho order
        if (cashDTO.getCustomerId() != null && !cashDTO.getCustomerId().isEmpty()) {
            Customer customer = customerService.getCustomer(cashDTO.getCustomerId());
            order.setCustomer(customer);
            // Cập nhật loyaltyRedeemPoints, discountAmount cho order
            order.setLoyaltyRedeemPoints(cashDTO.getLoyaltyRedeemPoints());
            order.setDiscountAmount(cashDTO.getDiscountAmount());
            // Tính điểm tích lũy: mỗi 10k được 1 điểm
            long currentPoints = customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0L;
            long redeemPoints = cashDTO.getLoyaltyRedeemPoints() != null ? cashDTO.getLoyaltyRedeemPoints() : 0L;
            long plusPoints = cashDTO.getTotalAmount() != null ? cashDTO.getTotalAmount().divide(new java.math.BigDecimal(10000)).longValue() : 0L;
            long newPoints = currentPoints - redeemPoints + plusPoints;
            if (newPoints < 0) newPoints = 0;
            customer.setLoyaltyPoints(newPoints);
            customerService.updateCustomer(customer.getId(), customer);
        }

        // Cập nhật order
        order.setTotalAmount(cashDTO.getTotalAmount());
        order.setStatus("PAID");
        ordersRepository.save(order);

        // Tạo payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setCashier(cashier);
        payment.setAmount(cashDTO.getTotalAmount());
        payment.setMethod("CASH");
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // Kết thúc session (có thể truyền customerId null hoặc rỗng)
        sessionService.endSession(order.getSession().getId(), cashDTO.getCustomerId());

        // Trả về PaymentDTO
        return PaymentMapper.toDTO(payment);
    }

    public String processQRPayment(VnpayPaymentRequestDTO vnpayDTO, HttpServletRequest request) throws IOException {
        // Validate đầu vào
        if (vnpayDTO == null) throw new IllegalArgumentException("Dữ liệu thanh toán không được để trống");
        if (vnpayDTO.getOrderId() == null || vnpayDTO.getOrderId().isEmpty())
            throw new IllegalArgumentException("Phải chọn đơn hàng");
        if (vnpayDTO.getTotalAmount() == null) throw new IllegalArgumentException("Tổng tiền không được để trống");
        if (vnpayDTO.getCashierId() == null || vnpayDTO.getCashierId().isEmpty())
            throw new IllegalArgumentException("Phải chọn thu ngân");

        // Lấy order
        Orders order = ordersRepository.findById(vnpayDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
        Employee cashier = employeeRepository.findById(vnpayDTO.getCashierId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thu ngân"));
        // Nếu có customer
        Customer customer = null;
        if (vnpayDTO.getCustomerId() != null && !vnpayDTO.getCustomerId().isEmpty()) {
            customer = customerService.getCustomer(vnpayDTO.getCustomerId());
            // Cập nhật loyaltyRedeemPoints, discountAmount cho order
            order.setLoyaltyRedeemPoints(vnpayDTO.getLoyaltyRedeemPoints());
            order.setDiscountAmount(vnpayDTO.getDiscountAmount());
            order.setCustomer(customer);
        } else if (order.getSession() != null && order.getSession().getCustomer() != null) {
            // Nếu không truyền customerId nhưng session có customer thì set luôn cho order
            order.setCustomer(order.getSession().getCustomer());
        }

        // Tạo payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(vnpayDTO.getTotalAmount());
        payment.setMethod("VNPAY");
        payment.setStatus("PENDING");
        payment.setCashier(cashier);
        paymentRepository.save(payment);

        // Cập nhật order
        order.setTotalAmount(vnpayDTO.getTotalAmount());
        order.setStatus("WAITING_PAYMENT");
        ordersRepository.save(order);


        // Sinh URL QR như cũ
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = Integer.parseInt(String.valueOf(vnpayDTO.getTotalAmount())) * 100L;
        String bankCode = vnpayDTO.getBankCode();//VNPAYQR

        String vnp_TxnRef = vnpayDTO.getOrderId();
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);

        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = vnpayDTO.getLanguage();
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
        // Trả về trực tiếp paymentUrl thay vì vnp_Params
        return paymentUrl;
    }

    // Xử lý IPN từ VNPAY
    @Transactional
    public String processVnpayIpn(HttpServletRequest request) {

        try {

        /*  IPN URL: Record payment results from VNPAY
        Implementation steps:
        Check checksum
        Find transactions (vnp_TxnRef) in the database (checkOrderId)
        Check the payment status of transactions before updating (checkOrderStatus)
        Check the amount (vnp_Amount) of transactions before updating (checkAmount)
        Update results to Database
        Return recorded results to VNPAY
        */


            //Begin process return from VNPAY
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            // Check checksum
            String signValue = VnpayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {

                Orders order = ordersRepository.findById((String) fields.get("vnp_TxnRef"))
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
                // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the
                // amount of the code (vnp_TxnRef) in the Your database).
                boolean checkAmount = false;
                Payment payment = paymentRepository.findByOrder_Id(order.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán"));
                if (fields.get("vnp_Amount") != null) {
                    BigDecimal ipnAmount = new BigDecimal((String) fields.get("vnp_Amount")).divide(new BigDecimal(100));
                    if (ipnAmount.compareTo(payment.getAmount()) == 0 && ipnAmount.compareTo(order.getTotalAmount()) == 0) {
                        checkAmount = true;
                    }
                }
                boolean checkOrderStatus = false; // PaymnentStatus = 0 (pending)
                if ("VNPAY".equals(payment.getMethod()) && "PENDING".equals(payment.getStatus()) && "WAITING_PAYMENT".equals(order.getStatus())) {
                    checkOrderStatus = true;
                }
                if (checkAmount) {
                    if (checkOrderStatus) {
                        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                            //Here Code update PaymnentStatus = 1 into your Database
                            payment.setStatus("PAID");
                            payment.setTransactionCode((String) fields.get("vnp_TransactionNo"));
                            payment.setPaidAt(LocalDateTime.now());
                            paymentRepository.save(payment);
                            order.setStatus("PAID");
                            ordersRepository.save(order);
                            // Cập nhật loyaltyPoints cho customer nếu có
                            if (order.getCustomer() != null) {
                                Customer customer = order.getCustomer();
                                long currentPoints = customer.getLoyaltyPoints() != null ? customer.getLoyaltyPoints() : 0L;
                                long redeemPoints = order.getLoyaltyRedeemPoints() != null ? order.getLoyaltyRedeemPoints() : 0L;
                                long plusPoints = order.getTotalAmount() != null ? order.getTotalAmount().divide(new java.math.BigDecimal(10000)).longValue() : 0L; // Mỗi 10k được 1 điểm
                                long newPoints = currentPoints - redeemPoints + plusPoints;
                                if (newPoints < 0) newPoints = 0;
                                customer.setLoyaltyPoints(newPoints);
                                customerService.updateCustomer(customer.getId(), customer);

                            // Kết thúc session
                            sessionService.endSession(order.getSession().getId(), customer.getId());
                            }
                            return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
                        } else {
                            // Here Code update PaymnentStatus = 2 into your Database
                            payment.setStatus("FAILED");
                            paymentRepository.save(payment);
                            return "{\"RspCode\":\"05\",\"Message\":\"Payment Failed\"}";
                        }
                    } else {
                        return "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
                    }
                } else {
                    return "{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}";
                }
            } else {
                return "{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}";
            }
        } catch (Exception e) {
            return "{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}";
        }

    }
}
