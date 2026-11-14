package Models;
import Enum.*;
import Interface.IIdentificable;
import Exception.*;

import java.time.LocalDate;

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
    public Vehiculo( String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, TipoCombustible combustible) throws DatosInvalidosException {
        this.id = ++contadorId;
        setMarca(marca);
        setModelo(modelo);
        setAnio(anio);
        setPrecioBase(precioBase);
        setKilometraje(kilometraje);
        this.estado=EstadoVehiculo.DISPONIBLE;
        setCombustible(combustible);
    }
    // Constructor para reconstruir un vehiculo que ya existe en la base de datos.
// Recibe el id y el estado tal como estaban guardados, en vez de generarlos.
    public Vehiculo(Integer id, String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, EstadoVehiculo estado, TipoCombustible combustible) throws DatosInvalidosException {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El id debe ser mayor a 0");
        }
        if (estado == null) {
            throw new DatosInvalidosException("El estado no puede estar vacio");
        }
        this.id = id;
        // Si el id que viene de la base es mayor al contador, se actualiza el contador.
        // Asi los vehiculos nuevos siguen numerandose a partir del ultimo id guardado.
        if (id > contadorId) {
            contadorId = id;
        }
        setMarca(marca);
        setModelo(modelo);
        setAnio(anio);
        setPrecioBase(precioBase);
        setKilometraje(kilometraje);
        this.estado = estado;
        setCombustible(combustible);
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

    public void setMarca(String marca) throws DatosInvalidosException {
        if(marca == null || marca.trim().isEmpty()){
            throw new DatosInvalidosException("La marca no puede estar vacia");
        }
        if(!marca.trim().matches("[\\p{L}0-9 .'-]+")){
            throw new DatosInvalidosException("La marca contiene caracteres no validos");
        }
        this.marca = marca.trim();
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo)  throws DatosInvalidosException {
        if(modelo == null || modelo.trim().isEmpty()){
            throw new DatosInvalidosException("El modelo no puede estar vacio");
        }
        if(!modelo.trim().matches("[\\p{L}0-9 .'-]+")){
            throw new DatosInvalidosException("El modelo contiene caracteres no validos");
        }
        this.modelo = modelo.trim();
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) throws DatosInvalidosException {
        int anioMaximo = LocalDate.now().getYear() + 1;
        if (anio < 1900 || anio > anioMaximo) {
            throw new DatosInvalidosException("El anio debe estar entre 1900 y " + anioMaximo + ".");
        }
        this.anio = anio;
    }
    public Double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(Double precioBase) throws DatosInvalidosException {
        if(precioBase<=0){
            throw new DatosInvalidosException("El precio base debe ser mayor a 0");
        }
        this.precioBase = precioBase;
    }

    public Integer getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Integer kilometraje) throws DatosInvalidosException {
        if(kilometraje<0){
            throw new DatosInvalidosException("El kilometraje no puede ser negativo");
        }
        this.kilometraje = kilometraje;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }
    public void setEstado(EstadoVehiculo estado) throws DatosInvalidosException {
        if (estado == null) {
            throw new DatosInvalidosException("El estado del vehiculo no puede estar vacio.");
        }
        this.estado = estado;
    }

    public TipoCombustible getCombustible() {
        return combustible;
    }

    public void setCombustible(TipoCombustible combustible) throws DatosInvalidosException {
        if (combustible == null) {
            throw new DatosInvalidosException("El tipo de combustible no puede estar vacio.");
        }
        this.combustible = combustible;
    }

    //METODOS

    public abstract Double calcularPrecioFinal();

    public boolean estaDisponible() {
        return estado == EstadoVehiculo.DISPONIBLE;
    }

    public void marcarComoVendido() throws VehiculoNoDisponibleException {
        if(estado == EstadoVehiculo.VENDIDO){
            throw new VehiculoNoDisponibleException("El vehiculo "+marca+" " + modelo+ " ya fue vendido");
        }
        this.estado = EstadoVehiculo.VENDIDO;
    }

    @Override
    public String toString() {
        return "══════════ " + getClass().getSimpleName().toUpperCase() + " ══════════" + "\n" +
                "  ID:            " + id + "\n" +
                "  Marca:         " + marca + "\n" +
                "  Modelo:        " + modelo + "\n" +
                "  Año:           " + anio + "\n" +
                "  Precio base:   " + String.format("$%,.2f", precioBase) + "\n" +
                "  Precio final:  " + String.format("$%,.2f", calcularPrecioFinal()) + "\n" +
                "  Kilometraje:   " + kilometraje + " km" + "\n" +
                "  Estado:        " + estado + "\n" +
                "  Combustible:   " + combustible;
    }


}
