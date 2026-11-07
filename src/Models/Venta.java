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
    public Venta( Cliente cliente, Vendedor vendedor, Double precioFinal, MetodoPago metodoPago, Vehiculo vehiculo) throws DatosInvalidosException {
        if (vehiculo == null || cliente == null || vendedor == null|| metodoPago == null){
            throw new DatosInvalidosException("La venta debe tener vehiculo, cliente, vendedor y metodo de pago");
        }
        this.id = ++contadorId;
        this.fecha = LocalDate.now();
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.precioFinal = vehiculo.calcularPrecioFinal();
        this.metodoPago = metodoPago;
        this.vehiculo = vehiculo;
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
        if (fecha.isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha de la venta no puede ser futura");
        } else if (fecha==null) {
            throw new DatosInvalidosException("La venta debe tener fecha");
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
    public Double getComisionVendedor(){
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
