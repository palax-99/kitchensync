package antoninopalazzolo.kitchensync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// Raggruppa i piatti dentro una sezione (es. "Pizze classiche" nella Pizzeria)
@Entity
@Table(name = "categorie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    // Ogni categoria appartiene a una sola sezione
    @ManyToOne
    @JoinColumn(name = "sezione_id", nullable = false)
    private Sezione sezione;

    // L'id lo genera JPA, la sezione è obbligatoria
    public Categoria(String nome, Sezione sezione) {
        this.nome = nome;
        this.sezione = sezione;
    }
}