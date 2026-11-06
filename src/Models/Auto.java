package Models;
import Enum.*;

import java.util.Objects;


public class Auto extends Vehiculo{
    private int cantidadPuertas;
    private String tipoCarroceria;


    //CONTRUCTOR
    public Auto(String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje,EstadoVehiculo estado, TipoCombustible combustible, int cantidadPuertas, String tipoCarroceria) {
        super(marca, modelo, anio, precioBase, kilometraje, estado, combustible);
        this.cantidadPuertas = cantidadPuertas;
        this.tipoCarroceria = tipoCarroceria;
    }

    //GETTER Y SETTER

    public int getCantidadPuertas() {
        return cantidadPuertas;
    }

    public void setCantidadPuertas(int cantidadPuertas) {
        this.cantidadPuertas = cantidadPuertas;
    }

    public String getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(String tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }
    //EQUALS Y HASHCODE
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Auto auto = (Auto) o;
        return cantidadPuertas == auto.cantidadPuertas && Objects.equals(tipoCarroceria, auto.tipoCarroceria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cantidadPuertas, tipoCarroceria);
    }

    //METODOS

    @Override
    public String toString() {
        return  super.toString()+
                "%nPuertas:       %d%n" + cantidadPuertas+
                "Carrocería:    %s%n" + tipoCarroceria+
                "══════════════════════════════";
    }

    public Double calcularPrecioFinal() {
        double precio = getPrecioBase();
        if (tipoCarroceria.equalsIgnoreCase("SUV")) {
            precio *= 1.10;          // +10% por ser SUV
        }
        return precio;
    }



}
