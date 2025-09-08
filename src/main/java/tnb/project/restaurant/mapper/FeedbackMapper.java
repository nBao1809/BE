package tnb.project.restaurant.mapper;

import tnb.project.restaurant.entities.Feedback;
import tnb.project.restaurant.DTO.FeedbackResponseDTO;

public class FeedbackMapper {
    public static FeedbackResponseDTO toDTO(Feedback feedback) {
        FeedbackResponseDTO dto = new FeedbackResponseDTO();
        dto.setId(feedback.getId());
        dto.setSessionId(feedback.getSession() != null ? feedback.getSession().getId() : null);
        dto.setRating(feedback.getRating());
        dto.setContent(feedback.getContent());
        dto.setCreatedAt(feedback.getCreatedAt() != null ? feedback.getCreatedAt().toString() : null);
        return dto;
    }
}

