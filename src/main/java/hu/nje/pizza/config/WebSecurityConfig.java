package hu.nje.pizza.config;

import hu.nje.pizza.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Jogosultságok:
 *  - látogató (nincs bejelentkezve): Főoldal, Adatbázis, Kapcsolat, Diagram, REST, Login, Regisztráció
 *  - regisztrált látogató (USER): + Üzenetek
 *  - admin (ADMIN): + CRUD
 */
@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /** Bejelentkezés után: admin → CRUD, regisztrált látogató → Üzenetek. */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            // A szerveren az alkalmazás egy alkönyvtárban fut (pl. /qjawqn), ezért kell a context path
            String base = request.getContextPath();
            response.sendRedirect(base + (isAdmin ? "/crud" : "/uzenetek"));
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/adatbazis", "/kapcsolat", "/diagram", "/rest",
                                "/register", "/login", "/error",
                                "/css/**", "/js/**", "/img/**", "/api/**").permitAll()
                        .requestMatchers("/uzenetek").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/crud/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                // A REST API-t cURL-ből és Postman-ből is el kell érni, ott nincs CSRF-token
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, e) ->
                                response.sendRedirect(request.getContextPath() + "/"))
                );
        return http.build();
    }
}
