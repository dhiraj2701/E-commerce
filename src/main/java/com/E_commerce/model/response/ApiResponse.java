package com.E_commerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<TDATA> {
    private String id;
    private TDATA data;
    private List<ApiResponseError> error;
}
