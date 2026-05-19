package antoninopalazzolo.kitchensync.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configurazione di Swagger / OpenAPI.
// Personalizzo titolo e descrizione, e attivo il Bearer Token in Swagger UI
// così posso testare gli endpoint protetti direttamente dal browser.
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kitchenSyncOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KitchenSync API")
                        .description("Backend API per KitchenSync — piattaforma gestionale SaaS per ristoranti.")
                        .version("v1"))
                // Dico a Swagger UI che TUTTE le richieste useranno il Bearer Token
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                // Definisco lo schema di sicurezza Bearer JWT
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}