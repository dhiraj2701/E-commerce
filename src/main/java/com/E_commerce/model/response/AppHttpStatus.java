package com.E_commerce.model.response;

import com.E_commerce.enums.HttpStatusCode;  // Your custom HttpStatusCode enum
import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppHttpStatus {
    private int httpCode;                    // Numeric HTTP code (e.g., 200)
    private HttpStatus springHttpStatus;     // Spring's HttpStatus enum (e.g., HttpStatus.OK)
    private HttpStatusCode customStatus;     // Your custom HttpStatusCode enum (e.g., OK)
    private Object data;                     // Response payload (can be any object like user data, results, etc.)

    // Custom constructor for enum-based initialization
    public AppHttpStatus(HttpStatusCode customStatus,
                         HttpStatus springHttpStatus,
                         Object data) {
        this.httpCode = springHttpStatus.value();  // Set numeric HTTP code from Spring's enum
        this.springHttpStatus = springHttpStatus;  // Set Spring HttpStatus
        this.customStatus = customStatus;          // Set custom HttpStatusCode
        this.data = data;                          // Set the response payload (user data, etc.)
    }
}

