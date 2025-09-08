package tnb.project.restaurant.mapper;

import tnb.project.restaurant.DTO.SessionDTO;
import tnb.project.restaurant.entities.Session;

public class SessionMapper {
    public static SessionDTO toDTO(Session session) {
        if (session == null) return null;
        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setTableId(session.getTable() != null ? session.getTable().getId() : null);
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setStatus(session.getStatus());
        return dto;
    }
}

