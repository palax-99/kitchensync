package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.Categoria;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.payload.CategoriaDTO;
import antoninopalazzolo.kitchensync.payload.CategoriaResponseDTO;
import antoninopalazzolo.kitchensync.payload.SezioneResponseDTO;
import antoninopalazzolo.kitchensync.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categorie")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Solo l'ADMIN può gestire le categorie della sua sezione
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<CategoriaResponseDTO> trovaTutte(
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy
    ) {
        return categoriaService.trovaTutte(adminLoggato, page, size, sortBy)
                .map(c -> new CategoriaResponseDTO(c.getId(), c.getNome(),
                        new SezioneResponseDTO(c.getSezione().getId(), c.getSezione().getNome(), c.getSezione().isAttiva())));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoriaResponseDTO trovaPerId(@PathVariable UUID id) {
        Categoria c = categoriaService.trovaPerIdOException(id);
        return new CategoriaResponseDTO(c.getId(), c.getNome(),
                new SezioneResponseDTO(c.getSezione().getId(), c.getSezione().getNome(), c.getSezione().isAttiva()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoriaResponseDTO salva(
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated CategoriaDTO body
    ) {
        Categoria c = categoriaService.salva(body.nome(), adminLoggato);
        return new CategoriaResponseDTO(c.getId(), c.getNome(),
                new SezioneResponseDTO(c.getSezione().getId(), c.getSezione().getNome(), c.getSezione().isAttiva()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoriaResponseDTO modifica(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated CategoriaDTO body
    ) {
        Categoria c = categoriaService.modifica(id, body.nome(), adminLoggato);
        return new CategoriaResponseDTO(c.getId(), c.getNome(),
                new SezioneResponseDTO(c.getSezione().getId(), c.getSezione().getNome(), c.getSezione().isAttiva()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void elimina(@PathVariable UUID id) {
        categoriaService.elimina(id);
    }
}