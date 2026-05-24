package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Categoria;
import antoninopalazzolo.kitchensync.entity.Piatto;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.payload.PiattoDTO;
import antoninopalazzolo.kitchensync.repository.PiattoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PiattoService {

    private final PiattoRepository piattoRepository;
    private final CategoriaService categoriaService;

    public PiattoService(PiattoRepository piattoRepository, CategoriaService categoriaService) {
        this.piattoRepository = piattoRepository;
        this.categoriaService = categoriaService;
    }

    // Lista paginata filtrata per categoria
    public Page<Piatto> trovaTutti(UUID categoriaId, int page, int size, String sortBy) {
        if (size > 30) size = 30;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Categoria categoria = categoriaService.trovaPerIdOException(categoriaId);
        return piattoRepository.findByCategoria(categoria, pageable);
    }

    // Se non esiste lancio l'eccezione
    public Piatto trovaPerIdOException(UUID id) {
        return piattoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Piatto con id " + id + " non trovato"));
    }

    // Il nome deve essere univoco dentro la stessa categoria
    // Controllo anche che la categoria appartenga alla sezione dell'admin loggato
    // Uso @Transactional perché faccio più operazioni — se il salvataggio
    // fallisce voglio che venga annullato tutto, non restare a metà
    @Transactional
    public Piatto salva(PiattoDTO body, Utente adminLoggato) {
        Categoria categoria = categoriaService.trovaPerIdOException(body.categoriaId());

        if (!categoria.getSezione().getId().equals(adminLoggato.getSezione().getId()))
            throw new BadRequestException("La categoria non appartiene alla tua sezione");

        if (piattoRepository.existsByNomeAndCategoria(body.nome(), categoria))
            throw new BadRequestException("Esiste già un piatto con il nome '" + body.nome() + "' in questa categoria");

        Piatto piatto = new Piatto(body.nome(), body.descrizione(), body.prezzo(), body.personalizzabile(), categoria);
        return piattoRepository.save(piatto);
    }

    // Stessa cosa in modifica
    @Transactional
    public Piatto modifica(UUID id, PiattoDTO body, Utente adminLoggato) {
        Piatto piatto = trovaPerIdOException(id);
        Categoria categoria = categoriaService.trovaPerIdOException(body.categoriaId());

        if (!categoria.getSezione().getId().equals(adminLoggato.getSezione().getId()))
            throw new BadRequestException("La categoria non appartiene alla tua sezione");

        if (piattoRepository.existsByNomeAndCategoria(body.nome(), categoria))
            throw new BadRequestException("Esiste già un piatto con il nome '" + body.nome() + "' in questa categoria");

        piatto.setNome(body.nome());
        piatto.setDescrizione(body.descrizione());
        piatto.setPrezzo(body.prezzo());
        piatto.setPersonalizzabile(body.personalizzabile());
        piatto.setCategoria(categoria);
        return piattoRepository.save(piatto);
    }

    // Cancello per id — prima verifico che esista
    public void elimina(UUID id) {
        Piatto piatto = trovaPerIdOException(id);
        piattoRepository.delete(piatto);
    }
}