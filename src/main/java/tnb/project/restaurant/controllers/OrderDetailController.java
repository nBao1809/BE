package tnb.project.restaurant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tnb.project.restaurant.DTO.requests.UpdateOrderDetailQuantityStatusDTO;
import tnb.project.restaurant.DTO.responses.KitchenOrderDetailDTO;
import tnb.project.restaurant.entities.OrderDetail;
import tnb.project.restaurant.services.OrderDetailService;
import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    private final OrderDetailService orderDetailService;

    @GetMapping
    ResponseEntity<List<OrderDetail>> getOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails();
        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping("/{orderDetailId}")
    ResponseEntity<OrderDetail> getOrderDetail(@PathVariable Long orderDetailId) {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(orderDetailId);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping
    ResponseEntity<OrderDetail> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        OrderDetail createdOrderDetail = orderDetailService.createOrderDetail(orderDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDetail);
    }

    @PatchMapping("/{orderDetailId}")
    ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable Long orderDetailId, @RequestBody OrderDetail orderDetail) {
        OrderDetail updatedOrderDetail = orderDetailService.updateOrderDetail(orderDetailId, orderDetail);
        return ResponseEntity.ok(updatedOrderDetail);
    }

    @PatchMapping("/{orderDetailId}/quantity-status")
    public ResponseEntity<KitchenOrderDetailDTO> updateQuantityAndStatus(@PathVariable Long orderDetailId, @RequestBody UpdateOrderDetailQuantityStatusDTO dto) {
        KitchenOrderDetailDTO updated = orderDetailService.updateQuantityAndStatus(orderDetailId, dto.getQuantity(), dto.getStatus());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{orderDetailId}")
    ResponseEntity<Void> deleteOrderDetail(@PathVariable Long orderDetailId) {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ResponseEntity.noContent().build();
    }
}
