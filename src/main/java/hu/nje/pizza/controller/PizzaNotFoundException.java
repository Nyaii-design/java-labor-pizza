package hu.nje.pizza.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Ha a REST API-n nem létező pizzát kérnek: 404 Not Found. */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PizzaNotFoundException extends RuntimeException {
    public PizzaNotFoundException(String nev) {
        super("A pizza nem található: " + nev);
    }
}
