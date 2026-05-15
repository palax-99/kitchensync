package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
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

    // Controllo se esiste già un utente con questa email
    public boolean existsByEmail(String email) {
        return utenteRepository.existsByEmail(email);
    }

    // Salvo un nuovo utente — se l'email esiste già lancio BadRequestException
    public Utente save(Utente utente) {
        if (utenteRepository.existsByEmail(utente.getEmail())) {
            throw new BadRequestException("Email " + utente.getEmail() + " già in uso.");
        }
        return utenteRepository.save(utente);
    }
}
