package antoninopalazzolo.kitchensync.payload;

import java.util.UUID;

// Quello che mando al frontend — con piatto e ingrediente annidati
public record PiattoIngredienteResponseDTO(
        UUID id,
        PiattoResponseDTO piatto,
        IngredienteResponseDTO ingrediente
) {
}