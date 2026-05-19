package antoninopalazzolo.kitchensync.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

// Gestore centralizzato delle eccezioni.
// @RestControllerAdvice intercetta le eccezioni lanciate da QUALSIASI controller
// e le trasforma in response HTTP pulite con JSON strutturato.
@RestControllerAdvice
public class ErrorsHandler {

    // 400 - Richiesta malformata o dati non validi a livello di business
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    // 401 - Token mancante, non valido, o credenziali errate
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), LocalDateTime.now()),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 404 - Risorsa non trovata nel database
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), LocalDateTime.now()),
                HttpStatus.NOT_FOUND
        );
    }

    // 400 - Errori di validazione dei DTO (@NotBlank, @Email, @Size, ecc.)
    // Spring Boot lancia questa eccezione automaticamente quando @Valid fallisce.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Costruisco un messaggio leggibile con tutti gli errori di validazione
        String errori = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return new ResponseEntity<>(
                new ErrorResponse(errori, LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    // Record interno per la struttura standard delle response di errore
    public record ErrorResponse(String message, LocalDateTime timestamp) {
    }
}