package Controller;
import Connection.ClienteDao;
import Connection.VehiculoDao;
import Connection.VendedorDao;
import Connection.VentaDAO;
import Enum.EstadoVehiculo;
import Enum.MetodoPago;
import Exception.*;
import Models.Cliente;
import Models.Vehiculo;
import Models.Vendedor;
import Models.Venta;

import java.util.ArrayList;
import java.util.List;

public class GestorVentas {

    // Dao: guardan y leen los datos de la base
    private ClienteDao clienteDao;
    private VendedorDao vendedorDao;
    private VehiculoDao vehiculoDao;
    private VentaDAO ventaDao;

    // Registros: tienen los datos en memoria mientras el programa esta abierto
    private Registro<Cliente> clientes;
    private Registro<Vendedor> vendedores;
    private Registro<Vehiculo> vehiculos;
    private Registro<Venta> ventas;

    // Constructor: abre las conexiones de los Dao y crea los registros vacios
    public GestorVentas() throws PersistenciaException {
        this.clienteDao = new ClienteDao();
        this.vendedorDao = new VendedorDao();
        this.vehiculoDao = new VehiculoDao();
        this.ventaDao = new VentaDAO(clienteDao, vehiculoDao, vendedorDao);

        this.clientes = new Registro<>();
        this.vendedores = new Registro<>();
        this.vehiculos = new Registro<>();
        this.ventas = new Registro<>();
    }

    // ---------------------------------------------------------------
    // INICIO Y CIERRE
    // ---------------------------------------------------------------

    // Se llama una sola vez al arrancar el programa:
    // crea las tablas si no existen y carga en memoria lo que ya esta guardado.
    public void iniciar() throws GestionVentasException {
        // La tabla ventas va ultima porque sus FOREIGN KEY apuntan a las otras tres
        clienteDao.crearTabla();
        vendedorDao.crearTabla();
        vehiculoDao.crearTabla();
        ventaDao.crearTabla();

        cargarDatos();
    }

    // Lee todo lo que hay en la base y lo guarda en los registros.
    // Al crear cada objeto con el constructor que recibe el id, los contadores
    // quedan actualizados, asi los objetos nuevos no repiten ids.
    private void cargarDatos() throws GestionVentasException {
        for (Cliente c : clienteDao.listarTodos()) {
            clientes.agregar(c);
        }
        for (Vendedor v : vendedorDao.listarTodos()) {
            vendedores.agregar(v);
        }
        for (Vehiculo v : vehiculoDao.listarTodos()) {
            vehiculos.agregar(v);
        }
        for (Venta v : ventaDao.listarTodos()) {
            ventas.agregar(v);
        }
    }

    // Cierra las conexiones de todos los Dao. Se llama al salir del programa.
    public void cerrar() throws PersistenciaException {
        ventaDao.cerrarConexion();
        vehiculoDao.cerrarConexion();
        vendedorDao.cerrarConexion();
        clienteDao.cerrarConexion();
    }

    // ---------------------------------------------------------------
    // ALTAS
    // ---------------------------------------------------------------

    // Registra un vehiculo nuevo: primero en la base y despues en memoria
    public void registrarVehiculo(Vehiculo vehiculo) throws GestionVentasException {
        if (vehiculo == null) {
            throw new DatosInvalidosException("El vehiculo no puede estar vacio");
        }
        vehiculoDao.guardar(vehiculo);
        vehiculos.agregar(vehiculo);
    }

    // Registra un cliente nuevo. No se permiten dos clientes con el mismo DNI.
    public void registrarCliente(Cliente cliente) throws GestionVentasException {
        if (cliente == null) {
            throw new DatosInvalidosException("El cliente no puede estar vacio");
        }
        for (Cliente c : clientes.listar()) {
            if (c.getDni().equals(cliente.getDni())) {
                throw new DatosInvalidosException("Ya existe un cliente con el DNI " + cliente.getDni());
            }
        }
        clienteDao.guardar(cliente);
        clientes.agregar(cliente);
    }

    // Registra un vendedor nuevo. No se permiten dos vendedores con el mismo legajo.
    public void registrarVendedor(Vendedor vendedor) throws GestionVentasException {
        if (vendedor == null) {
            throw new DatosInvalidosException("El vendedor no puede estar vacio");
        }
        for (Vendedor v : vendedores.listar()) {
            if (v.getLegajo().equals(vendedor.getLegajo())) {
                throw new DatosInvalidosException("Ya existe un vendedor con el legajo " + vendedor.getLegajo());
            }
        }
        vendedorDao.guardar(vendedor);
        vendedores.agregar(vendedor);
    }

