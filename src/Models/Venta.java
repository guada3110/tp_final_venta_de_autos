package Models;

import java.time.LocalDate;
import java.util.Objects;

import Enum.MetodoPago;

public class Venta {
    private static Integer contadorId=0;
    private Integer id;
    private LocalDate fecha;
    private Cliente cliente;
    private Vendedor vendedor;
    private Double precioFinal;
    private MetodoPago metodoPago;
    private Vehiculo vehiculo;
    //constructor
    public Venta( LocalDate fecha, Cliente cliente, Vendedor vendedor, Double precioFinal, MetodoPago metodoPago, Vehiculo vehiculo) {
        this.id = ++contadorId;
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.precioFinal = precioFinal;
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

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(Double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
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
        return "\n*****Venta*****" +
                "\nID: " + id +
                ", Fecha: " + fecha +
                ", Cliente: " + cliente +
                ", Vendedor: " + vendedor +
                ", Precio Final: " + precioFinal +
                ", Metodo de Pago: " + metodoPago;
    }
}
