package hu.nje.pizza;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PizzaApplication {

    /**
     * Helyi indítás (IntelliJ): a "local" profillal fut, H2 adatbázissal.
     * A szerveren a Tomcat a ServletInitializer-en keresztül indítja, ott MySQL-t használ.
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(PizzaApplication.class)
                .profiles("local")
                .run(args);
        System.out.println("Az alkalmazás fut: http://localhost:8080");
    }
}
