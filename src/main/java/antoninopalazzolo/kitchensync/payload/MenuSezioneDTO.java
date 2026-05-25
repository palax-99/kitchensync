package antoninopalazzolo.kitchensync.payload;

import java.util.List;
import java.util.UUID;

public record MenuSezioneDTO(
        UUID id,
        String nome,
        List<MenuCategoriaDTO> categorie
) {
}