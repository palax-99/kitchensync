package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Ingrediente;
import antoninopalazzolo.kitchensync.entity.Piatto;
import antoninopalazzolo.kitchensync.entity.PiattoIngrediente;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.payload.PiattoIngredienteDTO;
import antoninopalazzolo.kitchensync.repository.PiattoIngredienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PiattoIngredienteService {

    private final PiattoIngredienteRepository piattoIngredienteRepository;
    private final PiattoService piattoService;
    private final IngredienteService ingredienteService;

    public PiattoIngredienteService(PiattoIngredienteRepository piattoIngredienteRepository,
                                    PiattoService piattoService,
                                    IngredienteService ingredienteService) {
        this.piattoIngredienteRepository = piattoIngredienteRepository;
        this.piattoService = piattoService;
        this.ingredienteService = ingredienteService;
    }

    // Trovo tutti gli ingredienti di un piatto
    public List<PiattoIngrediente> trovaPerId(UUID piattoId) {
        Piatto piatto = piattoService.trovaPerIdOException(piattoId);
        return piattoIngredienteRepository.findByPiatto(piatto);
    }

    // Collego un ingrediente a un piatto — controllo che appartengano alla stessa sezione
    // Uso @Transactional perché faccio più operazioni che devono stare o cadere insieme
    @Transactional
    public PiattoIngrediente salva(PiattoIngredienteDTO body, Utente adminLoggato) {
        Piatto piatto = piattoService.trovaPerIdOException(body.piattoId());
        Ingrediente ingrediente = ingredienteService.trovaPerIdOException(body.ingredienteId());

        // Controllo che piatto e ingrediente appartengano alla stessa sezione
        if (!piatto.getCategoria().getSezione().getId()
                .equals(ingrediente.getSezione().getId()))
            throw new BadRequestException("Il piatto e l'ingrediente non appartengono alla stessa sezione");

        // Controllo che il collegamento non esista già
        if (piattoIngredienteRepository.existsByPiattoAndIngrediente(piatto, ingrediente))
            throw new BadRequestException("Questo ingrediente è già collegato al piatto");

        return piattoIngredienteRepository.save(new PiattoIngrediente(piatto, ingrediente));
    }

    // Rimuovo il collegamento tra un piatto e un ingrediente
    public void elimina(UUID id) {
        PiattoIngrediente piattoIngrediente = piattoIngredienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Collegamento non trovato"));
        piattoIngredienteRepository.delete(piattoIngrediente);
    }
}