package antoninopalazzolo.kitchensync.security;

import antoninopalazzolo.kitchensync.entity.Utente;
import antoninopalazzolo.kitchensync.exception.UnauthorizedException;
import antoninopalazzolo.kitchensync.service.UtenteRuoloService;
import antoninopalazzolo.kitchensync.service.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Intercetto ogni richiesta HTTP e verifico il token JWT.
// Estendo OncePerRequestFilter — garantisce che questo filtro
// venga eseguito una sola volta per ogni richiesta.
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UtenteService utenteService;
    private final UtenteRuoloService utenteRuoloService;

    public JWTFilter(JWTTools jwtTools,
                     UtenteService utenteService,
                     UtenteRuoloService utenteRuoloService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
        this.utenteRuoloService = utenteRuoloService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Se manca o non inizia con "Bearer " rispondo con JSON pulito
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeErrorResponse(response, "Token mancante o malformato.");
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            // Verifico il token, estraggo l'id, recupero l'utente, carico i ruoli
            jwtTools.verifyToken(token);
            UUID utenteId = jwtTools.extractIdFromToken(token);
            Utente utente = utenteService.findById(utenteId);

            List<SimpleGrantedAuthority> authorities = utenteRuoloService.getAuthoritiesByUtente(utente);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(utente, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException ex) {
            // Token scaduto, firma sbagliata, formato invalido
            writeErrorResponse(response, ex.getMessage());
        } catch (Exception ex) {
            // Qualunque altra cosa imprevista
            writeErrorResponse(response, "Errore di autenticazione.");
        }
    }

    // Scrivo manualmente una response JSON con lo stesso formato dell'ErrorsHandler
    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format(
                "{\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message,
                LocalDateTime.now()
        );
        response.getWriter().write(json);
    }

    // Escludo dal filtro le rotte pubbliche — login, Swagger e OPTIONS per CORS
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return request.getMethod().equals("OPTIONS")
                || path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/v3/api-docs");
    }
}
