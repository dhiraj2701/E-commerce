package com.E_commerce.controllers;

import com.E_commerce.entity.TblUser;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import com.E_commerce.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
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

    @GetMapping("/forgot-password")
    public ResponseEntity<?>forgotPassword(@RequestParam String email) {
        return iUserService.forgotPassword(email);
    }
}
