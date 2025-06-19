package com.E_commerce.services;

import com.E_commerce.dao.LoginRepo;
import com.E_commerce.entity.TblUser;
import com.E_commerce.entity.Token;
import com.E_commerce.enums.HttpStatusCode;
import com.E_commerce.enums.Role;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import com.E_commerce.model.response.AppHttpStatus;
import com.E_commerce.utils.TokenUtility;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.E_commerce.enums.HttpStatusCode.CREATED;


@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Autowired
    private LoginRepo loginRepo;
    @Autowired
    private TokenUtility tokenUtility;

    public UserService(TokenService tokenService, EmailService emailService) {
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginRepo.findByUsername(username);
    }

    @Override
    @Transactional
    public ResponseEntity<?> userRegistration(Users user) {
        loginRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail())
                .ifPresent(existingUser -> {
                    throw new IllegalStateException("User already exists");
                });

        TblUser tblUser = new TblUser();
        tblUser.setPassword(passwordEncoder.encode(user.getPassword()));
        tblUser.setRole(Role.USER);
        tblUser.setFirstName(user.getFirstName());
        tblUser.setLastName(user.getLastName());
        tblUser.setEmail(user.getEmail());
        tblUser.setUsername(user.getUsername());
        tblUser.setEnabled(false);
        tblUser.setLocked(false);
        tblUser = loginRepo.save(tblUser);

        tokenUtility.tokenConfirmation(tblUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new AppHttpStatus(
                        HttpStatusCode.OK,
                        HttpStatus.OK,CREATED));
    }

    @SneakyThrows
    @Override
    public ResponseEntity<?> resetPassword(UserRequest request) {
        String token = request.getToken();

        Token confirmationToken = tokenService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        // Check if token is expired or already confirmed
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Token expired", HttpStatus.BAD_REQUEST);
        }
        if (confirmationToken.getConfirmedAt() != null) {
            return new ResponseEntity<>("Token already used", HttpStatus.BAD_REQUEST);
        }

        // Update the user's password
        TblUser user = confirmationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        loginRepo.save(user);

        // Mark token as confirmed AFTER successful password reset
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenService.save(confirmationToken);

        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> userLogin(Login request) {
        if (request.getUsername() == null || request.getPassword() == null
                || request.getUsername().isEmpty() || request.getPassword().isEmpty()) {
            return new ResponseEntity<>("Username or Password cannot be null", HttpStatus.BAD_REQUEST);
        }
        TblUser user = loginRepo.findByUsername(request.getUsername());

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        if (user.getUsername().equals(request.getUsername()) &&
                passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        TblUser tblUser = loginRepo.findUserByEmail(email);

        if (tblUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        String token = UUID.randomUUID().toString();
        Token confirmationToken = new Token(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                tblUser
        );
        tokenService.save(confirmationToken);
        emailService.sendSimpleMail(tblUser.getEmail(), token);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    public void enableUser(TblUser user) {
        user.setEnabled(true);
        loginRepo.save(user);
    }

    @Transactional
    public void confirmToken(String token) {
        Token confirmationToken = tokenService.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("User already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

//        confirmationToken.setConfirmedAt(LocalDateTime.now());
//        tokenService.save(confirmationToken);

        enableUser(confirmationToken.getUser());
    }
}
