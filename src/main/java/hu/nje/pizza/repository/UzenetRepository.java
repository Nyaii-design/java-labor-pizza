package hu.nje.pizza.repository;

import hu.nje.pizza.entity.Uzenet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UzenetRepository extends JpaRepository<Uzenet, Long> {

    /** Fordított időrend: a legfrissebb üzenet legyen elöl. */
    List<Uzenet> findAllByOrderByKuldesIdejeDesc();
}
