package hu.nje.pizza.controller;

import hu.nje.pizza.repository.KategoriaRepository;
import hu.nje.pizza.repository.PizzaRepository;
import hu.nje.pizza.repository.RendelesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FooldalController {

    private final PizzaRepository pizzaRepo;
    private final KategoriaRepository kategoriaRepo;
    private final RendelesRepository rendelesRepo;

    public FooldalController(PizzaRepository pizzaRepo, KategoriaRepository kategoriaRepo, RendelesRepository rendelesRepo) {
        this.pizzaRepo = pizzaRepo;
        this.kategoriaRepo = kategoriaRepo;
        this.rendelesRepo = rendelesRepo;
    }

    @GetMapping("/")
    public String fooldal(Model model) {
        model.addAttribute("pizzaSzam", pizzaRepo.count());
        model.addAttribute("kategoriaSzam", kategoriaRepo.count());
        model.addAttribute("rendelesSzam", rendelesRepo.count());
        model.addAttribute("kategoriak", kategoriaRepo.findAllByOrderByArAsc());
        return "index";
    }
}
