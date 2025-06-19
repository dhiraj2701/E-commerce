package com.E_commerce.controllers;

import com.E_commerce.dao.TokenRepository;
import com.E_commerce.entity.TblUser;
import com.E_commerce.entity.Token;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.model.Users;
import com.E_commerce.services.TokenService;
import com.E_commerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;
    @Autowired
    private TokenService tokenService;

    // Show registration form
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new TblUser());
        return "index";
    }

    // Handle registration submission
    @PostMapping("/register")
    public String submitForm(@Valid @ModelAttribute("user") Users user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "index";
        }

        ResponseEntity<?> response = userService.userRegistration(user);

        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            redirectAttributes.addAttribute("userExisted", true);
            return "redirect:/register";
        }

        redirectAttributes.addAttribute("success", true);
        return "redirect:/register";
    }
    // Controller method
    @GetMapping("/reset-password")
    public String showResetPasswordPage(
            @RequestParam(name = "token") String token,
            Model model
    ) {
        UserRequest userRequest = new UserRequest();
        userRequest.setToken(token);
        model.addAttribute("userRequest", userRequest);
        return "reset-password"; // Maps to reset-password.html
    }

    @GetMapping("/user-login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new Login());
        return "user-login"; // Assuming you have a `user-login.html` or similar
    }

    @PostMapping("/user-login")
    public String handleLogin(@ModelAttribute("user") Login user,
                              RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = userService.userLogin(user);

        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addAttribute("success", true);
            return "redirect:/home";
        } else {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/user-login?error=true";
        }
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";  // This maps to home.html
    }

    @GetMapping("/register/confirmToken")
    public String confirmToken(@RequestParam("token") String token, Model model) {
        try {
            userService.confirmToken(token);
            model.addAttribute("message", "Email verified successfully!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "confirmToken";  // Show success/error message on the page
    }

    @GetMapping("/style.css")
    @ResponseBody
    public Resource getCss() {
        return new ClassPathResource("static/style.css");
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @ModelAttribute UserRequest userRequest,
            RedirectAttributes redirectAttributes
    ) {
        ResponseEntity<?> response = userService.resetPassword(userRequest); // Updated to use UserRequest

        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("success", "Password reset successful!");
            return "redirect:/user-login";  // Redirect to login after success
        } else {
            redirectAttributes.addFlashAttribute("error", response.getBody());
            return "redirect:/reset-password?token=" + userRequest.getToken();  // Stay on form with error
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email,
                              RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = userService.forgotPassword(email);

        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("success", "Password reset link sent to your email!");
        } else {
            redirectAttributes.addFlashAttribute("error", response.getBody());
        }

        return "redirect:/forgot-password";
    }
    public ResponseEntity<?> confirmToken(String token) {
        // Decode the token from URL-safe encoding
        String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);

        // Pass the DECODED token to the service layer
        userService.confirmToken(decodedToken);

        // Redirect to the reset password page with the decoded token
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/reset-password?token=" + URLEncoder.encode(decodedToken, StandardCharsets.UTF_8)))
                .build();
    }
}
