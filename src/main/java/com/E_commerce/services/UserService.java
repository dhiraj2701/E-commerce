package com.E_commerce.services;

import com.E_commerce.dao.LoginRepo;
import com.E_commerce.dao.UserRepository;
import com.E_commerce.entity.TblUsers;
import com.E_commerce.entity.UserLogin;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
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
            return new ResponseEntity<>("User Already Exist", HttpStatus.OK);
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
        return new ResponseEntity<>(tblUsers, HttpStatus.OK);
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
            throw new NotFoundException("User not found");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        loginRepo.save(user);

        return new ResponseEntity<>("Password Updated", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> userLogin(Login request) {
        UserLogin user = loginRepo.findByUsername(request.getUsername());
        if(user.getUsername().equals(request.getUsername()) &&
                passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

}
