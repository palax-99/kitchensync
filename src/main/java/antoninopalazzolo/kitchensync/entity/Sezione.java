package antoninopalazzolo.kitchensync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "sezioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sezione {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private boolean attiva = true;

    public Sezione(String nome) {
        this.nome = nome;
        this.attiva = true;
    }
}