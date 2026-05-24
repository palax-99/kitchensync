package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Ingrediente;
import antoninopalazzolo.kitchensync.entity.Sezione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IngredienteRepository extends JpaRepository<Ingrediente, UUID> {

    // Mi serve per filtrare gli ingredienti per sezione dell'admin loggato
    Page<Ingrediente> findBySezione(Sezione sezione, Pageable pageable);

    // Mi serve per verificare che il nome non sia già usato dentro la stessa sezione
    boolean existsByNomeAndSezione(String nome, Sezione sezione);
}