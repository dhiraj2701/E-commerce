package com.E_commerce.utils;

import com.E_commerce.entity.TblUser;
import com.E_commerce.entity.Token;
import com.E_commerce.model.Users;
import com.E_commerce.services.EmailService;
import com.E_commerce.services.TokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenUtility {

    private final TokenService tokenService;
    private final EmailService emailService;

    public TokenUtility(TokenService tokenService, EmailService emailService) {
        this.tokenService = tokenService;
        this.emailService = emailService;
    }
    /**
     * Generates a confirmation token for the user and sends it via email.
     *
     * @param tblUser The user for whom the token is generated.
     */
    public void tokenConfirmation(TblUser tblUser) {

        String token = UUID.randomUUID().toString();
        Token confirmationToken = new Token(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                tblUser
        );
        tokenService.save(confirmationToken);
        emailService.sendSimpleMail(tblUser.getEmail(), token);
    }
}
