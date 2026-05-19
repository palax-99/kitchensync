package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.UnauthorizedException;
import antoninopalazzolo.kitchensync.payload.LoginDTO;
import antoninopalazzolo.kitchensync.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Logica di business per l'autenticazione.
@Service
public class AuthService {

    private final UtenteService utenteService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTools jwtTools;

    public AuthService(UtenteService utenteService,
                       PasswordEncoder passwordEncoder,
                       JWTTools jwtTools) {
        this.utenteService = utenteService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTools = jwtTools;
    }

    // Verifico le credenziali e ritorno il token JWT.
    // Lancio UnauthorizedException se l'email non esiste o la password è sbagliata —
    // non specifico quale dei due per non dare informazioni a chi tenta di indovinare.
    public String login(LoginDTO body) {
        Utente utenteTrovato = utenteService.findByEmail(body.email());

        if (!passwordEncoder.matches(body.password(), utenteTrovato.getPassword())) {
            throw new UnauthorizedException("Credenziali non valide.");
        }

        return jwtTools.generateToken(utenteTrovato);
    }
}
