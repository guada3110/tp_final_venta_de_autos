package Models;
import Enum.*;

import java.util.Objects;
import Exception.*;

public class Camioneta extends Vehiculo {

    //ATRIBUTOS
    private Double capacidadDeCarga;
    private Boolean traccion4x4;

    //CONSTRUCTOR
    public Camioneta(String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, TipoCombustible combustible, Double capacidadDeCarga, Boolean traccion4x4) throws DatosInvalidosException{
        super(marca, modelo, anio, precioBase, kilometraje, combustible);
       setCapacidadDeCarga( capacidadDeCarga);
       setTraccion4x4(traccion4x4);
    }
    public Camioneta(Integer id, String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, EstadoVehiculo estado, TipoCombustible combustible, Double capacidadDeCarga, Boolean traccion4x4) throws DatosInvalidosException {
        super(id, marca, modelo, anio, precioBase, kilometraje, estado, combustible);
        setCapacidadDeCarga(capacidadDeCarga);
        setTraccion4x4(traccion4x4);
    }
    //GETTER Y SETTER
    public Double getCapacidadDeCarga() {
        return capacidadDeCarga;
    }

    public void setCapacidadDeCarga(Double capacidadDeCarga) throws DatosInvalidosException {
        if(capacidadDeCarga <=0||capacidadDeCarga >5000) {
            throw new DatosInvalidosException("La capacidad de carga debe ser mayor a 0 y no superar los 5000 kg");
        }
        this.capacidadDeCarga = capacidadDeCarga;
    }

    public Boolean isTraccion4x4() {
        return traccion4x4;
    }

    public void setTraccion4x4(Boolean traccion4x4) throws DatosInvalidosException {
        this.traccion4x4 = traccion4x4;
    }

    //EQUALS Y HASHCODE
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Camioneta camioneta = (Camioneta) o;
        return Double.compare(capacidadDeCarga, camioneta.capacidadDeCarga) == 0 && traccion4x4 == camioneta.traccion4x4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacidadDeCarga, traccion4x4);
    }
    //METODOS

    @Override
    public Double calcularPrecioFinal() {
        Double precio = getPrecioBase();
        if (traccion4x4) {
            precio *= 1.20;          // +20% por 4x4
        }
        return precio;
    }

    @Override
    public String toString() {
        return super.toString() +
                "%nCap. de carga:  %,.2f kg%n" + capacidadDeCarga+
                "Tracción 4x4:  %s%n" + traccion4x4+
                "══════════════════════════════";

    }


}
