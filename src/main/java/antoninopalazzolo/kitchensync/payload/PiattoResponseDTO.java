package antoninopalazzolo.kitchensync.payload;

import java.util.UUID;

public record PiattoResponseDTO(
        UUID id,
        String nome,
        String descrizione,
        double prezzo,
        boolean personalizzabile,
        String immagineUrl,
        CategoriaResponseDTO categoria
) {
}