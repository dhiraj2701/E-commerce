package com.E_commerce.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
    private BigInteger mobileNo;
    private String dob;
    private String address;
}
