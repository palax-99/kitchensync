package antoninopalazzolo.kitchensync.payload;

import java.util.UUID;

public record MenuPiattoDTO(
        UUID id,
        String nome,
        String descrizione,
        double prezzo,
        boolean personalizzabile,
        String immagineUrl
) {
}