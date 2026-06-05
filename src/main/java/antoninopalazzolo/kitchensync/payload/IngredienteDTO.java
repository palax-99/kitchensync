package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.NotBlank;

// Quello che ricevo dal frontend quando l'ADMIN crea o modifica un ingrediente
public record IngredienteDTO(
        @NotBlank(message = "Il nome dell'ingrediente non può essere vuoto")
        String nome
) {
}