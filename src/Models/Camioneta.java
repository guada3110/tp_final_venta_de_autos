package Models;
import Enum.*;

import java.util.Objects;

public class Camioneta extends Vehiculo {

    //ATRIBUTOS
    private double capacidadDeCarga;
    private boolean traccion4x4;

    //CONSTRUCTOR


    public Camioneta(String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje,EstadoVehiculo estado, TipoCombustible combustible, double capacidadDeCarga, boolean traccion4x4) {
        super(marca, modelo, anio, precioBase, kilometraje, estado, combustible);
       this.capacidadDeCarga = capacidadDeCarga;
        this.traccion4x4 = traccion4x4;
    }

    //GETTER Y SETTER
    public double getCapacidadDeCarga() {
        return capacidadDeCarga;
    }

    public void setCapacidadDeCarga(double capacidadDeCarga) {
        capacidadDeCarga = capacidadDeCarga;
    }

    public boolean isTraccion4x4() {
        return traccion4x4;
    }

    public void setTraccion4x4(boolean traccion4x4) {
        traccion4x4 = traccion4x4;
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
        double precio = getPrecioBase();
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
