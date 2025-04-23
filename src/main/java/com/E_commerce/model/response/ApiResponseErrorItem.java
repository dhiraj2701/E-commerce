package com.E_commerce.model.response;

import com.E_commerce.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseErrorItem {
    private String reason;
    private Integer code;
    private String message;
    private Map<String, Serializable> data;

    public static ApiResponseErrorItem from(Exception exception){
        return ApiResponseErrorItem.builder()
                .reason(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }

    public static ApiResponseErrorItem from(BaseException exception){
        String reason = exception.getClass().getSimpleName();
        Integer code = null;

        if(exception.getCode() != null){
            reason = exception.getCode().getReason();
            code = exception.getCode().getCode();
        }

        return ApiResponseErrorItem.builder()
                .reason(reason)
                .code(code)
                .message(exception.getMessage())
                .data(exception.getData())
                .build();
    }
}

