package antoninopalazzolo.kitchensync.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ruoli")
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ruolo_denominazione", nullable = false, unique = true)
    private String denominazione;

    public Ruolo(String denominazione) {
        this.denominazione = denominazione.toUpperCase();
    }
}
