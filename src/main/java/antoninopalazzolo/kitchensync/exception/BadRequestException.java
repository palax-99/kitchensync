package antoninopalazzolo.kitchensync.exception;

// Lancio questa eccezione quando arriva una richiesta non valida.
// Nel ErrorsHandler la mapperò a HTTP 400.
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}