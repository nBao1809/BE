package tnb.project.restaurant.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequestDTO {
    private String sessionId;
    private Integer rating;
    private String content;
}
