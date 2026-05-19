package antoninopalazzolo.kitchensync.exception;

// Lancio questa eccezione quando una risorsa non esiste nel database.
// Nel ErrorsHandler la mapperò a HTTP 404.
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
