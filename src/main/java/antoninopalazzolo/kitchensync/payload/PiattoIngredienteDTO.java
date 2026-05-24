package antoninopalazzolo.kitchensync.payload;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PiattoIngredienteDTO(
        @NotNull(message = "L'id del piatto è obbligatorio")
        UUID piattoId,

        @NotNull(message = "L'id dell'ingrediente è obbligatorio")
        UUID ingredienteId
) {
}