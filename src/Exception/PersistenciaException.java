package Exception;

public class PersistenciaException extends GestionVentasException {
    public PersistenciaException(String message) {
        super(message);
    }
    //falta agregar el que lanza la causa (Throwable)
}
