package hu.nje.pizza.controller;

import hu.nje.pizza.repository.RendelesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/** Diagram menü: Chart.js diagramok az adatbázis adataiból. */
@Controller
public class DiagramController {

    private final RendelesRepository rendelesRepo;

    public DiagramController(RendelesRepository rendelesRepo) {
        this.rendelesRepo = rendelesRepo;
    }

    @GetMapping("/diagram")
    public String diagram(Model model) {
        // 1. A 10 legnépszerűbb pizza (eladott darab)
        List<String> topNevek = new ArrayList<>();
        List<Long> topDarab = new ArrayList<>();
        for (Object[] sor : rendelesRepo.legnepszerubbPizzak(PageRequest.of(0, 10))) {
            topNevek.add((String) sor[0]);
            topDarab.add(((Number) sor[1]).longValue());
        }

        // 2. Eladott darab kategóriánként
        List<String> katNevek = new ArrayList<>();
        List<Long> katDarab = new ArrayList<>();
        for (Object[] sor : rendelesRepo.eladottDarabKategoriankent()) {
            katNevek.add((String) sor[0]);
            katDarab.add(((Number) sor[1]).longValue());
        }

        // 3. Rendelések száma havonta
        List<String> honapok = new ArrayList<>();
        List<Long> haviDarab = new ArrayList<>();
        for (Object[] sor : rendelesRepo.rendelesekHavonta()) {
            honapok.add(sor[0] + "." + String.format("%02d", ((Number) sor[1]).intValue()));
            haviDarab.add(((Number) sor[2]).longValue());
        }

        model.addAttribute("topNevek", topNevek);
        model.addAttribute("topDarab", topDarab);
        model.addAttribute("katNevek", katNevek);
        model.addAttribute("katDarab", katDarab);
        model.addAttribute("honapok", honapok);
        model.addAttribute("haviDarab", haviDarab);
        return "diagram";
    }
}
