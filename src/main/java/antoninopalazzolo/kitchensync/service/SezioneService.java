package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Sezione;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.repository.SezioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SezioneService {

    @Autowired
    private SezioneRepository sezioneRepository;

    // Lista paginata, max 30 per pagina
    public Page<Sezione> trovaTutte(int page, int size, String sortBy) {
        if (size > 30) size = 30;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return sezioneRepository.findAll(pageable);
    }

    // Se non esiste lancio l'eccezione, così non lo riscrivo in ogni metodo
    public Sezione trovaPerIdOException(UUID id) {
        return sezioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sezione con id " + id + " non trovata"));
    }

    // Prima di salvare controllo che il nome non sia già preso
    public Sezione salva(String nome) {
        if (sezioneRepository.existsByNome(nome))
            throw new BadRequestException("Esiste già una sezione con il nome '" + nome + "'");
        return sezioneRepository.save(new Sezione(nome));
    }

    // Stessa cosa in modifica — il nome deve restare univoco
    public Sezione modifica(UUID id, String nome) {
        Sezione sezione = trovaPerIdOException(id);
        if (sezioneRepository.existsByNome(nome))
            throw new BadRequestException("Esiste già una sezione con il nome '" + nome + "'");
        sezione.setNome(nome);
        return sezioneRepository.save(sezione);
    }

    // Per disattivare una sezione cambio solo il flag, non la cancello
    public Sezione cambiaStato(UUID id, boolean attiva) {
        Sezione sezione = trovaPerIdOException(id);
        sezione.setAttiva(attiva);
        return sezioneRepository.save(sezione);
    }

    // Il delete esiste ma lo uso raramente — meglio disattivare
    public void elimina(UUID id) {
        Sezione sezione = trovaPerIdOException(id);
        sezioneRepository.delete(sezione);
    }

    // Mi serve nel menu vivo — prendo solo le sezioni attive
    public List<Sezione> trovaTutteAttive() {
        return sezioneRepository.findByAttiva(true);
    }
}