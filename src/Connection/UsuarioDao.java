package Connection;

import Interface.IDAO;
import Models.Administrador;
import Models.Empleado;
import Models.Usuario;
import Exception.*;
import Enum.ERol;
import Models.Vendedor;

import javax.swing.text.AbstractDocument;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao implements IDAO<Usuario> {
    private Connection conn;

    private VendedorDao vendedorDao;

    //constructor
    public UsuarioDao(VendedorDao vendedorDao) throws PersistenciaException {
        this.vendedorDao = vendedorDao;
        try {
            conn = ConexionDB.getConexion();
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo conectar con la base de datos",e);
        }
    }

    //metodos

    // id_vendedor queda en NULL para los administradores
    // y tiene el id del vendedor para los empleados
    @Override
    public void crearTabla() throws PersistenciaException {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INT NOT NULL, "
                + "nombre_usuario VARCHAR(20) NOT NULL UNIQUE, "
                + "contrasenia VARCHAR(50) NOT NULL, "
                + "rol VARCHAR(20) NOT NULL, "
                + "id_vendedor INT NULL, "
                + "PRIMARY KEY (id), "
                + "FOREIGN KEY (id_vendedor) REFERENCES vendedores(id))";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear la tabla de usuarios", e);
        }
    }

    @Override
    public void guardar(Usuario elemento) throws PersistenciaException {

        String sql = "INSERT INTO usuarios (id, nombre_usuario, contrasenia, rol, id_vendedor) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, elemento.getId());
            ps.setString(2, elemento.getNombreUsuario());
            ps.setString(3, elemento.getContrasenia());
            ps.setString(4, elemento.getRol().name());
            completarVendedor(ps, elemento, 5);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el usuario", e);
        }
    }

    @Override
    public Usuario buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el usuario con id " + id, e);
        }
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) throws PersistenciaException {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el usuario " + nombreUsuario, e);
        }
    }
    @Override
    public List<Usuario> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Statement st = conn.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    usuarios.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron listar los usuarios", e);
        }
        return usuarios;
    }

    @Override
    public void actualizar(Usuario elemento) throws PersistenciaException {
        String sql = "UPDATE usuarios SET nombre_usuario = ?, contrasenia = ?, rol = ?, id_vendedor = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, elemento.getNombreUsuario());
            ps.setString(2, elemento.getContrasenia());
            ps.setString(3, elemento.getRol().name());
            completarVendedor(ps, elemento, 4);
            ps.setInt(5, elemento.getId());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No existe un usuario con id " + elemento.getId() + " para actualizar");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el usuario", e);
        }
    }

    @Override
    public void eliminar(Integer id) throws PersistenciaException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No existe un usuario con id " + id + " para eliminar");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar el usuario con id " + id, e);
        }
    }

    @Override
    public void eliminarTabla() throws PersistenciaException {
        String sql = "DROP TABLE IF EXISTS usuarios";

        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar la tabla de usuarios", e);
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

    // Completa la columna id_vendedor: si es un Empleado se guarda el id de su vendedor,
    // si es un Administrador queda en NULL. Se usa en guardar y en actualizar.
    private void completarVendedor(PreparedStatement ps, Usuario usuario, int posicion) throws SQLException{
        if(usuario instanceof Empleado){
            Empleado empleado = (Empleado) usuario;
            ps.setInt(posicion,empleado.getVendedor().getId());
        }else {
            ps.setNull(posicion, Types.INTEGER);
        }
    }

    // Convierte la fila actual del ResultSet en un Administrador o en un Empleado,
    // segun lo que diga la columna rol. Usa los constructores que reciben el id.
    private Usuario mapear(ResultSet rs) throws  SQLException, PersistenciaException {
        Integer id= rs.getInt("id");
        String nombreUsuario = rs.getString("nombre_usuario");
        String contrasenia = rs.getString("contasenia");
        ERol rol = ERol.valueOf(rs.getString("rol"));

        try{
            if(rol==ERol.ADMINISTRADOR){
                return new Administrador(id,nombreUsuario,contrasenia);
            }
            else {
                Vendedor vendedor = vendedorDao.buscarPorId(rs.getInt("id_vendedor"));
                return new Empleado(id, nombreUsuario, contrasenia, vendedor);
            }
        } catch (DatosInvalidosException e) {
            throw new SQLException("Datos invalidos en la tabla usuarios: " + e.getMessage(), e);
        }
    }

}
