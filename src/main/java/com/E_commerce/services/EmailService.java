package com.E_commerce.services;

import jakarta.mail.internet.MimeMessage;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendSimpleMail(String to, String token) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom("dhirajmandal60@gmail.com");
            helper.setSubject("Confirm your email");

            // Load HTML template
            Resource resource = new ClassPathResource("templates/email.html");
            String htmlContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Replace placeholder with actual token URL
            String verificationUrl = "http://localhost:8080/register/confirmToken?token=" + token;
            htmlContent = htmlContent.replace("{{VERIFICATION_LINK}}", verificationUrl);

            // Set HTML content
            helper.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalIdentifierException("Failed to send email");
        }
    }
}
