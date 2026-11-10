package Exception;

public class GestionVentasException extends Exception {
    public GestionVentasException(String message) {
        super(message);
    }

    // Constructor que ademas guarda la excepcion original que causo el error
    public GestionVentasException(String message, Throwable causa) {
        super(message, causa);
    }
}
