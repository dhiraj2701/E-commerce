package com.E_commerce.services;

import com.E_commerce.entity.UserLogin;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    ResponseEntity<?> registerUser(Users users);

    ResponseEntity<?> userRegistration(UserLogin user);

    ResponseEntity<?> resetPassword(UserRequest request);

    ResponseEntity<?> userLogin(Login request);
}
