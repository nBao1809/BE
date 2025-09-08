package tnb.project.restaurant.DTO;

import lombok.Data;
import tnb.project.restaurant.entities.OptionDetail;
import java.util.List;

@Data
public class OptionTypeWithDetailsDTO {
    private Long id;
    private String content;
    private List<OptionDetail> optionDetails;
}

