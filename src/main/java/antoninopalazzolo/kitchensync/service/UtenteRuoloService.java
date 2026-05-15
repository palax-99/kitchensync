package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Ruolo;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.entity.UtenteRuolo;
import antoninopalazzolo.kitchensync.repository.UtenteRuoloRepository;
import org.springframework.stereotype.Service;

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
}
