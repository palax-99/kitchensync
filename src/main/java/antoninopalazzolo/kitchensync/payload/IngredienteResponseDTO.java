package antoninopalazzolo.kitchensync.payload;

import java.util.UUID;

// Quello che mando al frontend — con la sezione e la disponibilità
public record IngredienteResponseDTO(
        UUID id,
        String nome,
        boolean disponibile,
        SezioneResponseDTO sezione
) {
}