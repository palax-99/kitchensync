package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Ruolo;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.entity.UtenteRuolo;
import antoninopalazzolo.kitchensync.repository.UtenteRuoloRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

// Logica di business per la gestione del collegamento tra utente e ruolo.
@Service
public class UtenteRuoloService {

    private final UtenteRuoloRepository utenteRuoloRepository;

    public UtenteRuoloService(UtenteRuoloRepository utenteRuoloRepository) {
        this.utenteRuoloRepository = utenteRuoloRepository;
    }

    // Collego un ruolo a un utente — lo uso nel Runner e quando creo nuovi utenti.
    public UtenteRuolo assegnaRuolo(Utente utente, Ruolo ruolo) {
        UtenteRuolo utenteRuolo = new UtenteRuolo(utente, ruolo);
        return utenteRuoloRepository.save(utenteRuolo);
    }

    // Recupero tutti i ruoli di un utente per popolare il SecurityContext nel JWTFilter.
// Trasformo ogni UtenteRuolo in SimpleGrantedAuthority — il tipo che Spring Security capisce.
    public List<SimpleGrantedAuthority> getAuthoritiesByUtente(Utente utente) {
        return utenteRuoloRepository.findByUtente(utente)
                .stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRuolo().getDenominazione()))
                .toList();
    }
}
