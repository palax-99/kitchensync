package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Ruolo;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.exception.UnauthorizedException;
import antoninopalazzolo.kitchensync.payload.NuovoUtenteDTO;
import antoninopalazzolo.kitchensync.payload.UtenteResponseDTO;
import antoninopalazzolo.kitchensync.repository.UtenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// Logica di business per la gestione degli utenti.
@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final RuoloService ruoloService;
    private final UtenteRuoloService utenteRuoloService;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository,
                         RuoloService ruoloService,
                         UtenteRuoloService utenteRuoloService,
                         PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.ruoloService = ruoloService;
        this.utenteRuoloService = utenteRuoloService;
        this.passwordEncoder = passwordEncoder;
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

    // Cerco un utente per email — lo uso nel login.
// Se non lo trovo lancio UnauthorizedException così non rivelo
// se l'email esiste o no a chi tenta di indovinare.
    public Utente findByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Credenziali non valide."));
    }

    // Creo un nuovo utente partendo dal DTO.
// 1. Controllo che l'email non sia già in uso
// 2. Hasho la password con BCrypt
// 3. Salvo l'utente
// 4. Per ogni ruolo richiesto lo cerco e lo collego all'utente
    @Transactional
    public Utente creaUtente(NuovoUtenteDTO body) {

        if (utenteRepository.existsByEmail(body.email())) {
            throw new BadRequestException("Email " + body.email() + " già in uso.");
        }

        Utente nuovoUtente = new Utente(
                body.nome(),
                body.cognome(),
                body.email(),
                passwordEncoder.encode(body.password())
        );
        Utente utenteSalvato = utenteRepository.save(nuovoUtente);

        // Per ogni denominazione ruolo nel DTO la cerco e la collego all'utente
        for (String denominazione : body.ruoli()) {
            Ruolo ruolo = ruoloService.findByDenominazione(denominazione);
            utenteRuoloService.assegnaRuolo(utenteSalvato, ruolo);
        }

        return utenteSalvato;
    }

    // Converto un Utente in UtenteResponseDTO — espongo solo i campi puliti.
// Recupero i ruoli dell'utente per popolare la lista delle denominazioni.
    public UtenteResponseDTO toResponseDTO(Utente utente) {
        List<String> denominazioni = utenteRuoloService.getAuthoritiesByUtente(utente)
                .stream()
                .map(a -> a.getAuthority())
                .toList();

        return new UtenteResponseDTO(
                utente.getId(),
                utente.getNome(),
                utente.getCognome(),
                utente.getEmail(),
                utente.getAvatar(),
                denominazioni
        );
    }

    // Restituisco la lista paginata degli utenti.
    // Costruisco il Pageable da page, size e sortBy ricevuti dal controller.
    public Page<UtenteResponseDTO> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50; // Limito la dimensione massima per non sovraccaricare
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return utenteRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }
}
