package Models;

import Exception.*;
import Enum.ERol;
public class Administrador extends Usuario{

    //constructor
    public Administrador(String nombreUsuario, String contrasenia) throws DatosInvalidosException {
        super(nombreUsuario, contrasenia);
    }
    public Administrador(Integer id, String nombreUsuario, String contrasenia) throws DatosInvalidosException {
        super(id, nombreUsuario, contrasenia);
    }

    @Override
    public ERol getRol() {
        return ERol.ADMINISTRADOR;
    }

    @Override
    public String getMenu() {
        return  "══════════ MENU ADMINISTRADOR ══════════" + "\n" +
                "  1. Registrar vehiculo" + "\n" +
                "  2. Registrar vendedor" + "\n" +
                "  3. Registrar cliente" + "\n" +
                "  4. Registrar venta" + "\n" +
                "  5. Ver vehiculos" + "\n" +
                "  6. Ver vehiculos disponibles" + "\n" +
                "  7. Ver clientes" + "\n" +
                "  8. Ver vendedores" + "\n" +
                "  9. Ver todas las ventas" + "\n" +
                "  10. Ver total recaudado" + "\n" +
                "  11. Ver comision de un vendedor" + "\n" +
                "  12. Crear usuario" + "\n" +
                "  13. Ver usuarios" + "\n" +
                "  0. Cerrar sesion" + "\n" +
                "════════════════════════════════════════";
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "══════════════════════════════";
    }
}

