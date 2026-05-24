package antoninopalazzolo.kitchensync.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// Un piatto del menu — appartiene a una categoria e quindi indirettamente a una sezione
@Entity
@Table(name = "piatti")
@Getter
@Setter
@NoArgsConstructor
public class Piatto {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nome;

    // Opzionale — il pizzaiolo può aggiungerla o no
    private String descrizione;

    @Column(nullable = false)
    private double prezzo;

    // Flag per il pizza builder — per ora false di default, lo implementerò poi
    @Column(nullable = false)
    private boolean personalizzabile = false;

    // Per ora null — lo popolo quando integrerò Cloudinary
    private String immagineUrl;

    // La categoria determina anche la sezione — "Margherita" sta in "Pizze classiche" che sta in "Pizzeria"
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Piatto(String nome, String descrizione, double prezzo, boolean personalizzabile, Categoria categoria) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.personalizzabile = personalizzabile;
        this.categoria = categoria;
    }
}