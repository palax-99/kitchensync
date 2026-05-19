package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.payload.NuovoUtenteDTO;
import antoninopalazzolo.kitchensync.payload.UtenteResponseDTO;
import antoninopalazzolo.kitchensync.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Endpoint per la gestione degli utenti.
// Tutti gli endpoint qui dentro richiedono autenticazione.
@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // Solo il SUPER_ADMIN può creare nuovi utenti (ADMIN o METRE).
    // @PreAuthorize controlla i permessi prima ancora di entrare nel metodo.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public UtenteResponseDTO creaUtente(@RequestBody @Valid NuovoUtenteDTO body) {
        Utente nuovoUtente = utenteService.creaUtente(body);
        return utenteService.toResponseDTO(nuovoUtente);
    }

    // L'utente loggato recupera i propri dati.
    // @AuthenticationPrincipal mi dà direttamente l'Utente che il JWTFilter
    // ha messo nel SecurityContext — niente query, niente parametri.
    @GetMapping("/me")
    public UtenteResponseDTO getMe(@AuthenticationPrincipal Utente currentUser) {
        return utenteService.toResponseDTO(currentUser);
    }

    // Lista paginata di tutti gli utenti — solo per SUPER_ADMIN.
// Default: pagina 0, 10 utenti per pagina, ordinati per cognome.
    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public Page<UtenteResponseDTO> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cognome") String sortBy) {
        return utenteService.findAll(page, size, sortBy);
    }
}