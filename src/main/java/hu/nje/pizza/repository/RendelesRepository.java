package hu.nje.pizza.repository;

import hu.nje.pizza.entity.Rendeles;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RendelesRepository extends JpaRepository<Rendeles, Long> {

    List<Rendeles> findTop20ByOrderByFelvetelDesc();

    /** A legtöbbet rendelt pizzák: [pizzanév, összes darab], csökkenő sorrendben. */
    @Query("select r.pizza.nev, sum(r.darab) from Rendeles r group by r.pizza.nev order by sum(r.darab) desc")
    List<Object[]> legnepszerubbPizzak(Pageable pageable);

    /** Eladott darabszám kategóriánként: [kategórianév, összes darab] */
    @Query("select r.pizza.kategoria.nev, sum(r.darab) from Rendeles r group by r.pizza.kategoria.nev")
    List<Object[]> eladottDarabKategoriankent();

    /** Rendelések száma havonta: [év, hónap, darab] */
    @Query("select year(r.felvetel), month(r.felvetel), count(r) from Rendeles r "
            + "group by year(r.felvetel), month(r.felvetel) order by year(r.felvetel), month(r.felvetel)")
    List<Object[]> rendelesekHavonta();

    long countByPizzaNev(String pizzaNev);

    /** Egy pizza összes rendelésének törlése (a pizza törlése előtt). */
    @Modifying
    @Query("delete from Rendeles r where r.pizza.nev = :nev")
    int deleteByPizzaNev(@Param("nev") String nev);
}
