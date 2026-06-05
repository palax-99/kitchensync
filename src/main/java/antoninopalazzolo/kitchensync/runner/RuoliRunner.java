package antoninopalazzolo.kitchensync.runner;


import antoninopalazzolo.kitchensync.entity.Ruolo;
import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.service.RuoloService;
import antoninopalazzolo.kitchensync.service.UtenteRuoloService;
import antoninopalazzolo.kitchensync.service.UtenteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Parte automaticamente all'avvio dell'applicazione.
// Popola il database con i ruoli e il SUPER_ADMIN di default.
@Component
@Order(1)
public class RuoliRunner implements CommandLineRunner {

    private final RuoloService ruoloService;
    private final UtenteService utenteService;
    private final UtenteRuoloService utenteRuoloService;
    private final PasswordEncoder passwordEncoder;

    public RuoliRunner(RuoloService ruoloService,
                       UtenteService utenteService,
                       UtenteRuoloService utenteRuoloService,
                       PasswordEncoder passwordEncoder) {
        this.ruoloService = ruoloService;
        this.utenteService = utenteService;
        this.utenteRuoloService = utenteRuoloService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Creo i tre ruoli se non esistono già
        Ruolo superAdmin = ruoloService.findOrCreate("SUPER_ADMIN");
        ruoloService.findOrCreate("ADMIN");
        Ruolo metre = ruoloService.findOrCreate("METRE");

        // Creo il SUPER_ADMIN di default se non esiste già
        if (!utenteService.existsByEmail("superadmin@kitchensync.com")) {
            Utente sa = new Utente(
                    "Super",
                    "Admin",
                    "superadmin@kitchensync.com",
                    passwordEncoder.encode("superadmin123")
            );
            Utente saSalvato = utenteService.save(sa);
            utenteRuoloService.assegnaRuolo(saSalvato, superAdmin);
        }

        // Creo il METRE di default se non esiste già
        if (!utenteService.existsByEmail("metre@kitchensync.com")) {
            Utente metreUtente = new Utente(
                    "Metre",
                    "Sala",
                    "metre@kitchensync.com",
                    passwordEncoder.encode("metre123")
            );
            Utente metreSalvato = utenteService.save(metreUtente);
            utenteRuoloService.assegnaRuolo(metreSalvato, metre);
        }
    }
}
