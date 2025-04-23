package com.E_commerce.controllers;

import com.E_commerce.entity.UserLogin;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import com.E_commerce.model.response.ApiResponse;
import com.E_commerce.model.response.ApiResponseErrorItem;
import com.E_commerce.model.response.UserResponse;
import com.E_commerce.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private IUserService iUserService;


    @PostMapping("/register-user")
    ResponseEntity<?> registerUser(@RequestBody Users users) {
        return iUserService.registerUser(users);
    }

    // Endpoint to create a user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserLogin user) {
        return iUserService.userRegistration(user);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserRequest request) {
        return iUserService.resetPassword(request);
    }

    @PostMapping("/user-login")
    public ResponseEntity<?> login(@RequestBody Login request) {
        return iUserService.userLogin(request);
    }
}
