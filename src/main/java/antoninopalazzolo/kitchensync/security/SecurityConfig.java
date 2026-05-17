package antoninopalazzolo.kitchensync.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configurazione centrale di Spring Security.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Abilita @PreAuthorize sui controller
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabilito CSRF — non serve con JWT, è per le sessioni
                .csrf(AbstractHttpConfigurer::disable)
                // Disabilito le sessioni — con JWT ogni richiesta è stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Le rotte /auth sono pubbliche (login e registrazione)
                // tutto il resto richiede autenticazione
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                // Aggiungo il mio JWTFilter prima del filtro standard di Spring Security
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
