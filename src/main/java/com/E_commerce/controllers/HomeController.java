package com.E_commerce.controllers;

import com.E_commerce.entity.UserLogin;
import com.E_commerce.model.Login;
import com.E_commerce.model.UserRequest;
import com.E_commerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;

    // Show registration form
    @GetMapping("/register")
    public String showForm(Model model) {
        model.addAttribute("user", new UserLogin());
        return "index";
    }

    // Handle registration submission
    @PostMapping("/register")
    public String submitForm(@Valid @ModelAttribute("user") UserLogin user,
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
    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(UserRequest request,
                                      RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = userService.resetPassword(request);

        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addAttribute("success", true);
        } else {
            redirectAttributes.addAttribute("error", true);
        }

        return "redirect:/reset-password";
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

    @GetMapping("/style.css")
    @ResponseBody
    public Resource getCss() {
        return new ClassPathResource("static/style.css");
    }
}
