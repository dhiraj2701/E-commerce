package com.E_commerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private boolean locked;

}
