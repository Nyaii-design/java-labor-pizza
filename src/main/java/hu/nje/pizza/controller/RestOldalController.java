package hu.nje.pizza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/** REST menü: bemutatja az API végpontjait és a cURL-parancsokat. */
@Controller
public class RestOldalController {

    @GetMapping("/rest")
    public String rest(Model model) {
        // pl. http://rivendell.nje.hu:9443/qjawqn vagy http://localhost:8080
        String alap = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String api = alap + "/api/pizzak";
        model.addAttribute("apiUrl", api);
        // Windows parancssorba bemásolható cURL-parancsok
        model.addAttribute("curlok", List.of(
                "curl " + api,
                "curl " + api + "/Babos",
                "curl -X POST " + api + " -H \"Content-type:application/json\" -d \"{\\\"nev\\\": \\\"Teszt\\\", \\\"kategoria\\\": \\\"lovag\\\", \\\"vegetarianus\\\": true}\"",
                "curl -X PUT " + api + "/Teszt -H \"Content-type:application/json\" -d \"{\\\"kategoria\\\": \\\"lovag\\\", \\\"vegetarianus\\\": false}\"",
                "curl -X DELETE " + api + "/Teszt"));
        return "rest";
    }
}
