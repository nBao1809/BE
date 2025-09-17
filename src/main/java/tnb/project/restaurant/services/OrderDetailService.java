package tnb.project.restaurant.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tnb.project.restaurant.DTO.responses.KitchenOrderDetailDTO;
import tnb.project.restaurant.DTO.responses.OrderDetailResponseDTO;
import tnb.project.restaurant.entities.OrderDetail;

import java.util.List;

import tnb.project.restaurant.mapper.KitchenOrderDetailMapper;
import tnb.project.restaurant.mapper.OrderDetailMapper;
import tnb.project.restaurant.repositorires.OrderDetailRepository;
import tnb.project.restaurant.repositorires.OrdersRepository;
import tnb.project.restaurant.repositorires.DishRepository;
import tnb.project.restaurant.entities.Orders;
import tnb.project.restaurant.entities.Dish;


@Service
public class OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrdersRepository ordersRepository;
    private final DishRepository dishRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, OrdersRepository ordersRepository, DishRepository dishRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.orderDetailRepository = orderDetailRepository;
        this.ordersRepository = ordersRepository;
        this.dishRepository = dishRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public OrderDetail getOrderDetail(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết đơn hàng với id: " + orderDetailId));
    }

    public OrderDetail createOrderDetail(OrderDetail orderDetail) {
        // Validate and set Orders
        if (orderDetail.getOrder() != null && orderDetail.getOrder().getId() != null) {
            Orders orders = ordersRepository.findById(orderDetail.getOrder().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng"));
            orderDetail.setOrder(orders);
        } else {
            throw new IllegalArgumentException("Đơn hàng là bắt buộc");
        }
        // Validate and set Dish
        if (orderDetail.getDish() != null && orderDetail.getDish().getId() != null) {
            Dish dish = dishRepository.findById(orderDetail.getDish().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy món ăn"));
            orderDetail.setDish(dish);
        } else {
            throw new IllegalArgumentException("Món ăn là bắt buộc");
        }
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        return savedOrderDetail;
    }

    public OrderDetail updateOrderDetail(Long orderDetailId, OrderDetail orderDetail) {
        OrderDetail existing = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        if (orderDetail.getQuantity() != null) {
            existing.setQuantity(orderDetail.getQuantity());
        }
        if (orderDetail.getNote() != null) {
            existing.setNote(orderDetail.getNote());
        }
        if (orderDetail.getStatus() != null) {
            existing.setStatus(orderDetail.getStatus());
        }
        // Update Orders if provided
        if (orderDetail.getOrder() != null && orderDetail.getOrder().getId() != null) {
            Orders orders = ordersRepository.findById(orderDetail.getOrder().getId())
                    .orElseThrow(() -> new RuntimeException("Orders not found"));
            existing.setOrder(orders);
        }
        // Update Dish if provided
        if (orderDetail.getDish() != null && orderDetail.getDish().getId() != null) {
            Dish dish = dishRepository.findById(orderDetail.getDish().getId())
                    .orElseThrow(() -> new RuntimeException("Dish not found"));
            existing.setDish(dish);
        }
        return orderDetailRepository.save(existing);
    }


    public KitchenOrderDetailDTO updateQuantityAndStatus(Long orderDetailId, Integer quantity, String status) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chi tiết đơn hàng với id: " + orderDetailId));
        if (quantity != null) {
            if (quantity < 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn hoặc bằng 0");
            }
            orderDetail.setQuantity(quantity);
        }
        if (status != null) {
            if (status.trim().isEmpty()) {
                throw new IllegalArgumentException("Trạng thái không được để trống");
            }
            orderDetail.setStatus(status);
        }
        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        Orders parentOrder = savedOrderDetail.getOrder();
        KitchenOrderDetailDTO detailDTO = KitchenOrderDetailMapper.mapToDTO(savedOrderDetail, parentOrder);
        String sessionId = parentOrder.getSession() != null ? parentOrder.getSession().getId() : null;
        // Gửi WS cho khách theo topic /topic/session/{sessionId}/order-detail, chỉ gửi KitchenOrderDetailDTO
        simpMessagingTemplate.convertAndSend("/topic/session/" + sessionId + "/order-detail", detailDTO);
        return detailDTO;
    }

    public void deleteOrderDetail(Long orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }
}
