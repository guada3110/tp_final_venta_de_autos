package Connection;

import Interface.IDAO;
import Models.Auto;
import Models.Camioneta;
import Models.Moto;
import Models.Vehiculo;
import Enum.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Exception.*;

public class VehiculoDao implements IDAO<Vehiculo> {
    private Connection conn;

    //constructor
    public VehiculoDao() throws PersistenciaException {
        try {
            conn = ConexionDB.getConexion();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo conectar con la base de datos", e);
        }
    }

    //metodos

    @Override
    public void crearTabla() throws PersistenciaException {
        String sql = "CREATE TABLE IF NOT EXISTS vehiculos ("
                + "id INT NOT NULL, "
                + "tipo VARCHAR(20) NOT NULL, "
                + "marca VARCHAR(50) NOT NULL, "
                + "modelo VARCHAR(50) NOT NULL, "
                + "anio INT NOT NULL, "
                + "precio_base DOUBLE NOT NULL, "
                + "kilometraje INT NOT NULL, "
                + "estado VARCHAR(20) NOT NULL, "
                + "combustible VARCHAR(20) NOT NULL, "
                + "cantidad_puertas INT NULL, "       // solo Auto
                + "tipo_carroceria VARCHAR(30) NULL, " // solo Auto
                + "cilindrada INT NULL, "             // solo Moto
                + "capacidad_carga DOUBLE NULL, "     // solo Camioneta
                + "traccion_4x4 BOOLEAN NULL, "       // solo Camioneta
                + "PRIMARY KEY (id))";
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear la tabla de vehiculos", e);
        }
    }

    @Override
    public void guardar(Vehiculo vehiculo) throws PersistenciaException {
        String sql = "INSERT INTO vehiculos (id, tipo, marca, modelo, anio, precio_base, kilometraje, estado, combustible, " +
                "cantidad_puertas, tipo_carroceria, cilindrada, capacidad_carga, traccion_4x4) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehiculo.getId());
            ps.setString(2, obtenerTipo(vehiculo));
            ps.setString(3, vehiculo.getMarca());
            ps.setString(4, vehiculo.getModelo());
            ps.setInt(5, vehiculo.getAnio());
            ps.setDouble(6, vehiculo.getPrecioBase());
            ps.setInt(7, vehiculo.getKilometraje());
            // Los enums se guardan como texto con name(), por ejemplo "DISPONIBLE"
            ps.setString(8, vehiculo.getEstado().name());
            ps.setString(9, vehiculo.getCombustible().name());
            // Las columnas 10 a 14 dependen del tipo de vehiculo
            completarDatosSegunTipo(ps, vehiculo, 10);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el vehiculo", e);
        }
    }

    @Override
    public Vehiculo buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM vehiculos WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo buscar el vehiculo con id " + id, e);
        }
    }


    @Override
    public List<Vehiculo> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM vehiculos";
        List<Vehiculo> vehiculos = new ArrayList<>();
        try (Statement st = conn.createStatement()) {

            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    vehiculos.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron listar los vehiculos", e);
        }
        return vehiculos;
    }

    // Devuelve los vehiculos que tienen el estado recibido (por ejemplo, solo los DISPONIBLES)
    public List<Vehiculo> listarPorEstado(EstadoVehiculo estado) throws PersistenciaException {
        String sql = "SELECT * FROM vehiculos WHERE estado = ?";
        List<Vehiculo> vehiculos = new ArrayList<>();

        // Se usa PreparedStatement porque el estado va como parametro
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vehiculos.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron listar los vehiculos con estado " + estado, e);
        }
        return vehiculos;
    }

    @Override
    public void actualizar(Vehiculo vehiculo) throws PersistenciaException {
        String sql = "UPDATE vehiculos SET marca = ?, modelo = ?, anio = ?, precio_base = ?, kilometraje = ?, "
                + "estado = ?, combustible = ?, cantidad_puertas = ?, tipo_carroceria = ?, cilindrada = ?, "
                + "capacidad_carga = ?, traccion_4x4 = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehiculo.getMarca());
            ps.setString(2, vehiculo.getModelo());
            ps.setInt(3, vehiculo.getAnio());
            ps.setDouble(4, vehiculo.getPrecioBase());
            ps.setInt(5, vehiculo.getKilometraje());
            ps.setString(6, vehiculo.getEstado().name());
            ps.setString(7, vehiculo.getCombustible().name());
            // Las columnas 8 a 12 dependen del tipo de vehiculo
            completarDatosSegunTipo(ps, vehiculo, 8);
            ps.setInt(13, vehiculo.getId());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No existe un vehiculo con id " + vehiculo.getId() + " para actualizar");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el vehiculo", e);
        }
    }

    @Override
    public void eliminar(Integer id) throws PersistenciaException {
        String sql = "DELETE FROM vehiculos WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new PersistenciaException("No existe un vehiculo con id " + id + " para eliminar");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar el vehiculo con id " + id, e);
        }
    }

    @Override
    public void eliminarTabla() throws PersistenciaException {
        String sql = "DROP TABLE IF EXISTS vehiculos";

        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar la tabla de vehiculos", e);
        }
    }

    @Override
    public void cerrarConexion() throws PersistenciaException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo cerrar la conexion", e);
        }
    }

    // Devuelve el texto que se guarda en la columna tipo segun la clase del vehiculo
    private String obtenerTipo(Vehiculo vehiculo) {
        if (vehiculo instanceof Auto) {
            return "AUTO";
        } else if (vehiculo instanceof Moto) {
            return "MOTO";
        } else {
            return "CAMIONETA";
        }
    }

    // Completa los 5 parametros de las columnas especificas de cada tipo
    // (cantidad_puertas, tipo_carroceria, cilindrada, capacidad_carga, traccion_4x4),
    // empezando desde la posicion "inicio" del PreparedStatement.
    // Las columnas que no corresponden al tipo se guardan en NULL con setNull.
    // Se usa en guardar y en actualizar, para no repetir el mismo codigo.
    private void completarDatosSegunTipo(PreparedStatement ps, Vehiculo vehiculo, int inicio) throws SQLException {
        // Primero se ponen las 5 columnas en NULL
        ps.setNull(inicio, Types.INTEGER);       // cantidad_puertas
        ps.setNull(inicio + 1, Types.VARCHAR);   // tipo_carroceria
        ps.setNull(inicio + 2, Types.INTEGER);   // cilindrada
        ps.setNull(inicio + 3, Types.DOUBLE);    // capacidad_carga
        ps.setNull(inicio + 4, Types.BOOLEAN);   // traccion_4x4

        // Despues se completan solo las del tipo que corresponde
        if (vehiculo instanceof Auto) {
            Auto auto = (Auto) vehiculo;
            ps.setInt(inicio, auto.getCantidadPuertas());
            ps.setString(inicio + 1, auto.getTipoCarroceria());
        } else if (vehiculo instanceof Moto) {
            Moto moto = (Moto) vehiculo;
            ps.setInt(inicio + 2, moto.getCilindrada());
        } else if (vehiculo instanceof Camioneta) {
            Camioneta camioneta = (Camioneta) vehiculo;
            ps.setDouble(inicio + 3, camioneta.getCapacidadDeCarga());
            ps.setBoolean(inicio + 4, camioneta.isTraccion4x4());
        }
    }

    // segun lo que diga la columna tipo. Usa los constructores que reciben el id.
    private Vehiculo mapear(ResultSet rs) throws SQLException {
        // Datos comunes a todos los vehiculos
        Integer id = rs.getInt("id");
        String tipo = rs.getString("tipo");
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        Integer anio = rs.getInt("anio");
        Double precioBase = rs.getDouble("precio_base");
        Integer kilometraje = rs.getInt("kilometraje");
        // Los enums se leen como texto y se convierten con valueOf
        EstadoVehiculo estado = EstadoVehiculo.valueOf(rs.getString("estado"));
        TipoCombustible combustible = TipoCombustible.valueOf(rs.getString("combustible"));

        try {
            if (tipo.equals("AUTO")) {
                return new Auto(id, marca, modelo, anio, precioBase, kilometraje, estado, combustible,
                        rs.getInt("cantidad_puertas"), rs.getString("tipo_carroceria"));
            } else if (tipo.equals("MOTO")) {
                return new Moto(id, marca, modelo, anio, precioBase, kilometraje, estado, combustible,
                        rs.getInt("cilindrada"));
            } else if (tipo.equals("CAMIONETA")) {
                return new Camioneta(id, marca, modelo, anio, precioBase, kilometraje, estado, combustible,
                        rs.getDouble("capacidad_carga"), rs.getBoolean("traccion_4x4"));
            } else {
                throw new SQLException("Tipo de vehiculo desconocido en la tabla: " + tipo);
            }
        } catch (DatosInvalidosException e) {
            // Si los datos guardados no pasan las validaciones de la clase,
            // se avisa como un error al leer la base
            throw new SQLException("Datos invalidos en la tabla vehiculos: " + e.getMessage(), e);
        }
    }
}

