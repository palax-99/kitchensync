package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.Piatto;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.payload.CategoriaResponseDTO;
import antoninopalazzolo.kitchensync.payload.PiattoDTO;
import antoninopalazzolo.kitchensync.payload.PiattoResponseDTO;
import antoninopalazzolo.kitchensync.payload.SezioneResponseDTO;
import antoninopalazzolo.kitchensync.service.CloudinaryService;
import antoninopalazzolo.kitchensync.service.PiattoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/piatti")
public class PiattoController {


    private PiattoService piattoService;
    private CloudinaryService cloudinaryService;

    public PiattoController(PiattoService piattoService, CloudinaryService cloudinaryService) {
        this.piattoService = piattoService;
        this.cloudinaryService = cloudinaryService;
    }

    // Lista paginata per categoria — passo il categoriaId come parametro
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<PiattoResponseDTO> trovaTutti(
            @RequestParam UUID categoriaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy
    ) {
        return piattoService.trovaTutti(categoriaId, page, size, sortBy)
                .map(this::toResponseDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PiattoResponseDTO trovaPerId(@PathVariable UUID id) {
        return toResponseDTO(piattoService.trovaPerIdOException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public PiattoResponseDTO salva(
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated PiattoDTO body
    ) {
        return toResponseDTO(piattoService.salva(body, adminLoggato));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PiattoResponseDTO modifica(
            @PathVariable UUID id,
            @AuthenticationPrincipal Utente adminLoggato,
            @RequestBody @Validated PiattoDTO body
    ) {
        return toResponseDTO(piattoService.modifica(id, body, adminLoggato));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void elimina(@PathVariable UUID id) {
        piattoService.elimina(id);
    }

    // Converto Piatto in PiattoResponseDTO — lo uso in tutti i metodi sopra
    private PiattoResponseDTO toResponseDTO(Piatto p) {
        SezioneResponseDTO sezioneDTO = new SezioneResponseDTO(
                p.getCategoria().getSezione().getId(),
                p.getCategoria().getSezione().getNome(),
                p.getCategoria().getSezione().isAttiva()
        );
        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                p.getCategoria().getId(),
                p.getCategoria().getNome(),
                sezioneDTO
        );
        return new PiattoResponseDTO(
                p.getId(),
                p.getNome(),
                p.getDescrizione(),
                p.getPrezzo(),
                p.isPersonalizzabile(),
                p.getImmagineUrl(),
                categoriaDTO
        );
    }

    // Carico l'immagine del piatto su Cloudinary e aggiorno l'URL nel database
    @PostMapping("/{id}/immagine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PiattoResponseDTO uploadImmagine(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file
    ) {
        return toResponseDTO(piattoService.aggiornaImmagine(id, cloudinaryService.upload(file)));
    }
}