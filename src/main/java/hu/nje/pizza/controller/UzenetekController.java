package hu.nje.pizza.controller;

import hu.nje.pizza.repository.UzenetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Üzenetek menü (csak bejelentkezve): az elküldött üzenetek, a legfrissebb elöl. */
@Controller
public class UzenetekController {

    private final UzenetRepository uzenetRepo;

    public UzenetekController(UzenetRepository uzenetRepo) {
        this.uzenetRepo = uzenetRepo;
    }

    @GetMapping("/uzenetek")
    public String uzenetek(Model model) {
        model.addAttribute("uzenetek", uzenetRepo.findAllByOrderByKuldesIdejeDesc());
        return "uzenetek";
    }
}
