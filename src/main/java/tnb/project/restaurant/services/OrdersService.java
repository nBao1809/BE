package tnb.project.restaurant.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.PageResponse;
import tnb.project.restaurant.DTO.requests.OrderDetailRequestDTO;
import tnb.project.restaurant.DTO.requests.OrderRequestDTO;
import tnb.project.restaurant.DTO.responses.KitchenOrderDetailDTO;
import tnb.project.restaurant.DTO.responses.KitchenOrderResponseDTO;
import tnb.project.restaurant.DTO.responses.OrderDetailResponseDTO;
import tnb.project.restaurant.DTO.responses.OrderResponseDTO;
import tnb.project.restaurant.entities.*;
import tnb.project.restaurant.mapper.OrderDetailMapper;
import tnb.project.restaurant.repositorires.*;
import tnb.project.restaurant.mapper.OrderMapper;
import tnb.project.restaurant.mapper.KitchenOrderDetailMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final SessionRepository sessionRepository;
    private final DishRepository dishRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrdersService(OrdersRepository ordersRepository, OrderDetailRepository orderDetailRepository, SessionRepository sessionRepository, DishRepository dishRepository, OptionDetailRepository optionDetailRepository, SimpMessagingTemplate messagingTemplate) {
        this.ordersRepository = ordersRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.sessionRepository = sessionRepository;
        this.dishRepository = dishRepository;
        this.optionDetailRepository = optionDetailRepository;
        this.messagingTemplate = messagingTemplate;
    }


    public Orders updateOrder(String orderId, Orders order) {
        Orders existing = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Chỉ cập nhật session nếu được truyền lên
        if (order.getSession() != null) {
            existing.setSession(order.getSession());
        }
        // Chỉ cập nhật status nếu được truyền lên
        if (order.getStatus() != null) {
            existing.setStatus(order.getStatus());
        }
        // Tính lại tổng tiền dựa trên OrderDetail
        List<OrderDetail> details = getOrderDetailsByOrder(orderId);
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetail detail : details) {
            if (detail.getStatus() != null) {
                detail.setQuantity(0);
                continue; // Không cộng tiền món này
            }
            if (detail.getDish() != null && detail.getDish().getPrice() != null && detail.getQuantity() != null) {
                total = total.add(detail.getDish().getPrice().multiply(new BigDecimal(detail.getQuantity())));
            }
        }
        existing.setTotalAmount(total);
        Orders saved = ordersRepository.save(existing);
        // Lưu lại các order detail nếu có thay đổi quantity
        orderDetailRepository.saveAll(details);
        return saved;
    }

    public void deleteOrder(String orderId) {
        ordersRepository.deleteById(orderId);
    }

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        // Validate session
        Session session = sessionRepository.findById(orderRequest.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        Orders order = new Orders();
        order.setSession(session);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("ORDERING");

        BigDecimal amount = BigDecimal.ZERO;
        List<OrderDetail> savedDetails = new ArrayList<>();
        for (OrderDetailRequestDTO detailDTO : orderRequest.getOrderDetails()) {
            Dish dish = dishRepository.findById(detailDTO.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found: " + detailDTO.getDishId()));
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setDish(dish);
            orderDetail.setQuantity(detailDTO.getQuantity());
            // Ghép nội dung option vào note
            String baseNote = detailDTO.getNote() != null ? detailDTO.getNote() : "";
            String optionNote = "";
            if (detailDTO.getOptionDetailIds() != null && !detailDTO.getOptionDetailIds().isEmpty()) {
                List<String> optionContents = new ArrayList<>();
                for (Long optId : detailDTO.getOptionDetailIds()) {
                    OptionDetail option = optionDetailRepository.findById(optId)
                            .orElseThrow(() -> new RuntimeException("Option not found: " + optId));
                    optionContents.add(option.getContent());
                }
                optionNote = String.join("; ", optionContents);
            }
            String finalNote = baseNote;
            if (!baseNote.isEmpty() && !optionNote.isEmpty()) {
                finalNote += "; " + optionNote;
            } else if (!optionNote.isEmpty()) {
                finalNote = optionNote;
            }
            orderDetail.setNote(finalNote);
            amount = amount.add(dish.getPrice().multiply(BigDecimal.valueOf(detailDTO.getQuantity())));
            savedDetails.add(orderDetail);
        }
        order.setAmount(amount);
        order.setTotalAmount(amount);
        Orders savedOrder = ordersRepository.save(order);
        for (OrderDetail detail : savedDetails) {
            detail.setStatus("PREPARING");
            detail.setOrder(savedOrder);
            orderDetailRepository.save(detail);
        }
        // Map sang DTO trả về
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(savedOrder.getId());
        List<KitchenOrderDetailDTO> detailDTOs = orderDetails.stream()
                .map(od -> KitchenOrderDetailMapper.mapToDTO(od, savedOrder))
                .toList();
        OrderResponseDTO responseDTO = OrderMapper.mapOrderToDTO(savedOrder, orderDetails);
        // Gửi WS cho kitchen theo topic /topic/kitchen/order-detail (không phân biệt session)
        messagingTemplate.convertAndSend("/topic/kitchen/order-detail", detailDTOs);
        return responseDTO;
    }

    public List<OrderResponseDTO> getOrderDTOs() {
        List<Orders> orders = ordersRepository.findAll();
        List<String> orderIds = orders.stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderDetail> allDetails = orderDetailRepository.findByOrderIdIn(orderIds);
        Map<String, List<OrderDetail>> detailsByOrderId = allDetails.stream().collect(Collectors.groupingBy(od -> od.getOrder().getId()));
        return orders.stream().map(order -> {
            List<OrderDetail> details = detailsByOrderId.getOrDefault(order.getId(), List.of());
            return OrderMapper.mapOrderToDTO(order, details);
        }).collect(Collectors.toList());
    }
    public PageResponse<OrderResponseDTO> getOrderPageDTOs(Integer page, Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Orders> ordersPage = ordersRepository.findAll(pageable);
            List<String> orderIds = ordersPage.getContent().stream().map(Orders::getId).collect(Collectors.toList());
            List<OrderDetail> allDetails = orderDetailRepository.findByOrderIdIn(orderIds);
            Map<String, List<OrderDetail>> detailsByOrderId = allDetails.stream()
                .collect(Collectors.groupingBy(od -> od.getOrder().getId()));
            List<OrderResponseDTO> content = ordersPage.getContent().stream().map(order -> {
                List<OrderDetail> details = detailsByOrderId.getOrDefault(order.getId(), List.of());
                return OrderMapper.mapOrderToDTO(order, details);
            }).toList();
            return new PageResponse<>(content, ordersPage.getNumber(), ordersPage.getSize(), ordersPage.getTotalElements(), ordersPage.getTotalPages());
        } else {
            List<OrderResponseDTO> content = getOrderDTOs();
            return new PageResponse<>(content, 0, content.size(), content.size(), 1);
        }
    }
    public OrderResponseDTO getOrderDTO(String orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderDetail> details = orderDetailRepository.findByOrderId(order.getId());
        return OrderMapper.mapOrderToDTO(order, details);
    }

    public List<OrderDetail> getOrderDetailsByOrder(String orderId) {
        if (!ordersRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng với id: " + orderId);
        }
        return orderDetailRepository.findByOrderId(orderId);
    }

    public OrderResponseDTO addOrderDetails(String orderId, List<OrderDetailRequestDTO> orderDetails) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với id: " + orderId));
        // Lấy danh sách OrderDetail đã tồn tại
        List<OrderDetail> existingDetails = orderDetailRepository.findByOrderId(orderId);
        // Map note -> OrderDetail để dễ tra cứu
        Map<String, OrderDetail> noteToDetail = existingDetails.stream()
                .filter(od -> od.getNote() != null && !od.getNote().isEmpty())
                .collect(Collectors.toMap(OrderDetail::getNote, od -> od, (a, b) -> a));
        List<OrderDetail> newDetails = new ArrayList<>();
        for (OrderDetailRequestDTO detailDTO : orderDetails) {
            Dish dish = dishRepository.findById(detailDTO.getDishId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn với id: " + detailDTO.getDishId()));
            // Compose note with options
            String baseNote = detailDTO.getNote() != null ? detailDTO.getNote() : "";
            String optionNote = "";
            if (detailDTO.getOptionDetailIds() != null && !detailDTO.getOptionDetailIds().isEmpty()) {
                List<String> optionContents = new ArrayList<>();
                for (Long optId : detailDTO.getOptionDetailIds()) {
                    OptionDetail option = optionDetailRepository.findById(optId)
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tuỳ chọn với id: " + optId));
                    optionContents.add(option.getContent());
                }
                optionNote = String.join(", ", optionContents);
            }
            String finalNote = baseNote;
            if (!baseNote.isEmpty() && !optionNote.isEmpty()) {
                finalNote += "; " + optionNote;
            } else if (!optionNote.isEmpty()) {
                finalNote = optionNote;
            }
            // Nếu note đã tồn tại thì tăng số lượng
            if (noteToDetail.containsKey(finalNote)) {
                OrderDetail existDetail = noteToDetail.get(finalNote);
                existDetail.setQuantity(existDetail.getQuantity() + detailDTO.getQuantity());
                newDetails.add(existDetail);
            } else {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setDish(dish);
                orderDetail.setQuantity(detailDTO.getQuantity());
                orderDetail.setNote(finalNote);
                orderDetail.setStatus("PREPARING");
                newDetails.add(orderDetail);
            }
        }
        // Lưu lại các OrderDetail đã thay đổi hoặc mới
        for (OrderDetail detail : newDetails) {
            orderDetailRepository.save(detail);
        }
        // Tính lại tổng tiền
        List<OrderDetail> allDetails = orderDetailRepository.findByOrderId(order.getId());
        BigDecimal amount = BigDecimal.ZERO;
        for (OrderDetail od : allDetails) {
            if (od.getDish() != null && od.getDish().getPrice() != null && od.getQuantity() != null) {
                amount = amount.add(od.getDish().getPrice().multiply(BigDecimal.valueOf(od.getQuantity())));
            }
        }
        order.setAmount(amount);
        order.setTotalAmount(amount);
        ordersRepository.save(order);
        List<KitchenOrderDetailDTO> detailDTOs = allDetails.stream()
                .map(od -> KitchenOrderDetailMapper.mapToDTO(od, order))
                .toList();
        // Phát WS tới /topic/kitchen/order-detail (không phân biệt session)
        messagingTemplate.convertAndSend("/topic/kitchen/order-detail", detailDTOs);
        // Trả về DTO
        return OrderMapper.mapOrderToDTO(order, allDetails);
    }

