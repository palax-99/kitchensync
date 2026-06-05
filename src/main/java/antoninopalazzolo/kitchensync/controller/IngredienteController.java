package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.Ingrediente;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.payload.IngredienteDTO;
import antoninopalazzolo.kitchensync.payload.IngredienteResponseDTO;
import antoninopalazzolo.kitchensync.payload.SezioneResponseDTO;
import antoninopalazzolo.kitchensync.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ingredienti")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    // Solo l'ADMIN può gestire gli ingredienti della sua sezione
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<IngredienteResponseDTO> trovaTutti(
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy
    ) {
        return ingredienteService.trovaTutti(adminLoggato, page, size, sortBy)
                .map(i -> new IngredienteResponseDTO(i.getId(), i.getNome(), i.isDisponibile(),
                        new SezioneResponseDTO(i.getSezione().getId(), i.getSezione().getNome(), i.getSezione().isAttiva())));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public IngredienteResponseDTO trovaPerId(@PathVariable UUID id) {
        Ingrediente i = ingredienteService.trovaPerIdOException(id);
        return new IngredienteResponseDTO(i.getId(), i.getNome(), i.isDisponibile(),
                new SezioneResponseDTO(i.getSezione().getId(), i.getSezione().getNome(), i.getSezione().isAttiva()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public IngredienteResponseDTO salva(
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated IngredienteDTO body
    ) {
        Ingrediente i = ingredienteService.salva(body.nome(), adminLoggato);
        return new IngredienteResponseDTO(i.getId(), i.getNome(), i.isDisponibile(),
                new SezioneResponseDTO(i.getSezione().getId(), i.getSezione().getNome(), i.getSezione().isAttiva()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public IngredienteResponseDTO modifica(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated IngredienteDTO body
    ) {
        Ingrediente i = ingredienteService.modifica(id, body.nome(), adminLoggato);
        return new IngredienteResponseDTO(i.getId(), i.getNome(), i.isDisponibile(),
                new SezioneResponseDTO(i.getSezione().getId(), i.getSezione().getNome(), i.getSezione().isAttiva()));
    }

    // Cambio disponibilità — l'operazione più usata durante il servizio
    @PatchMapping("/{id}/disponibilita")
    @PreAuthorize("hasAuthority('ADMIN')")
    public IngredienteResponseDTO cambiaDisponibilita(
            @PathVariable UUID id,
            @RequestBody boolean disponibile
    ) {
        Ingrediente i = ingredienteService.cambiaDisponibilita(id, disponibile);
        return new IngredienteResponseDTO(i.getId(), i.getNome(), i.isDisponibile(),
                new SezioneResponseDTO(i.getSezione().getId(), i.getSezione().getNome(), i.getSezione().isAttiva()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void elimina(@PathVariable UUID id) {
        ingredienteService.elimina(id);
    }
}