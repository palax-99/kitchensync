package antoninopalazzolo.kitchensync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// Tabella di mezzo tra Piatto e Ingrediente — niente @ManyToMany, la gestisco esplicitamente
@Entity
@Table(name = "piatti_ingredienti")
@Getter
@Setter
@NoArgsConstructor
public class PiattoIngrediente {

    @Id
    @GeneratedValue
    private UUID id;

    // Il piatto a cui appartiene questo ingrediente
    @ManyToOne
    @JoinColumn(name = "piatto_id", nullable = false)
    private Piatto piatto;

    // L'ingrediente collegato al piatto
    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;
    
    public PiattoIngrediente(Piatto piatto, Ingrediente ingrediente) {
        this.piatto = piatto;
        this.ingrediente = ingrediente;
    }
}