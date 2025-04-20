package com.E_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private BigInteger id;
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
