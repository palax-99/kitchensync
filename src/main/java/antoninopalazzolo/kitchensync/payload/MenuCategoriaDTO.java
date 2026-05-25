package antoninopalazzolo.kitchensync.payload;

import java.util.List;
import java.util.UUID;

// La categoria con i suoi piatti disponibili
public record MenuCategoriaDTO(
        UUID id,
        String nome,
        List<MenuPiattoDTO> piatti
) {
}