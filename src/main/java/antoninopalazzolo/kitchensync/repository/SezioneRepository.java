package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Sezione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

// Repository per le operazioni sul database delle sezioni
public interface SezioneRepository extends JpaRepository<Sezione, UUID> {

    // Mi serve per controllare se esiste già una sezione con lo stesso nome prima di crearne una nuova
    Optional<Sezione> findByNome(String nome);

    // Mi serve per verificare l'unicità del nome escludendo la sezione che sto modificando
    boolean existsByNome(String nome);
}