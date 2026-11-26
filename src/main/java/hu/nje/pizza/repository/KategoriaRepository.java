package hu.nje.pizza.repository;

import hu.nje.pizza.entity.Kategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KategoriaRepository extends JpaRepository<Kategoria, String> {

    List<Kategoria> findAllByOrderByArAsc();
}
