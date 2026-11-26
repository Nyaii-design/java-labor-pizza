package hu.nje.pizza.repository;

import hu.nje.pizza.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, String> {

    List<Pizza> findAllByOrderByNevAsc();

    List<Pizza> findByKategoriaNevOrderByNevAsc(String kategoriaNev);

    /** Kategóriánként a pizzák száma: [kategórianév, darab] */
    @Query("select p.kategoria.nev, count(p) from Pizza p group by p.kategoria.nev")
    List<Object[]> pizzaSzamKategoriankent();
}
