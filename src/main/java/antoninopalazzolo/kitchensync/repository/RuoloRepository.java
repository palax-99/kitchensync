package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, UUID> {

    // Mi serve nel Runner per trovare un ruolo per denominazione
    // senza crearlo se esiste già
    Optional<Ruolo> findByDenominazione(String denominazione);
}