package hu.nje.pizza.controller;

import hu.nje.pizza.dto.PizzaDto;
import hu.nje.pizza.repository.PizzaRepository;
import hu.nje.pizza.service.PizzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RESTful API a pizza táblához (JSON).
 *  GET    /api/pizzak          – összes pizza
 *  GET    /api/pizzak/{nev}    – egy pizza
 *  POST   /api/pizzak          – új pizza
 *  PUT    /api/pizzak/{nev}    – módosítás (ha nincs ilyen, létrehozza)
 *  DELETE /api/pizzak/{nev}    – törlés
 */
@RestController
@RequestMapping("/api/pizzak")
public class RestApiController {

    private final PizzaRepository pizzaRepo;
    private final PizzaService pizzaService;

    public RestApiController(PizzaRepository pizzaRepo, PizzaService pizzaService) {
        this.pizzaRepo = pizzaRepo;
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public List<PizzaDto> olvasMind() {
        return pizzaRepo.findAllByOrderByNevAsc().stream().map(PizzaDto::of).toList();
    }

    @GetMapping("/{nev}")
    public PizzaDto olvasEgy(@PathVariable String nev) {
        return pizzaService.keres(nev).map(PizzaDto::of)
                .orElseThrow(() -> new PizzaNotFoundException(nev));
    }

    @PostMapping
    public ResponseEntity<?> uj(@RequestBody PizzaDto adat) {
        if (adat.nev() == null || adat.nev().isBlank() || adat.kategoria() == null) {
            return ResponseEntity.badRequest().body("A nev és a kategoria megadása kötelező.");
        }
        if (pizzaService.letezik(adat.nev())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Már van ilyen nevű pizza: " + adat.nev());
        }
        try {
            var p = pizzaService.ment(adat.nev().strip(), adat.kategoria(), adat.veg());
            return ResponseEntity.status(HttpStatus.CREATED).body(PizzaDto.of(p));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{nev}")
    public ResponseEntity<?> modosit(@PathVariable String nev, @RequestBody PizzaDto adat) {
        if (adat.kategoria() == null) {
            return ResponseEntity.badRequest().body("A kategoria megadása kötelező.");
        }
        try {
            return ResponseEntity.ok(PizzaDto.of(pizzaService.ment(nev, adat.kategoria(), adat.veg())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{nev}")
    public ResponseEntity<Void> torol(@PathVariable String nev) {
        if (!pizzaService.letezik(nev)) {
            throw new PizzaNotFoundException(nev);
        }
        pizzaService.torol(nev);
        return ResponseEntity.noContent().build();
    }
}
