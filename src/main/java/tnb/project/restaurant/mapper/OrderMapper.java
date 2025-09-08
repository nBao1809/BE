package tnb.project.restaurant.mapper;

import tnb.project.restaurant.DTO.responses.OrderResponseDTO;
import tnb.project.restaurant.entities.Orders;
import tnb.project.restaurant.DTO.responses.OrderDetailResponseDTO;
import tnb.project.restaurant.entities.OrderDetail;
import tnb.project.restaurant.DTO.responses.KitchenOrderResponseDTO;
import tnb.project.restaurant.entities.Tables;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponseDTO mapOrderToDTO(Orders order, List<OrderDetail> orderDetails) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setAmount(order.getAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus());
        if (orderDetails != null) {
            List<OrderDetailResponseDTO> detailDTOs = orderDetails.stream()
                .map(OrderDetailMapper::mapToDTO)
                .collect(Collectors.toList());
            dto.setOrderDetails(detailDTOs);
        }
        return dto;
    }

    public static KitchenOrderResponseDTO mapOrderToKitchenDTO(Orders order, List<OrderDetail> orderDetails) {
        String tableName = null;
        if (order.getSession() != null && order.getSession().getTable() != null) {
            Tables table = order.getSession().getTable();
            tableName = table.getNumber() != null ? "Bàn " + table.getNumber() : String.valueOf(table.getId());
        }
        java.util.List<OrderDetailResponseDTO> detailDTOs = orderDetails != null ?
            orderDetails.stream().map(OrderDetailMapper::mapToDTO).collect(java.util.stream.Collectors.toList()) :
            java.util.Collections.emptyList();
        return new KitchenOrderResponseDTO(tableName, order.getStatus(), detailDTOs);
    }
}
