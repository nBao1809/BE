package tnb.project.restaurant.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackResponseDTO {
    private Long id;
    private String sessionId;
    private Integer rating;
    private String content;
    private String createdAt;

}

