package antoninopalazzolo.kitchensync.payload;

import java.util.UUID;

// Quello che mando al frontend — con la sezione di appartenenza
public record CategoriaResponseDTO(
        UUID id,
        String nome,
        SezioneResponseDTO sezione
) {
}