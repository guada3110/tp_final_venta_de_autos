package Models;

import java.time.LocalDate;
import java.util.Objects;
import Exception.*;
import Enum.MetodoPago;
import Interface.IIdentificable;

public class Venta implements IIdentificable {
    private static Integer contadorId=0;
    private Integer id;
    private LocalDate fecha;
    private Cliente cliente;
    private Vendedor vendedor;
    private Double precioFinal;
    private MetodoPago metodoPago;
    private Vehiculo vehiculo;
    //constructor
    public Venta( Cliente cliente, Vendedor vendedor, MetodoPago metodoPago, Vehiculo vehiculo) throws DatosInvalidosException {
        this.id = ++contadorId;
        this.fecha = LocalDate.now();
        setCliente(cliente);
        setVendedor(vendedor);
        setMetodoPago(metodoPago);
        setVehiculo(vehiculo);
        setPrecioFinal(vehiculo.calcularPrecioFinal());
    }
    // Constructor para reconstruir una venta desde la base de datos.
// Recibe el id, la fecha y el precio final tal como se guardaron:
// la fecha no es la de hoy y el precio es el que se cobro en ese momento.
    public Venta(Integer id, LocalDate fecha, Cliente cliente, Vendedor vendedor, Double precioFinal, MetodoPago metodoPago, Vehiculo vehiculo) throws DatosInvalidosException {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El id debe ser mayor a 0");
        }
        this.id = id;
        // Si el id que viene de la base es mayor al contador, se actualiza el contador
        if (id > contadorId) {
            contadorId = id;
        }
        setFecha(fecha);
        setCliente(cliente);
        setVendedor(vendedor);
        setPrecioFinal(precioFinal);
        setMetodoPago(metodoPago);
        setVehiculo(vehiculo);
    }
    //get y set
    public static Integer getContadorId() {
        return contadorId;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) throws DatosInvalidosException {
        if (fecha==null) {
            throw new DatosInvalidosException("La venta debe tener fecha");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha de la venta no puede ser futura");
        }
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) throws  DatosInvalidosException {
       if(cliente == null){
           throw new DatosInvalidosException("La venta debe tener un cliente");
       }
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) throws  DatosInvalidosException {
        if(vendedor == null){
            throw new DatosInvalidosException("La venta debe tener un vendedor");
        }
        this.vendedor = vendedor;
    }

    public Double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(Double precioFinal) throws  DatosInvalidosException
    {
        if(precioFinal == null || precioFinal <= 0){
            throw new DatosInvalidosException("El precio final debe ser mayor a 0");
        }
        this.precioFinal = precioFinal;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) throws  DatosInvalidosException {
        if(metodoPago == null){
            throw new DatosInvalidosException("La venta debe tener un metodo de pago");
        }
        this.metodoPago = metodoPago;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) throws  DatosInvalidosException {
        if(vehiculo == null){
            throw new DatosInvalidosException("La venta debe tener un vehiculo");
        }
        this.vehiculo = vehiculo;
    }

    //metodos
    public Double getComisionVendedor()throws DatosInvalidosException{
        return vendedor.calcularComision(precioFinal);
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Venta venta)) return false;
        return Objects.equals(id, venta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {

        return   "\n╔══════════════ VENTA ══════════════╗" +
                "\n  ID:             " + id +
                "\n  Fecha:          " + fecha +
                "\n  Cliente:        " + cliente +
                "\n  Vendedor:       " + vendedor +
                "\n  Precio final:   $" + precioFinal +
                "\n  Método de pago: " + metodoPago +
                "  ╚═══════════════════════════════════╝";
    }

}
