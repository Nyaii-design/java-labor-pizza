package hu.nje.pizza;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Ezen keresztül indítja el az alkalmazást a Linux-szerveren futó Tomcat (WAR). */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PizzaApplication.class);
    }
}
