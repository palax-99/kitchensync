package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Categoria;
import antoninopalazzolo.kitchensync.entity.Sezione;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Lista paginata filtrata per la sezione dell'admin loggato
    public Page<Categoria> trovaTutte(Utente adminLoggato, int page, int size, String sortBy) {
        if (size > 30) size = 30;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return categoriaRepository.findBySezione(adminLoggato.getSezione(), pageable);
    }

    // Se non esiste lancio l'eccezione, così non lo riscrivo in ogni metodo
    public Categoria trovaPerIdOException(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria con id " + id + " non trovata"));
    }

    // Il nome deve essere univoco dentro la stessa sezione, non globalmente
    public Categoria salva(String nome, Utente adminLoggato) {
        Sezione sezione = adminLoggato.getSezione();
        if (categoriaRepository.existsByNomeAndSezione(nome, sezione))
            throw new BadRequestException("Esiste già una categoria con il nome '" + nome + "' in questa sezione");
        return categoriaRepository.save(new Categoria(nome, sezione));
    }

    // Stessa cosa in modifica — ricontrollo l'unicità dentro la sezione
    public Categoria modifica(UUID id, String nome, Utente adminLoggato) {
        Categoria categoria = trovaPerIdOException(id);
        if (categoriaRepository.existsByNomeAndSezione(nome, adminLoggato.getSezione()))
            throw new BadRequestException("Esiste già una categoria con il nome '" + nome + "' in questa sezione");
        categoria.setNome(nome);
        return categoriaRepository.save(categoria);
    }

    // Cancello per id — prima verifico che esista
    public void elimina(UUID id) {
        Categoria categoria = trovaPerIdOException(id);
        categoriaRepository.delete(categoria);
    }

    // Mi serve nel menu vivo — prendo tutte le categorie di una sezione senza paginazione
    public List<Categoria> findBySezione(Sezione sezione) {
        return categoriaRepository.findBySezione(sezione);
    }
}