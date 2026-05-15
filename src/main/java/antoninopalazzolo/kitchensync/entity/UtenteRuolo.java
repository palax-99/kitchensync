package antoninopalazzolo.kitchensync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Tabella di mezzo esplicita tra Utente e Ruolo.
// Non uso @ManyToMany perché voglio controllo totale sulla tabella di collegamento.
@Getter
@NoArgsConstructor
@Entity
@Table(name = "utenti_ruoli")
public class UtenteRuolo {

    // UUID generato automaticamente da Hibernate
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Conosco l'utente a cui appartiene questo ruolo
    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    // Conosco il ruolo assegnato a quell'utente
    @ManyToOne
    @JoinColumn(name = "ruolo_id", nullable = false)
    private Ruolo ruolo;

    // Quando assegno un ruolo a un utente uso questo costruttore
    public UtenteRuolo(Utente utente, Ruolo ruolo) {
        this.utente = utente;
        this.ruolo = ruolo;
    }
}
