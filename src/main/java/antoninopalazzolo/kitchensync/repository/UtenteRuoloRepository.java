package antoninopalazzolo.kitchensync.repository;


import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.entity.UtenteRuolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UtenteRuoloRepository extends JpaRepository<UtenteRuolo, UUID> {
    List<UtenteRuolo> findByUtente(Utente utente);

    // Mi serve per eliminare tutti i ruoli di un utente prima di cancellarlo
    void deleteByUtente(Utente utente);
}