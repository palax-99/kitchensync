package antoninopalazzolo.kitchensync.exception;

// Lancio questa eccezione quando il token JWT è mancante, non valido o scaduto.
// Nel ErrorsHandler la mapperò a HTTP 401.
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}