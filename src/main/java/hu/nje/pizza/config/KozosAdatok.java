package hu.nje.pizza.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Minden oldalnak átadja a készítők nevét és Neptun-kódját (lábléc). */
@ControllerAdvice
public class KozosAdatok {

    @Value("${app.keszito1.nev}")    private String nev1;
    @Value("${app.keszito1.neptun}") private String neptun1;
    @Value("${app.keszito2.nev}")    private String nev2;
    @Value("${app.keszito2.neptun}") private String neptun2;

    @ModelAttribute("keszitok")
    public String keszitok() {
        return nev1 + " (" + neptun1 + "), " + nev2 + " (" + neptun2 + ")";
    }
}
