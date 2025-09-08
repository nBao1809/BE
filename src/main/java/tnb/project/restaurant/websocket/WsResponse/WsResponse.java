package tnb.project.restaurant.websocket.WsResponse;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsResponse<T> {
    private String type;
    @JsonUnwrapped
    private T data;
}

