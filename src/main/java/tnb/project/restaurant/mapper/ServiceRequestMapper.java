package tnb.project.restaurant.mapper;

import tnb.project.restaurant.DTO.responses.ServiceResponseDTO;
import tnb.project.restaurant.entities.ServiceRequest;

public class ServiceRequestMapper {
    public static ServiceResponseDTO toDTO(ServiceRequest entity) {
        if (entity == null) return null;
        ServiceResponseDTO dto = new ServiceResponseDTO();
        dto.setId(entity.getId());
        dto.setSessionId(entity.getSession() != null ? entity.getSession().getId() : null);
        dto.setServiceTypeId(entity.getServiceType() != null ? entity.getServiceType().getId() : null);
        dto.setCreatedAt(entity.getCreated_at());
        dto.setHandleTime(entity.getHandleTime());
        dto.setStatus(entity.getStatus());
        dto.setServiceTypeName(entity.getServiceType() != null ? entity.getServiceType().getTypeName() : null);
        if (entity.getSession() != null && entity.getSession().getTable() != null) {
            dto.setTableNumber(entity.getSession().getTable().getNumber());
        }
        return dto;
    }
}
