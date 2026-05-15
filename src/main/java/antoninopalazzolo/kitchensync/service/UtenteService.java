package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

// Logica di business per la gestione degli utenti.
@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    // Mi serve nel JWTFilter — dato l'id estratto dal token,
    // recupero l'utente dal database.
    public Utente findById(UUID id) {
        Utente utenteTrovato = utenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con id " + id + " non trovato."));
        return utenteTrovato;
    }
}
