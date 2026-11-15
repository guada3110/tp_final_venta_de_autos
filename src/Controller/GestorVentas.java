package Controller;
import Connection.*;
import Enum.EstadoVehiculo;
import Enum.MetodoPago;
import Exception.*;
import Models.*;

import java.util.ArrayList;
import java.util.List;

public class GestorVentas {

    // Dao: guardan y leen los datos de la base
    private ClienteDao clienteDao;
    private VendedorDao vendedorDao;
    private VehiculoDao vehiculoDao;
    private VentaDAO ventaDao;
    private UsuarioDao usuarioDao;

    // Registros: tienen los datos en memoria mientras el programa esta abierto
    private Registro<Cliente> clientes;
    private Registro<Vendedor> vendedores;
    private Registro<Vehiculo> vehiculos;
    private Registro<Venta> ventas;
    private Registro<Usuario> usuarios;

    //Usuario que inicio sesion. Es null cuando no hay nadie logueado
    private Usuario usuarioActual;

    // Constructor: abre las conexiones de los Dao y crea los registros vacios
    public GestorVentas() throws PersistenciaException {
        this.clienteDao = new ClienteDao();
        this.vendedorDao = new VendedorDao();
        this.vehiculoDao = new VehiculoDao();
        this.ventaDao = new VentaDAO(clienteDao, vehiculoDao, vendedorDao);
        this.usuarioDao = new UsuarioDao(vendedorDao);

        this.clientes = new Registro<>();
        this.vendedores = new Registro<>();
        this.vehiculos = new Registro<>();
        this.ventas = new Registro<>();
        this.usuarios = new Registro<>();
    }


    // Se llama una sola vez al arrancar el programa:
    // crea las tablas si no existen y carga en memoria lo que ya esta guardado.
    public void iniciar() throws GestionVentasException {
        clienteDao.crearTabla();
        vendedorDao.crearTabla();
        vehiculoDao.crearTabla();
        ventaDao.crearTabla();
        usuarioDao.crearTabla();

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
        for(Usuario u : usuarioDao.listarTodos()){
            usuarios.agregar(u);
        }
    }

    // Cierra las conexiones de todos los Dao. Se llama al salir del programa.
    public void cerrar() throws PersistenciaException {
        usuarioDao.cerrarConexion();
        ventaDao.cerrarConexion();
        vehiculoDao.cerrarConexion();
        vendedorDao.cerrarConexion();
        clienteDao.cerrarConexion();
    }


    // Registra un vehiculo nuevo: primero en la base y despues en memoria
    //Solo puede hacerlo el administrador
    public void registrarVehiculo(Vehiculo vehiculo) throws GestionVentasException {
        verificarAdmin();
        if (vehiculo == null) {
            throw new DatosInvalidosException("El vehiculo no puede estar vacio");
        }
        vehiculoDao.guardar(vehiculo);
        vehiculos.agregar(vehiculo);
    }

