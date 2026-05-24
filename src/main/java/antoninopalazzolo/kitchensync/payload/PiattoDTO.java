package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

// Quello che ricevo dal frontend quando l'ADMIN crea o modifica un piatto
public record PiattoDTO(
        @NotBlank(message = "Il nome del piatto non può essere vuoto")
        String nome,

        // Descrizione opzionale
        String descrizione,

        @NotNull(message = "Il prezzo è obbligatorio")
        @Positive(message = "Il prezzo deve essere maggiore di zero")
        double prezzo,

        // Se non specificato parto da false
        boolean personalizzabile,

        @NotNull(message = "La categoria è obbligatoria")
        UUID categoriaId
) {
}