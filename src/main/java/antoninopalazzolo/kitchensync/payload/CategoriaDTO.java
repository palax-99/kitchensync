package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.NotBlank;

// Quello che ricevo dal frontend quando l'ADMIN crea o modifica una categoria
public record CategoriaDTO(
        @NotBlank(message = "Il nome della categoria non può essere vuoto")
        String nome
) {
}