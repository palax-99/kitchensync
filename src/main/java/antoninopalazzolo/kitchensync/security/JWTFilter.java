package antoninopalazzolo.kitchensync.security;

import antoninopalazzolo.kitchensync.entity.Utente;
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
import java.util.List;
import java.util.UUID;

// Intercetto ogni richiesta HTTP e verifico il token JWT.
// Estendo OncePerRequestFilter — garantisce che questo filtro
// venga eseguito una sola volta per ogni richiesta.
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UtenteService utenteService;

    public JWTFilter(JWTTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Leggo l'header Authorization dalla richiesta
        String authHeader = request.getHeader("Authorization");

        // Se manca o non inizia con "Bearer " blocco tutto con 401
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mancante");
            return;
        }

        // Tolgo "Bearer " e tengo solo il token
        String token = authHeader.replace("Bearer ", "");

        // Verifico che il token sia valido — se non lo è lancia UnauthorizedException
        jwtTools.verifyToken(token);

        // Estraggo l'id dell'utente dal token
        UUID utenteId = jwtTools.extractIdFromToken(token);

        // Recupero l'utente dal database tramite l'id
        Utente utente = utenteService.findById(utenteId);

        // Carico i ruoli dell'utente direttamente dal database —
        // evito la bidirezionalità in Utente e gestisco i ruoli qui
        List<SimpleGrantedAuthority> authorities = utente.getAuthorities()
                .stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                .toList();

        // Creo l'oggetto di autenticazione e lo metto nel SecurityContext —
        // da questo momento Spring Security sa chi è l'utente loggato
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(utente, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Passo la richiesta al prossimo filtro nella catena
        filterChain.doFilter(request, response);
    }

    // Escludo dal filtro le rotte pubbliche — login e OPTIONS per CORS
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return request.getMethod().equals("OPTIONS")
                || path.startsWith("/auth");
    }
}
