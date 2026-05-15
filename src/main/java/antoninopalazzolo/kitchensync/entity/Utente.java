package antoninopalazzolo.kitchensync.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

// Personale interno del ristorante.
// Implemento UserDetails per far capire a Spring Security chi è l'utente loggato.
@Getter
@NoArgsConstructor
@Entity
@Table(name = "utenti")
public class Utente implements UserDetails {

    // UUID generato automaticamente da Hibernate
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    // L'email è il campo usato per il login — la metto come unica
    @Column(nullable = false, unique = true)
    private String email;

    // Verrà hashata con BCrypt nel service — mai salvata in chiaro
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    // URL immagine su Cloudinary — opzionale, quindi nessun nullable = false
    private String avatar;

    // Costruttore che uso quando creo un nuovo utente.
    // L'id lo genera Hibernate da solo, l'avatar si aggiunge dopo.
    public Utente(String nome, String cognome, String email, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    // Spring Security chiama questo metodo per sapere i permessi dell'utente.
    // Per ora ritorna lista vuota — la completerò quando avrò UtenteRuolo.
    // Che sarebbe l'entità di collegamento fra Utente e Ruolo
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // Dico a Spring Security che l' "username" è l'email
    @Override
    public String getUsername() {
        return this.email;
    }
}