package Interface;

import Exception.PersistenciaException;

import java.util.List;
import java.util.Set;
// Metodos genericos que utilizan las clases DAO
public interface IDAO <T> {

    // Se crea la tabla en la base de datos
    void crearTabla() throws PersistenciaException;
    // Ayuda a guardar elementos
    void guardar (T elemento)  throws PersistenciaException;
    // Busca en la tabla por id
    T buscarPorId( Integer id) throws PersistenciaException;
    // Muestra a todos los elementos
    List<T> listarTodos()  throws PersistenciaException;
    // actualiza un elemento seleccionado
    void actualizar (T elemento)  throws PersistenciaException;
    // Elimina un elemento buscandolo por ID
    void eliminar (Integer id)  throws PersistenciaException;
    //Elimina directamente la tabla
    void eliminarTabla ()  throws PersistenciaException;
    // Se utiliza para la coneccion con MySql
    void cerrarConexion()  throws PersistenciaException;

}
