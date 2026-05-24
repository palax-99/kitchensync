package antoninopalazzolo.kitchensync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// Ingrediente gestito per sezione — quando non è disponibile i piatti che lo usano spariscono dal menu
@Entity
@Table(name = "ingredienti")
@Getter
@Setter
@NoArgsConstructor
public class Ingrediente {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    // Se false, tutti i piatti che usano questo ingrediente spariscono dal menu vivo
    @Column(nullable = false)
    private boolean disponibile = true;

    // Ogni ingrediente appartiene a una sezione — il pizzaiolo gestisce i suoi, lo chef i suoi
    @ManyToOne
    @JoinColumn(name = "sezione_id", nullable = false)
    private Sezione sezione;

    // L'id lo genera JPA, disponibile parte true di default
    public Ingrediente(String nome, Sezione sezione) {
        this.nome = nome;
        this.sezione = sezione;
        this.disponibile = true;
    }
}