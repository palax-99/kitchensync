package antoninopalazzolo.kitchensync.repository;

import antoninopalazzolo.kitchensync.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

    // Mi serve per il login — cerco l'utente per email
    Optional<Utente> findByEmail(String email);
}
