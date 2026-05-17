package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.payload.NuovoUtenteDTO;
import antoninopalazzolo.kitchensync.payload.UtenteResponseDTO;
import antoninopalazzolo.kitchensync.service.UtenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
}