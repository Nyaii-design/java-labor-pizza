package hu.nje.pizza.config;

import hu.nje.pizza.entity.User;
import hu.nje.pizza.repository.KategoriaRepository;
import hu.nje.pizza.repository.PizzaRepository;
import hu.nje.pizza.repository.RendelesRepository;
import hu.nje.pizza.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Induláskor feltölti az adatbázist a Pizza adatbázis szövegfájljaiból (src/main/resources/data),
 * ha a táblák még üresek, és létrehozza az admin felhasználót.
 * Így a szerveren nem kell kézzel SQL-t importálni.
 */
@Configuration
public class AdatBetolto {

    private static final Logger log = LoggerFactory.getLogger(AdatBetolto.class);
    private static final DateTimeFormatter DATUM = DateTimeFormatter.ofPattern("yyyy.MM.dd H:mm:ss");

    public static final String ADMIN_NEV = "admin";
    public static final String ADMIN_JELSZO = "admin123";

    @Bean
    CommandLineRunner adatokFeltoltese(JdbcTemplate jdbc, KategoriaRepository kategoriaRepo,
                                       PizzaRepository pizzaRepo, RendelesRepository rendelesRepo,
                                       UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (kategoriaRepo.count() == 0) {
                List<Object[]> sorok = new ArrayList<>();
                for (String[] s : beolvas("data/kategoria.txt")) {
                    sorok.add(new Object[]{s[0], Integer.parseInt(s[1])});
                }
                jdbc.batchUpdate("insert into kategoria (nev, ar) values (?, ?)", sorok);
                log.info("Kategóriák betöltve: {}", sorok.size());
            }
            if (pizzaRepo.count() == 0) {
                List<Object[]> sorok = new ArrayList<>();
                for (String[] s : beolvas("data/pizza.txt")) {
                    sorok.add(new Object[]{s[0], s[1], s[2].equals("1")});
                }
                jdbc.batchUpdate("insert into pizza (nev, kategorianev, vegetarianus) values (?, ?, ?)", sorok);
                log.info("Pizzák betöltve: {}", sorok.size());
            }
            if (rendelesRepo.count() == 0) {
                List<Object[]> sorok = new ArrayList<>();
                for (String[] s : beolvas("data/rendeles.txt")) {
                    sorok.add(new Object[]{s[0], Integer.parseInt(s[1]), idopont(s[2]), idopont(s[3])});
                }
                jdbc.batchUpdate("insert into rendeles (pizzanev, darab, felvetel, kiszallitas) values (?, ?, ?, ?)", sorok);
                log.info("Rendelések betöltve: {}", sorok.size());
            }
            if (userRepo.findByUsername(ADMIN_NEV).isEmpty()) {
                userRepo.save(new User(ADMIN_NEV, encoder.encode(ADMIN_JELSZO), "ADMIN"));
                log.info("Admin felhasználó létrehozva: {}", ADMIN_NEV);
            }
        };
    }

    /** Tabulátorral tagolt szövegfájl beolvasása, a fejléc nélkül. */
    private static List<String[]> beolvas(String fajl) throws IOException {
        List<String[]> eredmeny = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource(fajl).getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine(); // fejléc
            String sor;
            while ((sor = br.readLine()) != null) {
                if (!sor.isBlank()) {
                    eredmeny.add(sor.strip().split("\t"));
                }
            }
        }
        return eredmeny;
    }

    private static Timestamp idopont(String s) {
        return Timestamp.valueOf(LocalDateTime.parse(s.strip(), DATUM));
    }
}
