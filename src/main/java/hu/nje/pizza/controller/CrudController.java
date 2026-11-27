package hu.nje.pizza.controller;

import hu.nje.pizza.dto.PizzaUrlap;
import hu.nje.pizza.entity.Pizza;
import hu.nje.pizza.repository.KategoriaRepository;
import hu.nje.pizza.repository.PizzaRepository;
import hu.nje.pizza.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD menü (csak admin): a pizza tábla listázása, felvétele, módosítása és törlése. */
@Controller
@RequestMapping("/crud")
public class CrudController {

    private final PizzaRepository pizzaRepo;
    private final KategoriaRepository kategoriaRepo;
    private final PizzaService pizzaService;

    public CrudController(PizzaRepository pizzaRepo, KategoriaRepository kategoriaRepo, PizzaService pizzaService) {
        this.pizzaRepo = pizzaRepo;
        this.kategoriaRepo = kategoriaRepo;
        this.pizzaService = pizzaService;
    }

    // ---- Read ----
    @GetMapping
    public String lista(Model model) {
        model.addAttribute("pizzak", pizzaRepo.findAllByOrderByNevAsc());
        return "crud/lista";
    }

    // ---- Create ----
    @GetMapping("/uj")
    public String ujUrlap(Model model) {
        model.addAttribute("urlap", new PizzaUrlap());
        model.addAttribute("uj", true);
        model.addAttribute("kategoriak", kategoriaRepo.findAllByOrderByArAsc());
        return "crud/urlap";
    }

    @PostMapping("/uj")
    public String uj(@Valid @ModelAttribute("urlap") PizzaUrlap urlap, BindingResult hibak,
                     Model model, RedirectAttributes ra) {
        if (!hibak.hasFieldErrors("nev") && pizzaService.letezik(urlap.getNev().strip())) {
            hibak.rejectValue("nev", "letezik", "Már van ilyen nevű pizza.");
        }
        if (hibak.hasErrors()) {
            model.addAttribute("uj", true);
            model.addAttribute("kategoriak", kategoriaRepo.findAllByOrderByArAsc());
            return "crud/urlap";
        }
        pizzaService.ment(urlap.getNev().strip(), urlap.getKategoriaNev(), urlap.isVegetarianus());
        ra.addFlashAttribute("uzenet", "Új pizza felvéve: " + urlap.getNev().strip());
        return "redirect:/crud";
    }

    // ---- Update ----
    @GetMapping("/modosit")
    public String modositUrlap(@RequestParam String nev, Model model, RedirectAttributes ra) {
        Pizza p = pizzaService.keres(nev).orElse(null);
        if (p == null) {
            ra.addFlashAttribute("hiba", "Nincs ilyen pizza: " + nev);
            return "redirect:/crud";
        }
        PizzaUrlap urlap = new PizzaUrlap();
        urlap.setNev(p.getNev());
        urlap.setKategoriaNev(p.getKategoria().getNev());
        urlap.setVegetarianus(p.isVegetarianus());
        model.addAttribute("urlap", urlap);
        model.addAttribute("uj", false);
        model.addAttribute("kategoriak", kategoriaRepo.findAllByOrderByArAsc());
        return "crud/urlap";
    }

    @PostMapping("/modosit")
    public String modosit(@Valid @ModelAttribute("urlap") PizzaUrlap urlap, BindingResult hibak,
                          Model model, RedirectAttributes ra) {
        if (hibak.hasErrors()) {
            model.addAttribute("uj", false);
            model.addAttribute("kategoriak", kategoriaRepo.findAllByOrderByArAsc());
            return "crud/urlap";
        }
        pizzaService.ment(urlap.getNev(), urlap.getKategoriaNev(), urlap.isVegetarianus());
        ra.addFlashAttribute("uzenet", "Módosítva: " + urlap.getNev());
        return "redirect:/crud";
    }

    // ---- Delete ----
    @PostMapping("/torol")
    public String torol(@RequestParam String nev, RedirectAttributes ra) {
        if (!pizzaService.letezik(nev)) {
            ra.addFlashAttribute("hiba", "Nincs ilyen pizza: " + nev);
            return "redirect:/crud";
        }
        int rendelesek = pizzaService.torol(nev);
        ra.addFlashAttribute("uzenet", "Törölve: " + nev + " (és " + rendelesek + " hozzá tartozó rendelés)");
        return "redirect:/crud";
    }
}