    // Registra un cliente nuevo. No se permiten dos clientes con el mismo DNI.
    public void registrarCliente(Cliente cliente) throws GestionVentasException {
       verificarSesion();
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
   //Solo puede hacerlo el admin
    public void registrarVendedor(Vendedor vendedor) throws GestionVentasException {
       verificarAdmin();
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
    // Si la registra un empleado, el vendedor tiene que ser el suyo (no puede cargar ventas a nombre de otro).
    public Venta registrarVenta(Integer idVehiculo, Integer idCliente, Integer idVendedor, MetodoPago metodoPago) throws GestionVentasException {
        verificarSesion();
        if(usuarioActual instanceof Empleado){
            Empleado empleado = (Empleado) usuarioActual;
            if(!empleado.getVendedor().getId().equals(idVendedor)){
                throw new DatosInvalidosException("Un empleado solo puede registrar ventas a su nombre");
            }
        }
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
    public Cliente buscarClientePorDni(String dni) throws EntidadNoEncontradaException,AccesoDenegadoException {
        verificarSesion();
        for (Cliente c : clientes.listar()) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        throw new EntidadNoEncontradaException("No existe un cliente con el DNI " + dni);
    }

    // Devuelve los vehiculos que estan disponibles para la venta
    public List<Vehiculo> listarDisponibles() throws AccesoDenegadoException {
      verificarSesion();
        List<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo v : vehiculos.listar()) {
            if (v.estaDisponible()) {
                disponibles.add(v);
            }
        }
        return disponibles;
    }

    // Devuelve los vehiculos de una marca
    public List<Vehiculo> buscarPorMarca(String marca) throws AccesoDenegadoException{
        verificarSesion();
        List<Vehiculo> resultado= new ArrayList<>();
        for (Vehiculo v : vehiculos.listar()) {
            if (v.getMarca().equalsIgnoreCase(marca)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // Devuelve las compras que hizo un cliente
    public List<Venta> ventasDeCliente(Integer idCliente) throws AccesoDenegadoException {
        verificarSesion();
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas.listar()) {
            if (v.getCliente().getId().equals(idCliente)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    // Devuelve las ventas que hizo un vendedor
    public List<Venta> ventasDeVendedor(Integer idVendedor) throws AccesoDenegadoException {
       veriificarVendedorPropio(idVendedor);
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
    //Solo puede verlo el admin
    public Double totalRecaudado() throws AccesoDenegadoException{
       verificarAdmin();
        Double total = 0.0;
        for (Venta v : ventas.listar()) {
            total += v.getPrecioFinal();
        }
        return total;
    }

    // Suma la comision que gano un vendedor en todas sus ventas
    public Double comisionDeVendedor(Integer idVendedor) throws GestionVentasException {
        veriificarVendedorPropio(idVendedor);
        vendedores.buscarPorId(idVendedor); // si no existe, lanza EntidadNoEncontradaException
        Double total = 0.0;
        for (Venta v : ventasDeVendedor(idVendedor)) {
            total += v.getComisionVendedor();
        }
        return total;
    }

    // Textos con todos los elementos, para mostrar en el menu.
    // Si el registro esta vacio, lanzan ColeccionVaciaException.
    public String mostrarVehiculos() throws GestionVentasException {
        verificarSesion();
        return vehiculos.listarTodos();
    }

    public String mostrarClientes() throws GestionVentasException {
       verificarSesion();
        return clientes.listarTodos();
    }

    public String mostrarVendedores() throws GestionVentasException {
       verificarAdmin();
        return vendedores.listarTodos();
    }

    //Todas las ventas: solo puede verlas el admin
    public String mostrarVentas() throws GestionVentasException {
        verificarAdmin();
        return ventas.listarTodos();
    }

    // ---------------------------------------------------------------
    // BAJAS Y MODIFICACIONES (solo administrador)
    // ---------------------------------------------------------------

    // Elimina un vehiculo. No se puede eliminar si tiene una venta registrada,
    // porque la tabla ventas lo referencia con una FOREIGN KEY.
    public void eliminarVehiculo(Integer idVehiculo) throws GestionVentasException {
        verificarAdmin();
        vehiculos.buscarPorId(idVehiculo);
        for (Venta v : ventas.listar()) {
            if(v.getVehiculo().getId().equals(idVehiculo)){
                throw new DatosInvalidosException("No se puede eliminar un vehiculo que tiene una venta registrada");
            }
        }
        vehiculoDao.eliminar(idVehiculo);
        vehiculos.eliminar(idVehiculo);
    }

    // Cambia el precio base de un vehiculo. Solo se puede si todavia no se vendio.
    public void modificarPrecioVehiculo(Integer idVehiculo, Double nuevoPrecio) throws GestionVentasException {
        verificarAdmin();
        Vehiculo vehiculo = vehiculos.buscarPorId(idVehiculo);

        if(vehiculo.getEstado() == EstadoVehiculo.VENDIDO){
            throw new VehiculoNoDisponibleException("No se puede cambiar el precio de un vehiculo vendido");
        }

        Double precioAnterior = vehiculo.getPrecioBase();
        vehiculo.setPrecioBase(nuevoPrecio);
        try {
            vehiculoDao.actualizar(vehiculo);
        }catch (PersistenciaException e){
            vehiculo.setPrecioBase(precioAnterior);
            throw e;
        }
    }

    //Eliminar un cliente, solo si no tiene compras registradas
    public void eliminarCliente(Integer idCliente) throws GestionVentasException {
        verificarAdmin();
        clientes.buscarPorId(idCliente);

        if(!ventasDeCliente(idCliente).isEmpty()){
            throw new DatosInvalidosException("No se puede eliminar un cliente que tiene compras registradas");
        }

        clienteDao.eliminar(idCliente);
        clientes.eliminar(idCliente);
    }



    // ---------------------------------------------------------------
    // USUARIOS Y SESION
    // ---------------------------------------------------------------
    //Busca el usuario por su nombre y verifica la contraseña.
    //si todo esta bien, queda como usuario actual y se devuelve.
    public Usuario iniciarSesion(String nombreUsuario, String contrasenia) throws CredencialesInvalidasException{
        for(Usuario u: usuarios.listar()){
            if(u.getNombreUsuario().equalsIgnoreCase(nombreUsuario.trim())){
                if(u.contraseniaCorrecta(contrasenia)){
                    usuarioActual =u;
                }
            }
            break;
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    //Registra un usuario nuevo. Solo puede hacerlo el admin
    public void registrarUsuario(Usuario usuario) throws GestionVentasException {
        verificarAdmin();
        if(usuario==null){
            throw new DatosInvalidosException("El usuario no puede estar vacio");
        }
        for(Usuario u: usuarios.listar()){
            if(u.getNombreUsuario().equals(usuario.getNombreUsuario())){
                throw new DatosInvalidosException("Ya existe el usuario " +usuario.getNombreUsuario());
            }
        }
        usuarioDao.guardar(usuario);
        usuarios.agregar(usuario);
    }

    //Busca un vendedor por id, se usa al crear un Empleado, que necesita su Vendedor.
    public Vendedor buscarVendedor(Integer idVendedor) throws GestionVentasException {
        verificarAdmin();
        return vendedores.buscarPorId(idVendedor);
    }

    public String mostrarUsuarios() throws GestionVentasException {
        verificarAdmin();
        return usuarios.listarTodos();
    }

    //Lanza uuna excepcion si nadie inicio sesion
    private void verificarSesion() throws AccesoDenegadoException{
        if(usuarioActual == null){
            throw new AccesoDenegadoException("Tenes que iniciar sesion");
        }
    }
    //Lanza una excepcion si el usuario actual no es administrador y se llama al principio de cada operacion
    // que es solo del administrador
    private void verificarAdmin() throws AccesoDenegadoException{
        verificarSesion();
        if(!(usuarioActual instanceof Administrador)){
            throw new AccesoDenegadoException("Esta opcion es solo para el administrador");
        }

    }

    //Si el usuario actual es un empleado, verifica que el vendedor pedido sea el suyo
    private void veriificarVendedorPropio(Integer idVendedor) throws AccesoDenegadoException {
        verificarSesion();
        if(usuarioActual instanceof Empleado){
            Empleado empleado = (Empleado) usuarioActual;
            if(!empleado.getVendedor().getId().equals(idVendedor)){
                throw new AccesoDenegadoException("Un empleado solo puede consultar sus propias ventas");
            }
        }
    }
}
