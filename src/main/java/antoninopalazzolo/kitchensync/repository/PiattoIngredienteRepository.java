package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Ingrediente;
import antoninopalazzolo.kitchensync.entity.Piatto;
import antoninopalazzolo.kitchensync.entity.PiattoIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

// Query per la tabella di mezzo piatti_ingredienti
public interface PiattoIngredienteRepository extends JpaRepository<PiattoIngrediente, UUID> {

    // Mi serve per trovare tutti gli ingredienti di un piatto
    List<PiattoIngrediente> findByPiatto(Piatto piatto);

    // Mi serve per verificare che il collegamento non esista già
    boolean existsByPiattoAndIngrediente(Piatto piatto, Ingrediente ingrediente);

    // Mi serve in una funzione dell'applicazione che implemneterò successivamente — trovo tutti i piatti che usano un certo ingrediente
    List<PiattoIngrediente> findByIngrediente(Ingrediente ingrediente);

    // Mi serve per eliminare tutti i collegamenti di un ingrediente prima di cancellarlo
    void deleteByIngrediente(Ingrediente ingrediente);
}