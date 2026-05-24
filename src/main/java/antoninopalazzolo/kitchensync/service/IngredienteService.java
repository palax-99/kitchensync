package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Ingrediente;
import antoninopalazzolo.kitchensync.entity.Sezione;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.BadRequestException;
import antoninopalazzolo.kitchensync.exception.NotFoundException;
import antoninopalazzolo.kitchensync.repository.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    // Lista paginata filtrata per la sezione dell'admin loggato
    public Page<Ingrediente> trovaTutti(Utente adminLoggato, int page, int size, String sortBy) {
        if (size > 30) size = 30;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ingredienteRepository.findBySezione(adminLoggato.getSezione(), pageable);
    }

    // Se non esiste lancio l'eccezione
    public Ingrediente trovaPerIdOException(UUID id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingrediente con id " + id + " non trovato"));
    }

    // Il nome deve essere univoco dentro la stessa sezione
    public Ingrediente salva(String nome, Utente adminLoggato) {
        Sezione sezione = adminLoggato.getSezione();
        if (ingredienteRepository.existsByNomeAndSezione(nome, sezione))
            throw new BadRequestException("Esiste già un ingrediente con il nome '" + nome + "' in questa sezione");
        return ingredienteRepository.save(new Ingrediente(nome, sezione));
    }

    // Modifico il nome — ricontrollo l'unicità dentro la sezione
    public Ingrediente modifica(UUID id, String nome, Utente adminLoggato) {
        Ingrediente ingrediente = trovaPerIdOException(id);
        if (ingredienteRepository.existsByNomeAndSezione(nome, adminLoggato.getSezione()))
            throw new BadRequestException("Esiste già un ingrediente con il nome '" + nome + "' in questa sezione");
        ingrediente.setNome(nome);
        return ingredienteRepository.save(ingrediente);
    }

    // Cambio solo il flag — è l'operazione più usata in assoluto durante il servizio
    public Ingrediente cambiaDisponibilita(UUID id, boolean disponibile) {
        Ingrediente ingrediente = trovaPerIdOException(id);
        ingrediente.setDisponibile(disponibile);
        return ingredienteRepository.save(ingrediente);
    }

    // Cancello per id — prima verifico che esista
    public void elimina(UUID id) {
        Ingrediente ingrediente = trovaPerIdOException(id);
        ingredienteRepository.delete(ingrediente);
    }
}