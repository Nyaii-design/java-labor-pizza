package hu.nje.pizza.controller;

import hu.nje.pizza.entity.Kategoria;
import hu.nje.pizza.repository.KategoriaRepository;
import hu.nje.pizza.repository.PizzaRepository;
import hu.nje.pizza.repository.RendelesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Adatbázis menü: a három tábla (kategoria, pizza, rendeles) adatai, Repository-kon keresztül. */
@Controller
public class AdatbazisController {

    private final KategoriaRepository kategoriaRepo;
    private final PizzaRepository pizzaRepo;
    private final RendelesRepository rendelesRepo;

    public AdatbazisController(KategoriaRepository kategoriaRepo, PizzaRepository pizzaRepo, RendelesRepository rendelesRepo) {
        this.kategoriaRepo = kategoriaRepo;
        this.pizzaRepo = pizzaRepo;
        this.rendelesRepo = rendelesRepo;
    }

    @GetMapping("/adatbazis")
    public String adatbazis(@RequestParam(required = false) String kategoria, Model model) {
        List<Kategoria> kategoriak = kategoriaRepo.findAllByOrderByArAsc();

        // Kategóriánként hány pizza van
        Map<String, Long> pizzaSzam = new HashMap<>();
        for (Object[] sor : pizzaRepo.pizzaSzamKategoriankent()) {
            pizzaSzam.put((String) sor[0], ((Number) sor[1]).longValue());
        }

        boolean szurt = kategoria != null && !kategoria.isBlank();
        model.addAttribute("kategoriak", kategoriak);
        model.addAttribute("pizzaSzam", pizzaSzam);
        model.addAttribute("valasztott", szurt ? kategoria : "");
        model.addAttribute("pizzak", szurt ? pizzaRepo.findByKategoriaNevOrderByNevAsc(kategoria)
                                            : pizzaRepo.findAllByOrderByNevAsc());
        model.addAttribute("rendelesek", rendelesRepo.findTop20ByOrderByFelvetelDesc());
        model.addAttribute("rendelesSzam", rendelesRepo.count());
        return "adatbazis";
    }
}
