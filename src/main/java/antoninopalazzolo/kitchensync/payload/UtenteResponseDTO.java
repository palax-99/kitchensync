package antoninopalazzolo.kitchensync.payload;

import java.util.List;
import java.util.UUID;

// Risposta pulita che ritorno quando creo o restituisco un utente.
// Espone solo i campi che mi interessa mostrare al frontend —
// niente roba di Spring Security e niente password.
public record UtenteResponseDTO(
        UUID id,
        String nome,
        String cognome,
        String email,
        String avatar,
        List<String> ruoli,
        // La sezione è null per SUPER_ADMIN e METRE
        SezioneResponseDTO sezione
) {
}