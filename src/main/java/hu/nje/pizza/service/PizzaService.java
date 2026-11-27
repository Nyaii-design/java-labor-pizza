package hu.nje.pizza.service;

import hu.nje.pizza.entity.Kategoria;
import hu.nje.pizza.entity.Pizza;
import hu.nje.pizza.repository.KategoriaRepository;
import hu.nje.pizza.repository.PizzaRepository;
import hu.nje.pizza.repository.RendelesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** Pizza-műveletek, amiket a CRUD menü és a REST API is használ. */
@Service
public class PizzaService {

    private final PizzaRepository pizzaRepo;
    private final KategoriaRepository kategoriaRepo;
    private final RendelesRepository rendelesRepo;

    public PizzaService(PizzaRepository pizzaRepo, KategoriaRepository kategoriaRepo, RendelesRepository rendelesRepo) {
        this.pizzaRepo = pizzaRepo;
        this.kategoriaRepo = kategoriaRepo;
        this.rendelesRepo = rendelesRepo;
    }

    public Optional<Pizza> keres(String nev) {
        return pizzaRepo.findById(nev);
    }

    public boolean letezik(String nev) {
        return pizzaRepo.existsById(nev);
    }

    public Kategoria kategoria(String nev) {
        return kategoriaRepo.findById(nev)
                .orElseThrow(() -> new IllegalArgumentException("Nincs ilyen kategória: " + nev));
    }

    /** Új pizza mentése vagy meglévő módosítása. */
    @Transactional
    public Pizza ment(String nev, String kategoriaNev, boolean vegetarianus) {
        Pizza p = pizzaRepo.findById(nev).orElseGet(Pizza::new);
        p.setNev(nev);
        p.setKategoria(kategoria(kategoriaNev));
        p.setVegetarianus(vegetarianus);
        return pizzaRepo.save(p);
    }

    /** Pizza törlése: előbb a hozzá tartozó rendeléseket kell törölni (idegen kulcs). */
    @Transactional
    public int torol(String nev) {
        int torolt = rendelesRepo.deleteByPizzaNev(nev);
        pizzaRepo.deleteById(nev);
        return torolt;
    }
}
