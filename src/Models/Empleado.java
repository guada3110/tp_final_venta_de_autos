package Models;

import Exception.*;
import Enum.ERol;
public class Empleado extends Usuario{
    private Vendedor vendedor;

    //constructor
    public Empleado(String nombreUsuario, String contrasenia, Vendedor vendedor) throws DatosInvalidosException {
        super(nombreUsuario, contrasenia);
        setVendedor(vendedor);
    }
    public Empleado(Integer id, String nombreUsuario, String contrasenia, Vendedor vendedor) throws DatosInvalidosException {
        super(id, nombreUsuario, contrasenia);
        setVendedor(vendedor);
    }

    //get y set
    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) throws DatosInvalidosException {
        if(vendedor == null)
        {
            throw new DatosInvalidosException("El empleado debe estar asociado a un vendedor");
        }
        this.vendedor = vendedor;
    }

    @Override
    public ERol getRol() {
        return ERol.EMPLEADO;
    }

    @Override
    public String getMenu() {
        return "══════════ MENU EMPLEADO ══════════" + "\n" +
                "  1. Registrar cliente" + "\n" +
                "  2. Registrar venta" + "\n" +
                "  3. Ver vehiculos disponibles" + "\n" +
                "  4. Buscar vehiculos por marca" + "\n" +
                "  5. Buscar cliente por DNI" + "\n" +
                "  6. Ver mis ventas" + "\n" +
                "  7. Ver mi comision" + "\n" +
                "  0. Cerrar sesion" + "\n" +
                "═══════════════════════════════════";
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "  Vendedor:      " + vendedor.getNombreCompleto() + " (Legajo " + vendedor.getLegajo() + ")" + "\n" +
                "══════════════════════════════";
    }
}
