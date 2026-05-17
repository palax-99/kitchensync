package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Ruolo;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.repository.RuoloRepository;
import org.springframework.stereotype.Service;

// Logica di business per la gestione dei ruoli.
@Service
public class RuoloService {

    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    // Mi serve nel Runner per trovare un ruolo per denominazione.
    // Se non esiste lo creo — così non duplico mai i ruoli al riavvio.
    public Ruolo findOrCreate(String denominazione) {
        return ruoloRepository.findByDenominazione(denominazione)
                .orElseGet(() -> ruoloRepository.save(new Ruolo(denominazione)));
    }

    // Cerco un ruolo per denominazione — se non esiste lancio errore.
// Lo uso quando il SUPER_ADMIN assegna ruoli durante la creazione utenti:
// i ruoli validi sono solo quelli creati dal Runner all'avvio.
    public Ruolo findByDenominazione(String denominazione) {
        return ruoloRepository.findByDenominazione(denominazione)
                .orElseThrow(() -> new BadRequestException("Ruolo " + denominazione + " non valido."));
    }
}
