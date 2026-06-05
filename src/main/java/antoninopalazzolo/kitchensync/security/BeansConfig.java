package antoninopalazzolo.kitchensync.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Configurazione neutra per bean condivisi.
// Tenendo PasswordEncoder qui evito il loop di dipendenze tra
// SecurityConfig (che dipende da JWTFilter) e UtenteService (che usa PasswordEncoder).
@Configuration
public class BeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}