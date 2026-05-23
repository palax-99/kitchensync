package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

// Dati che ricevo quando il SUPER_ADMIN crea un nuovo utente.
public record NuovoUtenteDTO(

        @NotBlank(message = "Il nome è obbligatorio")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        String cognome,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Email non valida")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        String password,

        // Lista dei ruoli da assegnare — almeno uno obbligatorio.
        // Accetto i nomi dei ruoli come stringhe (es. ["ADMIN", "METRE"])
        @NotEmpty(message = "Almeno un ruolo è obbligatorio")
        List<String> ruoli,

        // Id della sezione — obbligatorio solo per gli ADMIN, per SUPER_ADMIN e METRE è null
        UUID sezioneId
) {
}