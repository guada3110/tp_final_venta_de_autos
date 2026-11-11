package Connection;
import Exception.*;
import Interface.IDAO;
import Models.Cliente;
import Models.Vehiculo;
import Models.Vendedor;
import Models.Venta;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import Enum.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO implements IDAO<Venta> {
 private Connection conn;
 private ClienteDao clientedao;
 private VehiculoDao vehiculoDao;
 private VendedorDao vendedorDao;

 //CONSTRUCTOR
    public VentaDAO(ClienteDao clientedao,VehiculoDao vehiculoDao,VendedorDao vendedorDao) throws PersistenciaException {
       this.clientedao=clientedao;
       this.vehiculoDao=vehiculoDao;
       this.vendedorDao=vendedorDao;

        try {
            conn=ConexionDB.getConexion();

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo conectar con la base de datos", e);
        }
    }
    @Override
    public void crearTabla() throws PersistenciaException {
        String sql = "CREATE TABLE IF NOT EXISTS Ventas ("+
                "id INT NOT NULL, "+
                "fecha DATE NOT NULL, "+
                "id_cliente INT NOT NULL, "+
                "id_vendedor INT NOT NULL, "+
                "id_vehiculo INT NOT NULL, "+
                "precio_final DOUBLE NOT NULL, "+
                "metodo_pago VARCHAR(20) NOT NULL, "+
                "PRIMARY KEY(id), "+
                "FOREIGN KEY (id_cliente)REFERENCES clientes(id), "+
                "FOREIGN KEY (id_vendedor)REFERENCES vendedores(id), "+
                "FOREIGN KEY (id_vehiculo)REFERENCES vehiculos(id), ";

        try (Statement st = conn.createStatement()){
            st.executeUpdate(sql);
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo crear la tabla de ventas", e);
        }
    }
    @Override
    public void guardar(Venta elemento) throws PersistenciaException {

        String sql="INSERT INTO ventas (id,fecha,id_cliente,id_vendedor,id_vechiculo,precio_final,metodo_pago) "+
        "VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,elemento.getId());
            ps.setDate(2, Date.valueOf(elemento.getFecha()));
            ps.setInt(3,elemento.getCliente().getId());
            ps.setInt(4,elemento.getVendedor().getId());
            ps.setInt(5,elemento.getVehiculo().getId());
            ps.setDouble(6,elemento.getPrecioFinal());
            ps.setString(7,elemento.getMetodoPago().name());
            ps.executeUpdate();

        }catch (SQLException e ){
            throw new PersistenciaException("No se pudo guardar la venta", e);
        }

    }

    @Override
    public Venta buscarPorId(Integer id) throws PersistenciaException {
        String sql="SELECT * FROM ventas WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return mapear(rs);
                }
                return null;
            }

        }catch (SQLException e){
            throw new PersistenciaException("No se pudo buscar la venta con id "+id , e);
        }

    }

    @Override
    public List<Venta> listarTodos() throws PersistenciaException {
        String sql = "SELECT * FROM ventas";
        List<Venta> ventas = new ArrayList<>();
        try (Statement st = conn.createStatement()){
            try (ResultSet rs= st.executeQuery(sql)){
                while (rs.next()){
                    ventas.add(mapear(rs));
                }
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudieron listar las ventas", e);
        }
        return ventas;
    }
    public List<Venta> listarPorVendedor(Integer idVendedor)throws PersistenciaException{
        String sql="SELECT * FROM ventas WHERE id_vendedor=?";
        List <Venta> ventas= new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,idVendedor);
            try (ResultSet rs= ps.executeQuery()){
                while (rs.next()){
                    ventas.add(mapear(rs));
                }
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudieron listar las ventas del vendedor: "+idVendedor, e);

        }
        return ventas;
    }
    public List<Venta> listarPorCliente(Integer idCliente)throws PersistenciaException{
        String sql="SELECT * FROM ventas WHERE id_cliente=?";
        List <Venta> ventas= new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,idCliente);
            try (ResultSet rs= ps.executeQuery()){
                while (rs.next()){
                    ventas.add(mapear(rs));
                }
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudieron listar las compas del cliente: "+idCliente, e);

        }
        return ventas;
    }

    @Override
    public void actualizar(Venta elemento) throws PersistenciaException {
        String sql="UPDATE ventas SET fecha= ?, id_cliente= ? , id_vendedor= ?, id_vehiculo= ?, precio_final= ?, "+
                "metodo_pago= ? WHERE id=? ";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setDate(1,Date.valueOf(elemento.getFecha()));
            ps.setInt(2,elemento.getCliente().getId());
            ps.setInt(3,elemento.getVendedor().getId());
            ps.setInt(4,elemento.getVehiculo().getId());
            ps.setDouble(5,elemento.getPrecioFinal());
            ps.setString(6,elemento.getMetodoPago().name());
            ps.setInt(7,elemento.getId());
            int filas=ps.executeUpdate();
            if( filas==0){
                throw new PersistenciaException("no existe una venta  para actualizar con el id: "+elemento.getId());
            }
        } catch (SQLException e){
            throw new PersistenciaException("no se pudo actualizar la venta ",e );
        }

    }

    @Override
    public void eliminar(Integer id) throws PersistenciaException {

        String sql = "DELETE FROM ventas WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if(filas == 0){
                throw new PersistenciaException("No existe una venta  con id "+id+ " para eliminar");
            }
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo eliminar la venta  con id"+ id, e);
        }
    }

    @Override
    public void eliminarTabla() throws PersistenciaException {
        String sql = "DROP TABLE IF EXISTS ventas";
        try (Statement st = conn.createStatement()){
            st.executeUpdate(sql);
        }catch (SQLException e){
            throw new PersistenciaException("No se pudo eliminar la tabla de ventas", e);
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
    private Venta mapear(ResultSet rs) throws SQLException,PersistenciaException{
        Integer id_cliente=rs.getInt("id_cliente");
        Integer id_vendedor=rs.getInt("id_vendedor");
        Integer id_vehiculo=rs.getInt("id_vechiculo");
        Cliente cliente=clientedao.buscarPorId(id_cliente);
        Vendedor vendedor=vendedorDao.buscarPorId(id_vendedor);
        Vehiculo vehiculo=vehiculoDao.buscarPorId(id_vehiculo);
        try{
            return new Venta(
                    rs.getInt("id"),rs.getDate("fecha").toLocalDate(),cliente,vendedor,rs.getDouble("precio_final"),
                    MetodoPago.valueOf(rs.getString("metodo_pago")),vehiculo);
        }catch (DatosInvalidosException e ){
            throw new SQLException("datos invalidos en la tabla ventas: "+ e.getMessage(),e);
        }
    }





}
