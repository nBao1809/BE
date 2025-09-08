package tnb.project.restaurant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private int code;
    private String message;
}
