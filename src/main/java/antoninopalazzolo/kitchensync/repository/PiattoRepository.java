package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Categoria;
import antoninopalazzolo.kitchensync.entity.Piatto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PiattoRepository extends JpaRepository<Piatto, UUID> {

    // Mi serve per filtrare i piatti per categoria
    Page<Piatto> findByCategoria(Categoria categoria, Pageable pageable);

    // Mi serve per verificare che il nome non sia già usato dentro la stessa categoria
    boolean existsByNomeAndCategoria(String nome, Categoria categoria);

    // Mi serve nel menu vivo — prendo tutti i piatti di una categoria senza paginazione
    List<Piatto> findByCategoria(Categoria categoria);
}