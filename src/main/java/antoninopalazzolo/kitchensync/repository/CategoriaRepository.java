package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Categoria;
import antoninopalazzolo.kitchensync.entity.Sezione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

    // Mi serve per verificare che il nome non sia già usato dentro la stessa sezione
    boolean existsByNomeAndSezione(String nome, Sezione sezione);


    // Mi serve per filtrare le categorie per sezione dell'admin loggato
    Page<Categoria> findBySezione(Sezione sezione, Pageable pageable);
}