    // Registra una venta. Pasos:
    // 1. Busca el vehiculo, el cliente y el vendedor (si alguno no existe, lanza una excepcion).
    // 2. Verifica que el vehiculo este disponible.
    // 3. Crea la venta (el precio final lo calcula el vehiculo).
    // 4. Marca el vehiculo como vendido y lo actualiza en la base.
    // 5. Guarda la venta en la base y en memoria.
    // Se valida todo en Java antes de tocar la base, para que solo pueda fallar por un error de conexion.
    public Venta registrarVenta(Integer idVehiculo, Integer idCliente, Integer idVendedor, MetodoPago metodoPago) throws GestionVentasException {
        Vehiculo vehiculo = vehiculos.buscarPorId(idVehiculo);
        Cliente cliente = clientes.buscarPorId(idCliente);
        Vendedor vendedor = vendedores.buscarPorId(idVendedor);

        if (!vehiculo.estaDisponible()) {
            throw new VehiculoNoDisponibleException("El vehiculo " + vehiculo.getMarca() + " " + vehiculo.getModelo() + " no esta disponible");
        }

        Venta venta = new Venta(cliente, vendedor, metodoPago, vehiculo);

        vehiculo.marcarComoVendido();
        vehiculoDao.actualizar(vehiculo);

        try {
            ventaDao.guardar(venta);
        } catch (PersistenciaException e) {
            // Si no se pudo guardar la venta, el vehiculo vuelve a quedar disponible,
            // asi no queda un auto vendido sin una venta registrada
            vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);
            vehiculoDao.actualizar(vehiculo);
            throw e;
        }

        ventas.agregar(venta);
        return venta;
    }

    // ---------------------------------------------------------------
    // BUSQUEDAS
    // ---------------------------------------------------------------

    // Busca un cliente por su DNI
    public Cliente buscarClientePorDni(String dni) throws EntidadNoEncontradaException {
        for (Cliente c : clientes.listar()) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        throw new EntidadNoEncontradaException("No existe un cliente con el DNI " + dni);
    }

    // Devuelve los vehiculos que estan disponibles para la venta
    public List<Vehiculo> listarDisponibles() {
        List<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo v : vehiculos.listar()) {
            if (v.estaDisponible()) {
                disponibles.add(v);
            }
        }
        return disponibles;
    }

    // Devuelve los vehiculos de una marca (sin importar mayusculas o minusculas)
    public List<Vehiculo> buscarPorMarca(String marca) {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos.listar()) {
            if (v.getMarca().equalsIgnoreCase(marca)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // Devuelve las compras que hizo un cliente
    public List<Venta> ventasDeCliente(Integer idCliente) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas.listar()) {
            if (v.getCliente().getId().equals(idCliente)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // Devuelve las ventas que hizo un vendedor
    public List<Venta> ventasDeVendedor(Integer idVendedor) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas.listar()) {
            if (v.getVendedor().getId().equals(idVendedor)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // ---------------------------------------------------------------
    // INFORMES
    // ---------------------------------------------------------------

    // Suma el precio final de todas las ventas
    public Double totalRecaudado() {
        Double total = 0.0;
        for (Venta v : ventas.listar()) {
            total += v.getPrecioFinal();
        }
        return total;
    }

    // Suma la comision que gano un vendedor en todas sus ventas
    public Double comisionDeVendedor(Integer idVendedor) throws GestionVentasException {
        vendedores.buscarPorId(idVendedor); // si no existe, lanza EntidadNoEncontradaException
        Double total = 0.0;
        for (Venta v : ventasDeVendedor(idVendedor)) {
            total += v.getComisionVendedor();
        }
        return total;
    }

    // Textos con todos los elementos, para mostrar en el menu.
    // Si el registro esta vacio, lanzan ColeccionVaciaException.
    public String mostrarVehiculos() throws ColeccionVaciaException {
        return vehiculos.listarTodos();
    }

    public String mostrarClientes() throws ColeccionVaciaException {
        return clientes.listarTodos();
    }

    public String mostrarVendedores() throws ColeccionVaciaException {
        return vendedores.listarTodos();
    }

    public String mostrarVentas() throws ColeccionVaciaException {
        return ventas.listarTodos();
    }
}
