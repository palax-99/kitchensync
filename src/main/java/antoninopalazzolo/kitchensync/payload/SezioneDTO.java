package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.NotBlank;

// Quello che ricevo dal frontend quando il SUPER_ADMIN crea o modifica una sezione
public record SezioneDTO(
        @NotBlank(message = "Il nome della sezione non può essere vuoto")
        String nome
) {
}