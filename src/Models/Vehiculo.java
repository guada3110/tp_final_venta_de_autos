package Models;
import Enum.*;
import Interface.IIdentificable;


public abstract class Vehiculo implements IIdentificable {
   private static Integer contadorId=0;
    private Integer id;
    private String marca;
    private String modelo;
    private Integer anio;
    private Double precioBase;
    private Integer kilometraje;
    private EstadoVehiculo estado;
    private TipoCombustible combustible;

    //constructor
    public Vehiculo( String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, EstadoVehiculo estado, TipoCombustible combustible) {
        this.id = ++contadorId;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precioBase = precioBase;
        this.kilometraje = kilometraje;
        this.estado = estado;
        this.combustible = combustible;
    }

    //get y set
    public static Integer getContadorId() {
        return contadorId;
    }

    public Integer getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Integer kilometraje) {
        this.kilometraje = kilometraje;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public TipoCombustible getCombustible() {
        return combustible;
    }

    public void setCombustible(TipoCombustible combustible) {
        this.combustible = combustible;
    }

}