//    public List<KitchenOrderResponseDTO> getKitchenOrders(String status, Long tableId, String sort) {
//        List<Orders> orders = ordersRepository.findAll();
//        // Lọc theo tableId nếu có
//        if (tableId != null) {
//            orders = orders.stream()
//                    .filter(o -> o.getSession() != null && o.getSession().getTable() != null &&
//                            tableId.equals(o.getSession().getTable().getId()))
//                    .collect(java.util.stream.Collectors.toList());
//        }
//        // Lọc theo status nếu có, nhưng luôn loại bỏ các order có status là "DONE"
//        if (status != null && !status.isEmpty()) {
//            orders = orders.stream()
//                    .filter(o -> status.equalsIgnoreCase(o.getStatus()))
//                    .filter(o -> o.getStatus() == null || !o.getStatus().equalsIgnoreCase("DONE"))
//                    .collect(java.util.stream.Collectors.toList());
//        } else {
//            // Nếu không truyền status, loại bỏ các order có status là "DONE"
//            orders = orders.stream()
//                    .filter(o -> o.getStatus() == null || !o.getStatus().equalsIgnoreCase("DONE"))
//                    .collect(java.util.stream.Collectors.toList());
//        }
//        // Sắp xếp theo sort (chỉ hỗ trợ createdAt, totalAmount)
//        if (sort != null && sort.equalsIgnoreCase("totalAmount")) {
//            orders.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));
//        } else {
//            orders.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
//        }
//        // Lấy toàn bộ orderId và batch fetch order detail
//        List<String> orderIds = orders.stream().map(Orders::getId).collect(Collectors.toList());
//        List<OrderDetail> allDetails = orderDetailRepository.findByOrderIdIn(orderIds);
//        Map<String, List<OrderDetail>> detailsByOrderId = allDetails.stream().collect(Collectors.groupingBy(od -> od.getOrder().getId()));
//        return orders.stream().map(order -> {
//            List<OrderDetail> details = detailsByOrderId.getOrDefault(order.getId(), List.of());
//            return OrderMapper.mapOrderToKitchenDTO(order, details);
//        }).collect(java.util.stream.Collectors.toList());
//    }

    public Map<String, List<KitchenOrderDetailDTO>> getKitchenOrderDetailsGroupedByStatus() {
        // Lấy tất cả orders, loại bỏ những order có status là null hoặc "SERVED"
        List<Orders> orders = ordersRepository.findAll().stream()
            .filter(o -> o.getStatus() != null && !o.getStatus().equalsIgnoreCase("DONE"))
            .toList();
        List<String> orderIds = orders.stream().map(Orders::getId).toList();
        List<OrderDetail> allDetails = orderDetailRepository.findByOrderIdIn(orderIds).stream().filter(od -> od.getStatus() != null && !od.getStatus().equalsIgnoreCase("SERVED")).toList();
        // Build a map of orderId to Orders for correct mapping
        Map<String, Orders> orderMap = orders.stream().collect(Collectors.toMap(Orders::getId, o -> o));
        // Map each OrderDetail to KitchenOrderDetailDTO using the correct Orders object
        List<KitchenOrderDetailDTO> dtos = allDetails.stream()
            .map(od -> KitchenOrderDetailMapper.mapToDTO(od, orderMap.get(od.getOrder().getId())))
            .toList();
        // Group by status
        return dtos.stream().collect(Collectors.groupingBy(KitchenOrderDetailDTO::getStatus));
    }

    public Orders updateStatus(String orderId, String status) {
        Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với id: " + orderId));
        order.setStatus(status);
        return ordersRepository.save(order);
    }

    public Orders updateWhenPayment(String orderId, String status, Long loyaltyRedeemPoints, BigDecimal discountAmount, BigDecimal totalAmount) {
        Orders order = ordersRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với id: " + orderId));
        if (status != null && !status.trim().isEmpty()) {
            order.setStatus(status);
        }
        if (loyaltyRedeemPoints != null) {
            if (loyaltyRedeemPoints < 0) throw new IllegalArgumentException("Điểm tích luỹ không hợp lệ");
            order.setLoyaltyRedeemPoints(loyaltyRedeemPoints);
        }
        if (discountAmount != null) {
            if (discountAmount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Chiết khấu không hợp lệ");
            order.setDiscountAmount(discountAmount);
        }
        if (totalAmount != null) {
            if (totalAmount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Tổng tiền không hợp lệ");
            order.setTotalAmount(totalAmount);
        }
        return ordersRepository.save(order);
    }

    public OrderResponseDTO getOrderDTOBySessionId(String sessionId) {
        Orders order = ordersRepository.findBySession_Id(sessionId)
            .stream().findFirst().orElse(null);
        if (order == null) return null;
        List<OrderDetail> details = orderDetailRepository.findByOrderId(order.getId());
        return OrderMapper.mapOrderToDTO(order, details);
    }
}
