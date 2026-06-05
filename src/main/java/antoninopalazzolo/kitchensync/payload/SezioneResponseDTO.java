package antoninopalazzolo.kitchensync.payload;

import java.util.UUID;

// Quello che mando al frontend — mai esporre l'entità raw
public record SezioneResponseDTO(
        UUID id,
        String nome,
        boolean attiva
) {
}