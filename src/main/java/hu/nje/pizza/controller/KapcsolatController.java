package hu.nje.pizza.controller;

import hu.nje.pizza.dto.KapcsolatUrlap;
import hu.nje.pizza.entity.Uzenet;
import hu.nje.pizza.repository.UzenetRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

/** Kapcsolat menü: üzenetküldő űrlap szerver oldali validációval; az üzenet az adatbázisba kerül. */
@Controller
public class KapcsolatController {

    private final UzenetRepository uzenetRepo;

    public KapcsolatController(UzenetRepository uzenetRepo) {
        this.uzenetRepo = uzenetRepo;
    }

    @GetMapping("/kapcsolat")
    public String urlap(Model model) {
        model.addAttribute("urlap", new KapcsolatUrlap());
        return "kapcsolat";
    }

    @PostMapping("/kapcsolat")
    public String kuldes(@Valid @ModelAttribute("urlap") KapcsolatUrlap urlap, BindingResult hibak,
                         Authentication auth, RedirectAttributes ra) {
        if (hibak.hasErrors()) {
            return "kapcsolat"; // vissza az űrlapra a hibaüzenetekkel
        }
        Uzenet u = new Uzenet();
        u.setNev(urlap.getNev().strip());
        u.setEmail(urlap.getEmail().strip());
        u.setTargy(urlap.getTargy().strip());
        u.setSzoveg(urlap.getSzoveg().strip());
        u.setKuldesIdeje(LocalDateTime.now());
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            u.setFelhasznalo(auth.getName());
        }
        uzenetRepo.save(u);
        ra.addFlashAttribute("siker", "Köszönjük, az üzenetedet megkaptuk!");
        return "redirect:/kapcsolat";
    }
}
