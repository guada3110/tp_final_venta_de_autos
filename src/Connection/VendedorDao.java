package Connection;

import Interface.IDAO;
import Models.Vendedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Exception.*;

public class VendedorDao implements IDAO<Vendedor> {
    private Connection conn;

    //constructor
    public VendedorDao() throws PersistenciaException {
        try {
            conn=ConexionDB.getConexion();
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo conectar con la base de datos", e);
        }
    }

    @Override
    public void crearTabla() throws PersistenciaException {
        String sql = "CREATE TABLE IF NOT EXISTS vendedores ("
                + "id INT NOT NULL, "
                + "nombre VARCHAR(50) NOT NULL, "
                + "apellido VARCHAR(50) NOT NULL, "
                + "dni VARCHAR(15) NOT NULL UNIQUE, "
                + "telefono VARCHAR(20) NOT NULL, "
                + "email VARCHAR(100) NOT NULL, "
                + "legajo VARCHAR(20) NOT NULL UNIQUE, "
                + "porcentaje_comision DOUBLE NOT NULL, "
                + "PRIMARY KEY (id))";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear la tabla de vendedores", e);
        }
    }

    @Override
    public void guardar(Vendedor elemento) throws PersistenciaException {
        String sql = "INSERT INTO vendedores (id, nombre, apellido, dni, telefono, email, legajo, porcentaje_comision) "+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, elemento.getId());
            ps.setString(2, elemento.getNombre());
            ps.setString(3, elemento.getApellido());
            ps.setString(4, elemento.getDni());
            ps.setString(5, elemento.getTelefono());
            ps.setString(6, elemento.getEmail());
            ps.setString(7, elemento.getLegajo());
            ps.setDouble(8, elemento.getPorcentajeComision());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el vendedor", e);
        }
    }


    @Override
    public Vendedor buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM vendedores WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el vendedor con id " + id, e);
        }
    }

    @Override
    public List<Vendedor> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM vendedores";
        List<Vendedor> vendedores = new ArrayList<>();

        try (Statement st = conn.createStatement()) {

            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    vendedores.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron listar los vendedores", e);
        }
        return vendedores;
    }

    @Override
    public void actualizar(Vendedor elemento) throws PersistenciaException {

        String sql = "UPDATE vendedores SET nombre = ?, apellido = ?, dni = ?, telefono = ?, email = ?, "
                + "legajo = ?, porcentaje_comision = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, elemento.getNombre());
            ps.setString(2, elemento.getApellido());
            ps.setString(3, elemento.getDni());
            ps.setString(4, elemento.getTelefono());
            ps.setString(5, elemento.getEmail());
            ps.setString(6, elemento.getLegajo());
            ps.setDouble(7, elemento.getPorcentajeComision());
            ps.setInt(8, elemento.getId());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No existe un vendedor con id " + elemento.getId() + " para actualizar");
            }

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el vendedor", e);
        }
    }

    @Override
    public void eliminar(Integer id) throws PersistenciaException {
        String sql = "DELETE FROM vendedores WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No existe un vendedor con id " + id + " para eliminar");
            }

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar el vendedor con id " + id, e);
        }
    }

    @Override
    public void eliminarTabla() throws PersistenciaException {
        String sql = "DROP TABLE IF EXISTS vendedores";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar la tabla de vendedores", e);
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

    private Vendedor mapear(ResultSet rs) throws SQLException {
        try {
            return new Vendedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("dni"),
                    rs.getString("apellido"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("legajo"),
                    rs.getDouble("porcentaje_comision"));
        } catch (DatosInvalidosException e) {
            // Si los datos guardados no pasan las validaciones de la clase,
            // se avisa como un error al leer la base
            throw new SQLException("Datos invalidos en la tabla vendedores: " + e.getMessage(), e);
        }
    }
}

