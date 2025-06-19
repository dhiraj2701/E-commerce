package com.E_commerce.services;

import com.E_commerce.entity.TblUser;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    ResponseEntity<?> userRegistration(Users user);

    ResponseEntity<?> resetPassword(UserRequest request);

    ResponseEntity<?> userLogin(Login request);

    ResponseEntity<?> forgotPassword(String email);
}
