package com.E_commerce.services;

import com.E_commerce.dao.LoginRepo;
import com.E_commerce.dao.UserRepository;
import com.E_commerce.entity.TblUsers;
import com.E_commerce.entity.UserLogin;
import com.E_commerce.exception.UserAlreadyTakenException;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import com.E_commerce.model.response.UserResponse;
import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginRepo loginRepo;

    @SneakyThrows
    @Override
    public ResponseEntity<?> registerUser(Users users) {

        TblUsers userExistence = userRepository.findByEmail(users.getEmail());
        if (userExistence != null) {
            throw new UserAlreadyTakenException();
        }
        TblUsers tblUsers = TblUsers.builder()
                .firstName(users.getFirstName())
                .middleName(users.getMiddleName())
                .lastName(users.getLastName())
                .gender(users.getGender())
                .email(users.getEmail())
                .password(users.getPassword())
                .mobileNo(users.getMobileNo())
                .dob(users.getDob())
                .address(users.getAddress())
                .build();

        userRepository.save(tblUsers);

        UserResponse userResponse = UserResponse.builder()
                .firstName(tblUsers.getFirstName())
                .middleName(tblUsers.getMiddleName())
                .lastName(tblUsers.getLastName())
                .gender(tblUsers.getGender())
                .email(tblUsers.getEmail())
                .mobileNo(tblUsers.getMobileNo())
                .dob(tblUsers.getDob())
                .address(tblUsers.getAddress())
                .build();

        return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> userRegistration(UserLogin user) {
        UserLogin existingUser = loginRepo.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserLogin savedUser = loginRepo.save(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<?> resetPassword(UserRequest request) {
        UserLogin user = loginRepo.findByUsername(request.getUsername());

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        loginRepo.save(user);

        return new ResponseEntity<>("Password Updated", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> userLogin(Login request) {
        if(request.getUsername() == null || request.getPassword() == null
                || request.getUsername().isEmpty() || request.getPassword().isEmpty()){
            return new ResponseEntity<>("Username or Password cannot be null", HttpStatus.BAD_REQUEST);
        }
        UserLogin user = loginRepo.findByUsername(request.getUsername());

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if(user.getUsername().equals(request.getUsername()) &&
                passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

}
