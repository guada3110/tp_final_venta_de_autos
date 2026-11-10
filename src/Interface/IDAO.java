package Interface;

import Exception.PersistenciaException;

import java.util.List;
import java.util.Set;

public interface IDAO <T> {

    void crearTabla() throws PersistenciaException;

    void guardar (T elemento)  throws PersistenciaException;

    T buscarPorId( Integer id) throws PersistenciaException;

    List<T> listarTodos()  throws PersistenciaException;

    void actualizar (T elemento)  throws PersistenciaException;

    void eliminar (Integer id)  throws PersistenciaException;

    void eliminarTabla ()  throws PersistenciaException;

    void cerrarConexion()  throws PersistenciaException;

}
