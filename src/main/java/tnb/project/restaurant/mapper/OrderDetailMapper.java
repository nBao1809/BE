package tnb.project.restaurant.mapper;

import tnb.project.restaurant.DTO.responses.OrderDetailResponseDTO;
import tnb.project.restaurant.entities.OrderDetail;

public class OrderDetailMapper {
    public static OrderDetailResponseDTO mapToDTO(OrderDetail orderDetail) {
        OrderDetailResponseDTO dto = new OrderDetailResponseDTO();
        dto.setId(orderDetail.getId());
        dto.setDishId(orderDetail.getDish().getId());
        dto.setDishName(orderDetail.getDish().getName());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setNote(orderDetail.getNote());
        dto.setStatus(orderDetail.getStatus());
        if (orderDetail.getDish() != null && orderDetail.getDish().getPrice() != null) {
            dto.setPrice(orderDetail.getDish().getPrice());
        } else {
            dto.setPrice(null);
        }
        return dto;
    }
}
