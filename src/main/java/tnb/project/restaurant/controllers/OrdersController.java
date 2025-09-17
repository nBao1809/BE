package tnb.project.restaurant.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tnb.project.restaurant.entities.Orders;
import tnb.project.restaurant.services.OrdersService;
import tnb.project.restaurant.DTO.requests.OrderRequestDTO;
import tnb.project.restaurant.DTO.responses.OrderResponseDTO;
import tnb.project.restaurant.entities.OrderDetail;
import tnb.project.restaurant.mapper.OrderMapper;
import tnb.project.restaurant.DTO.requests.OrderDetailRequestDTO;
import tnb.project.restaurant.DTO.responses.KitchenOrderResponseDTO;
import tnb.project.restaurant.DTO.responses.KitchenOrderDetailDTO;
import tnb.project.restaurant.DTO.requests.UpdateStatusDTO;
import tnb.project.restaurant.DTO.PageResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }


    @GetMapping
    ResponseEntity<?> getOrders(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "size", required = false) Integer size) {
        if (page != null && size != null) {
            PageResponse<OrderResponseDTO> paged = ordersService.getOrderPageDTOs(page, size);
            return ResponseEntity.ok(paged);
        } else {
            List<OrderResponseDTO> orders = ordersService.getOrderDTOs();
            return ResponseEntity.ok(orders);
        }
    }

    @GetMapping("/{orderId}")
    ResponseEntity<OrderResponseDTO> getOrder(@PathVariable String orderId) {
        OrderResponseDTO order = ordersService.getOrderDTO(orderId);
        return ResponseEntity.ok(order);
    }
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO createdOrder = ordersService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }


    @PatchMapping("/{orderId}")
    ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable String orderId, @RequestBody Orders order) {
        Orders updatedOrder = ordersService.updateOrder(orderId, order);
        List<OrderDetail> details = ordersService.getOrderDetailsByOrder(updatedOrder.getId());
        OrderResponseDTO dto = OrderMapper.mapOrderToDTO(updatedOrder, details);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{orderId}")
    ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        ordersService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/order-details")
    public ResponseEntity<OrderResponseDTO> addOrderDetails(
            @PathVariable String orderId,
            @RequestBody List<OrderDetailRequestDTO> orderDetails) {
        OrderResponseDTO updatedOrder = ordersService.addOrderDetails(orderId, orderDetails);
        return ResponseEntity.ok(updatedOrder);
    }

//    @GetMapping("/kitchen")
//    public ResponseEntity<List<KitchenOrderResponseDTO>> getKitchenOrders(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) Long tableId,
//            @RequestParam(required = false, defaultValue = "createdAt") String sort
//    ) {
//        List<KitchenOrderResponseDTO> orders = ordersService.getKitchenOrders(status, tableId, sort);
//        return ResponseEntity.ok(orders);
//    }


    @GetMapping("/kitchen/order-details/group-by-status")
    public ResponseEntity<Map<String, List<KitchenOrderDetailDTO>>> getKitchenOrderDetailsGroupedByStatus() {
        Map<String, List<KitchenOrderDetailDTO>> grouped = ordersService.getKitchenOrderDetailsGroupedByStatus();
        return ResponseEntity.ok(grouped);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Orders> updateStatus(@PathVariable String orderId, @RequestBody UpdateStatusDTO dto) {
        Orders updated = ordersService.updateStatus(orderId, dto.getStatus());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/by-session/{sessionId}")
    public ResponseEntity<OrderResponseDTO> getOrderBySessionId(@PathVariable String sessionId) {
        OrderResponseDTO order = ordersService.getOrderDTOBySessionId(sessionId);
        return ResponseEntity.ok(order);
    }
}
