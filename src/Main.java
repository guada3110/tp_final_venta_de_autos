import Controller.GestorVentas;
import Models.*;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import Exception.*;
import Enum.*;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static Scanner scanner =new Scanner(System.in);
    private static GestorVentas gestor;

    public static void main(String[] args) {

        try {
            gestor = new GestorVentas();
            gestor.iniciar();
            //crea las tablas si no existen y carga los datos de la base
        }catch (GestionVentasException e){
            System.out.println("No se pudo iniciar el sistema: "+e.getMessage());
            return;
        }

        System.out.println("══════════ SISTEMA DE VENTA DE VEHICULOS ══════════");
        // Se repite hasta que alguien escriba "salir" en el nombre de usuario

        boolean salir =false;
        while (!salir){
            Usuario usuario = iniciarSesion();
            if(usuario == null){
                salir=true;
            }else {
                usarMenu(usuario);
                gestor.cerrarSesion();
                System.out.println("Menu cerrado\n");
            }
        }

        try {
            gestor.cerrar();
        }catch (PersistenciaException e){
            System.out.println("Error al cerrar las conexiones: "+e.getMessage());
        }
        System.out.println("Saliste del programa \n");

    }

    // ---------------------------------------------------------------
    // LOGIN Y MENU
    // ---------------------------------------------------------------

    // Pide usuario y contrasenia hasta que sean correctos.
    // Devuelve null si el usuario escribe "salir".

    private static Usuario iniciarSesion(){
        while (true){
            System.out.println("Iniciar sesion (escribi 'salir' para cerrar el programa)");
            System.out.println("Usuario: ");
            String nombeUsuario = scanner.nextLine();
            if(nombeUsuario.equalsIgnoreCase("salir")){
                return null;
            }
            System.out.println("Contraseña: ");
            String contrasenia = scanner.nextLine();
            try {
                Usuario usuario = gestor.iniciarSesion(nombeUsuario, contrasenia);
                System.out.println("\nBienvenido/a, "+ usuario.getNombreUsuario() + " (" +usuario.getRol() + ")\n");
                return usuario;
            }catch (CredencialesInvalidasException e){
                System.out.println(e.getMessage() + "\n");
            }
        }
    }

    // Muestra el menu del usuario y ejecuta la opcion elegida hasta que elija 0.
    private static  void usarMenu(Usuario usuario){
        int opcion=-1;
        do {
            System.out.println(usuario.getMenu());
            System.out.println("Opcion: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                if(usuario instanceof Administrador){
                    opcionAdministrador(opcion);
                }else {
                    opcionEmpleado(opcion, (Empleado) usuario);
                }
            }catch (GestionVentasException e){
                System.out.println("Error: "+e.getMessage());
            }catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Error: tenes que ingresar un numero");
            }catch (IllegalArgumentException e){
                System.out.println("Error: la opcion escrita no existe");
            }
        }while (opcion!=0);
    }

    private static void opcionAdministrador(int opcion) throws GestionVentasException {
        switch (opcion){
            case 1:
                registrarVehiculo();
                break;
            case 2:
                registrarVendedor();
                break;
            case 3:
                registrarCliente();
                break;
            case 4:
                System.out.println("ID del vendedor: ");
                int idVendedor = scanner.nextInt();
                scanner.nextLine();
                registrarVenta(idVendedor);
                break;
            case 5:
                System.out.println(gestor.mostrarVehiculos());
                break;
            case 6:
                mostrarVehiculosDisponibles();
                break;
            case 7:
                System.out.println(gestor.mostrarClientes());
                break;
            case 8:
                System.out.println(gestor.mostrarVendedores());
                break;
            case 9:
                System.out.println(gestor.mostrarVentas());
                break;
            case 10:
                System.out.println("Total recaudado: "+gestor.totalRecaudado());
                break;
            case 11:
                System.out.println("ID del vendedor: ");
                int id = scanner.nextInt();
                scanner.nextLine();
                verComision(id);
                break;
            case 12:
                crearUsuario();
                break;
            case 13:
                System.out.println(gestor.mostrarUsuarios());
                break;
            case 14:
                eliminarVehiculo();
                break;
            case 15:
                modificarPrecioVehiculo();
                break;
            case 16:
                eliminarCliente();
                break;
            case 0:
                break;
            default:
                System.out.println("Opcion invalida");

        }
    }

    // El empleado siempre usa su propio vendedor para las ventas y las consultas
    private static void opcionEmpleado(int opcion, Empleado empleado) throws GestionVentasException {
        Integer idVendedor = empleado.getVendedor().getId();
        switch (opcion) {
            case 1:
                registrarCliente();
                break;
            case 2:
                registrarVenta(idVendedor);
                break;
            case 3:
                mostrarVehiculosDisponibles();
                break;
            case 4:
                buscarPorMarca();
                break;
            case 5:
                System.out.print("DNI: ");
                String dni = scanner.nextLine();
                System.out.println(gestor.buscarClientePorDni(dni));
                break;
            case 6:
                verMisVentas(idVendedor);
                break;
            case 7:
                verComision(idVendedor);
                break;
            case 0:
                break;
            default:
                System.out.println("Opcion invalida");
        }
    }
    // ---------------------------------------------------------------
    // ALTAS
    // ---------------------------------------------------------------

    // Pide los datos comunes y despues los propios del tipo elegido.
    private static void registrarVehiculo() throws GestionVentasException {
        System.out.println("Tipo de vehiculo: 1. Auto, 2. Moto, 3. Camioneta");
        System.out.println("Opcion: ");
        int tipo = scanner.nextInt();
        scanner.nextLine();
        if(tipo<1 || tipo>3){
            System.out.println("Tipo invalido");
            return;
        }

        System.out.println("Marca: ");
        String marca = scanner.nextLine();
        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();
        System.out.print("Anio: ");
        int anio = scanner.nextInt();
        System.out.print("Precio base: ");
        double precioBase = scanner.nextDouble();
        System.out.print("Kilometraje: ");
        int kilometraje = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Combustible (NAFTA, DIESEL, GNC, HIBRIDO, ELECTRICO): ");
        TipoCombustible combustible = TipoCombustible.valueOf(scanner.nextLine().trim().toUpperCase());

        Vehiculo vehiculo;
        if(tipo==1){
            System.out.print("Cantidad de puertas: ");
            int puertas = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Tipo de carroceria (ej: Sedan, SUV): ");
            String carroceria = scanner.nextLine();
            vehiculo = new Auto(marca, modelo, anio, precioBase, kilometraje, combustible, puertas, carroceria);
        } else if (tipo==2) {
            System.out.print("Cilindrada (cc): ");
            int cilindrada = scanner.nextInt();
            scanner.nextLine();
            vehiculo = new Moto(marca, modelo, anio, precioBase, kilometraje, combustible, cilindrada);
        }else {
            System.out.print("Capacidad de carga (kg): ");
            double carga = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("¿Tiene traccion 4x4? (s/n): ");
            boolean traccion4x4 = scanner.nextLine().equalsIgnoreCase("s");
            vehiculo = new Camioneta(marca, modelo, anio, precioBase, kilometraje, combustible, carga, traccion4x4);
        }

        gestor.registrarVehiculo(vehiculo);
        System.out.println("Vehiculo registrado con ID "+ vehiculo.getId());
    }

    private static void registrarVendedor() throws GestionVentasException {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Legajo: ");
        String legajo = scanner.nextLine();
        System.out.print("Porcentaje de comision: ");
        double comision = scanner.nextDouble();
        scanner.nextLine();

        Vendedor vendedor = new Vendedor(nombre, dni, apellido, telefono, email, legajo, comision);
        gestor.registrarVendedor(vendedor);
        System.out.println("Vendedor registrado con ID " + vendedor.getId());
    }

    // La fecha de alta es la de hoy
    private static void registrarCliente() throws GestionVentasException {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Telefono: ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Direccion: ");
        String direccion = scanner.nextLine();

        Cliente cliente = new Cliente(nombre, dni, apellido, telefono, email, direccion, LocalDate.now());
        gestor.registrarCliente(cliente);
        System.out.println("Cliente registrado con ID " + cliente.getId());
    }

    // Recibe el id del vendedor: el administrador lo elige y el empleado usa el suyo.
    private static void registrarVenta(Integer idVendedor) throws GestionVentasException {
        mostrarVehiculosDisponibles();
        System.out.print("ID del vehiculo: ");
        int idVehiculo = scanner.nextInt();
        scanner.nextLine();
        System.out.print("DNI del cliente: ");
        Cliente cliente = gestor.buscarClientePorDni(scanner.nextLine());
        System.out.print("Metodo de pago (EFECTIVO, TRANSFERENCIA, TARJETA, FINANCIADO): ");
        MetodoPago metodoPago = MetodoPago.valueOf(scanner.nextLine().trim().toUpperCase());

        Venta venta = gestor.registrarVenta(idVehiculo, cliente.getId(), idVendedor, metodoPago);
        System.out.println("Venta registrada:");
        System.out.println(venta);
    }

    // Crea un Administrador o un Empleado. El empleado necesita un vendedor ya cargado
    private static void crearUsuario() throws GestionVentasException {
        System.out.println("Tipo de usuario: 1. Administrador  2. Empleado");
        System.out.print("Opcion: ");
        int tipo = scanner.nextInt();
        scanner.nextLine();
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo invalido");
            return;
        }
        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasenia = scanner.nextLine();

        Usuario usuario;
        if (tipo == 1) {
            usuario = new Administrador(nombreUsuario, contrasenia);
        } else {
            System.out.print("ID del vendedor asociado: ");
            int idVendedor = scanner.nextInt();
            scanner.nextLine();
            Vendedor vendedor = gestor.buscarVendedor(idVendedor);
            usuario = new Empleado(nombreUsuario, contrasenia, vendedor);
        }

        gestor.registrarUsuario(usuario);
        System.out.println("Usuario " + usuario.getNombreUsuario() + " creado con ID " + usuario.getId());
    }

    // ---------------------------------------------------------------
    // BAJAS Y MODIFICACIONES
    // ---------------------------------------------------------------
    private static void eliminarVehiculo() throws GestionVentasException {
        System.out.print("ID del vehiculo a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("¿Seguro que queres eliminarlo? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            gestor.eliminarVehiculo(id);
            System.out.println("Vehiculo eliminado");
        }
    }

    private static void modificarPrecioVehiculo() throws GestionVentasException {
        System.out.print("ID del vehiculo: ");
        int id = scanner.nextInt();
        System.out.print("Nuevo precio base: ");
        double nuevoPrecio = scanner.nextDouble();
        scanner.nextLine();
        gestor.modificarPrecioVehiculo(id, nuevoPrecio);
        System.out.println("Precio actualizado");
    }
    private static void eliminarCliente() throws GestionVentasException {
        System.out.print("ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("¿Seguro que queres eliminarlo? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            gestor.eliminarCliente(id);
            System.out.println("Cliente eliminado");
        }
    }

    // ---------------------------------------------------------------
    // CONSULTAS
    // ---------------------------------------------------------------
    private static void mostrarVehiculosDisponibles() throws GestionVentasException {
        List<Vehiculo> disponibles = gestor.listarDisponibles();
        if (disponibles.isEmpty()) {
            System.out.println("No hay vehiculos disponibles");
        }
        for (Vehiculo v : disponibles) {
            System.out.println(v);
        }
    }
    private static void buscarPorMarca() throws GestionVentasException {
        System.out.print("Marca: ");
        List<Vehiculo> resultado = gestor.buscarPorMarca(scanner.nextLine());
        if (resultado.isEmpty()) {
            System.out.println("No hay vehiculos de esa marca");
        }
        for (Vehiculo v : resultado) {
            System.out.println(v);
        }
    }

    private static void verMisVentas(Integer idVendedor) throws GestionVentasException {
        List<Venta> misVentas = gestor.ventasDeVendedor(idVendedor);
        if (misVentas.isEmpty()) {
            System.out.println("Todavia no tenes ventas");
        }
        for (Venta v : misVentas) {
            System.out.println(v);
        }
    }
    private static void verComision(Integer idVendedor) throws GestionVentasException {
        Double comision = gestor.comisionDeVendedor(idVendedor);
        System.out.println("Comision total: " + String.format("$%,.2f", comision));
    }

}