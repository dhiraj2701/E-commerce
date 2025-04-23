package com.E_commerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseError {
    private String message;
    private int code;
    private List<ApiResponseErrorItem> errors;
}