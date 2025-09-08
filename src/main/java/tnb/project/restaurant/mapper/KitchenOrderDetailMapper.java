package tnb.project.restaurant.mapper;

import tnb.project.restaurant.DTO.responses.KitchenOrderDetailDTO;
import tnb.project.restaurant.entities.OrderDetail;
import tnb.project.restaurant.entities.Orders;
import tnb.project.restaurant.entities.Tables;

public class KitchenOrderDetailMapper {
    public static KitchenOrderDetailDTO mapToDTO(OrderDetail detail, Orders order) {
        String tableName = null;
        if (order.getSession() != null && order.getSession().getTable() != null) {
            Tables table = order.getSession().getTable();
            tableName = table.getNumber() != null ? "Bàn " + table.getNumber() : String.valueOf(table.getId());
        }
        return new KitchenOrderDetailDTO(
            detail.getId(),
            detail.getDish() != null ? detail.getDish().getName() : null,
            detail.getQuantity(),
            detail.getNote(),
            detail.getStatus(),
            tableName,
            order.getId(),
            order.getStatus()
        );
    }
}

