package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Dati che ricevo quando un utente prova a fare login.
public record LoginDTO(

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Email non valida")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        String password
) {
}
