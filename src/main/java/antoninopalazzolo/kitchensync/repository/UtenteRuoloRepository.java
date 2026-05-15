package antoninopalazzolo.kitchensync.repository;


import antoninopalazzolo.kitchensync.entity.UtenteRuolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UtenteRuoloRepository extends JpaRepository<UtenteRuolo, UUID> {
}