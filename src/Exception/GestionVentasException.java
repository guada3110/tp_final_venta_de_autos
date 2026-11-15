package Exception;
// Esta es la exception padre de la cual salen todas las demas exceptions
public class GestionVentasException extends Exception {
    public GestionVentasException(String message) {
        super(message);
    }

    // Constructor que ademas guarda la excepcion original que causo el error
    public GestionVentasException(String message, Throwable causa) {
        super(message, causa);
    }
}
