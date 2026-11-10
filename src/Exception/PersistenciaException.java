package Exception;

public class PersistenciaException extends GestionVentasException {

    public PersistenciaException(String message) {
        super(message);
    }

    public PersistenciaException(String message, Throwable causa) {
        super(message, causa);
    }
}

