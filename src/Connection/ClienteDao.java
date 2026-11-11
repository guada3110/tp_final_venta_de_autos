package Connection;

import Interface.IDAO;
import Models.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Exception.*;

public class ClienteDao implements IDAO<Cliente> {

    private Connection conn;

    //constructor
    public ClienteDao() throws PersistenciaException {
            try {
                conn=ConexionDB.getConexion();

            }catch (SQLException e){
                throw new PersistenciaException("No se pudo conectar con la base de datos", e);
            }
    }

    //metodos

    @Override
    public void crearTabla() throws PersistenciaException {

        String sql = "CREATE TABLE IF NOT EXISTS Cliente ("+
                "id INT NOT NULL, "+
                "nombre VARCHAR(50) NOT NULL, "+
                "apellido VARCHAR(15) NOT NULL, "+
                "dni VARCHAR(20) NOT NULL UNIQUE, "+
                "telefono VARCHAR(20) NOT NULL, "+
                 "email VARCHAR(100) NOT NULL, "+
                 "direccion VARCHAR(100) NOT NULL, "+
                 "fecha_alta DATE NOT NULL, "+
                 "PRIMARY KEY (id))";
        try (Statement st = conn.createStatement()){
            st.executeUpdate(sql);
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo crear la tabla de clientes", e);
        }
    }

    @Override
    public void guardar(Cliente elemento) throws PersistenciaException {

        String sql = "INSERT INTO clientes (id, nombre, apellido, dni, telefono, email, direccion, fecha_alta) "+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, elemento.getId());
            ps.setString(2, elemento.getNombre());
            ps.setString(3, elemento.getApellido());
            ps.setString(4, elemento.getDni());
            ps.setString(5, elemento.getTelefono());
            ps.setString(6, elemento.getEmail());
            ps.setString(7, elemento.getDireccion());
            ps.setDate(8, Date.valueOf(elemento.getFechaAlta()));

            ps.executeUpdate();
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo guardar el cliente", e);
        }
    }

    @Override
    public Cliente buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return mapear(rs);
                }
                return null;
            }

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo buscar el cliente con id "+id, e);
        }
    }

    @Override
    public List<Cliente> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM clientes";
        List<Cliente> clientes = new ArrayList<>();
        try (Statement st = conn.createStatement()){
            try (ResultSet rs= st.executeQuery(sql)){
                while (rs.next()){
                    clientes.add(mapear(rs));
                }
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudieron listar los clientes", e);
        }
        return clientes;
    }

    @Override
    public void actualizar(Cliente elemento) throws PersistenciaException {

        String sql = "UPDATE clientes SET nombre =?, apellido =?, dni =?, telefono =?, email =?, " +
                " direccion =?, fecha_alta =? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, elemento.getNombre());
            ps.setString(2, elemento.getApellido());
            ps.setString(3, elemento.getDni());
            ps.setString(4, elemento.getTelefono());
            ps.setString(5, elemento.getEmail());
            ps.setString(6, elemento.getDireccion());
            ps.setDate(7, Date.valueOf(elemento.getFechaAlta()));
            ps.setInt(8, elemento.getId());

            int filas = ps.executeUpdate();
            if(filas == 0){
                throw new PersistenciaException("No existe un cliente con id "+elemento.getId()+" para actualizar");
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo actualizar el cliente", e);
        }
    }


    @Override
    public void eliminar(Integer id) throws PersistenciaException {

        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if(filas == 0){
                throw new PersistenciaException("No existe un cliente con id "+id+ " para eliminar");
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo eliminar el cliente con id"+ id, e);
        }
    }

    @Override
    public void eliminarTabla() throws PersistenciaException {
        String sql = "DROP TABLE IF EXISTS clientes";
        try (Statement st = conn.createStatement()){
            st.executeUpdate(sql);
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo eliminar la tabla de clientes", e);
        }

    }

    @Override
    public void cerrarConexion() throws PersistenciaException {
        try {
            conn.close();
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo cerrar la conexion", e);
        }

    }

    // Convierte la fila actual del ResultSet en un objeto Cliente.
    // Usa el constructor que recibe el id, para que el cliente conserve el id guardado.
    private Cliente mapear(ResultSet rs) throws SQLException {
        try{
            return new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("dni"),
                    rs.getString("apellido"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    rs.getDate("fecha_alta").toLocalDate());
        }catch (DatosInvalidosException e){
            throw new SQLException("Datos invalidos en la tabla clientes: "+e.getMessage(), e);
        }
    }
}
