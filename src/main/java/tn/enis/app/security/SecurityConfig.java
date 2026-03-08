package tn.enis.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "tn.enis.app")
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Ressources statiques (webjars, css, js)
                .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()

                // Pages publiques
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/logout")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/access-denied")).permitAll()

                // Ajout : ADMIN + USER (doit être AVANT /persons/{id})
                .requestMatchers(new AntPathRequestMatcher("/persons/new", "GET")).hasAnyRole("ADMIN", "USER")
                .requestMatchers(new AntPathRequestMatcher("/persons/new", "POST")).hasAnyRole("ADMIN", "USER")

                // Recherche : ADMIN + USER
                .requestMatchers(new AntPathRequestMatcher("/persons/search")).hasAnyRole("ADMIN", "USER")
                .requestMatchers(new AntPathRequestMatcher("/persons", "GET")).hasAnyRole("ADMIN", "USER")

                // Suppression : ADMIN uniquement
                .requestMatchers(new AntPathRequestMatcher("/persons/*/delete", "POST")).hasRole("ADMIN")

                // Modification : ADMIN uniquement
                .requestMatchers(new AntPathRequestMatcher("/persons/*", "GET")).hasRole("ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/persons/*", "POST")).hasRole("ADMIN")

                // Gestion utilisateurs : ADMIN uniquement
                .requestMatchers(new AntPathRequestMatcher("/users/**")).hasRole("ADMIN")

                // Tout le reste nécessite une authentification
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", false)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }
}


