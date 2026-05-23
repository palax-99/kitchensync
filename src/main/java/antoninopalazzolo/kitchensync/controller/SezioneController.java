package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.Sezione;
import antoninopalazzolo.kitchensync.payload.SezioneDTO;
import antoninopalazzolo.kitchensync.payload.SezioneResponseDTO;
import antoninopalazzolo.kitchensync.service.SezioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sezioni")
public class SezioneController {

    @Autowired
    private SezioneService sezioneService;

    // Lista paginata — solo il SUPER_ADMIN gestisce le sezioni
    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public Page<SezioneResponseDTO> trovaTutte(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy
    ) {
        return sezioneService.trovaTutte(page, size, sortBy)
                .map(s -> new SezioneResponseDTO(s.getId(), s.getNome(), s.isAttiva()));
    }

    // Dettaglio singola sezione
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public SezioneResponseDTO trovaPerId(@PathVariable UUID id) {
        Sezione s = sezioneService.trovaPerIdOException(id);
        return new SezioneResponseDTO(s.getId(), s.getNome(), s.isAttiva());
    }

    // Creo una nuova sezione
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public SezioneResponseDTO salva(@RequestBody @Validated SezioneDTO body) {
        Sezione s = sezioneService.salva(body.nome());
        return new SezioneResponseDTO(s.getId(), s.getNome(), s.isAttiva());
    }

    // Modifico il nome di una sezione esistente
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public SezioneResponseDTO modifica(@PathVariable UUID id, @RequestBody @Validated SezioneDTO body) {
        Sezione s = sezioneService.modifica(id, body.nome());
        return new SezioneResponseDTO(s.getId(), s.getNome(), s.isAttiva());
    }

    // Attiva o disattiva una sezione — ricevo solo il boolean nel body
    @PatchMapping("/{id}/stato")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public SezioneResponseDTO cambiaStato(@PathVariable UUID id, @RequestBody boolean attiva) {
        Sezione s = sezioneService.cambiaStato(id, attiva);
        return new SezioneResponseDTO(s.getId(), s.getNome(), s.isAttiva());
    }

    // Cancella una sezione — operazione rara, di solito preferisco disattivarla
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public void elimina(@PathVariable UUID id) {
        sezioneService.elimina(id);
    }
}