package hu.nje.pizza.controller;

import hu.nje.pizza.entity.User;
import hu.nje.pizza.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Bejelentkezés és regisztráció (a tanári Autentikáció-megoldás továbbfejlesztése). */
@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String password2, Model model) {
        username = username.strip();
        model.addAttribute("username", username);
        if (username.length() < 3) {
            model.addAttribute("error", "A felhasználónév legalább 3 karakter legyen!");
            return "register";
        }
        if (password.length() < 6) {
            model.addAttribute("error", "A jelszó legalább 6 karakter legyen!");
            return "register";
        }
        if (!password.equals(password2)) {
            model.addAttribute("error", "A két jelszó nem egyezik!");
            return "register";
        }
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Ez a felhasználónév már foglalt!");
            return "register";
        }
        userRepository.save(new User(username, passwordEncoder.encode(password), "USER"));
        return "redirect:/login?registered";
    }
}
