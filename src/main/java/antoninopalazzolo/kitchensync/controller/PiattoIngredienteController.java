package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.PiattoIngrediente;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.payload.*;
import antoninopalazzolo.kitchensync.service.PiattoIngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/piatti-ingredienti")
public class PiattoIngredienteController {

    @Autowired
    private PiattoIngredienteService piattoIngredienteService;

    // Lista degli ingredienti di un piatto
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PiattoIngredienteResponseDTO> trovaPerId(@RequestParam UUID piattoId) {
        return piattoIngredienteService.trovaPerId(piattoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // Collego un ingrediente a un piatto
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public PiattoIngredienteResponseDTO salva(
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated PiattoIngredienteDTO body
    ) {
        return toResponseDTO(piattoIngredienteService.salva(body, adminLoggato));
    }

    // Rimuovo il collegamento — uso l'id del record in piatti_ingredienti
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void elimina(@PathVariable UUID id) {
        piattoIngredienteService.elimina(id);
    }

    // Conversione a DTO — lo uso in tutti i metodi sopra
    private PiattoIngredienteResponseDTO toResponseDTO(PiattoIngrediente pi) {
        SezioneResponseDTO sezioneDTO = new SezioneResponseDTO(
                pi.getPiatto().getCategoria().getSezione().getId(),
                pi.getPiatto().getCategoria().getSezione().getNome(),
                pi.getPiatto().getCategoria().getSezione().isAttiva()
        );
        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                pi.getPiatto().getCategoria().getId(),
                pi.getPiatto().getCategoria().getNome(),
                sezioneDTO
        );
        PiattoResponseDTO piattoDTO = new PiattoResponseDTO(
                pi.getPiatto().getId(),
                pi.getPiatto().getNome(),
                pi.getPiatto().getDescrizione(),
                pi.getPiatto().getPrezzo(),
                pi.getPiatto().isPersonalizzabile(),
                pi.getPiatto().getImmagineUrl(),
                categoriaDTO
        );
        IngredienteResponseDTO ingredienteDTO = new IngredienteResponseDTO(
                pi.getIngrediente().getId(),
                pi.getIngrediente().getNome(),
                pi.getIngrediente().isDisponibile(),
                new SezioneResponseDTO(
                        pi.getIngrediente().getSezione().getId(),
                        pi.getIngrediente().getSezione().getNome(),
                        pi.getIngrediente().getSezione().isAttiva()
                )
        );
        return new PiattoIngredienteResponseDTO(pi.getId(), piattoDTO, ingredienteDTO);
    }
}