package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.payload.LoginDTO;
import antoninopalazzolo.kitchensync.payload.LoginResponseDTO;
import antoninopalazzolo.kitchensync.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint pubblici per l'autenticazione.
// Sono escluse dal JWTFilter perché iniziano con /auth.
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Ricevo email e password, ritorno il token JWT.
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO body) {
        String token = authService.login(body);
        return new LoginResponseDTO(token);
    }
}
