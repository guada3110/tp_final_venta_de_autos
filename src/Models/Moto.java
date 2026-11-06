package Models;
import Enum.*;

import java.util.Objects;

public class Moto extends Vehiculo{
    //ATRIBUTOS
    private int cilindrada;
    //CONSTRUCTOR
    public Moto(String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, EstadoVehiculo estado, TipoCombustible combustible, int cilindrada) {
        super(marca, modelo, anio, precioBase, kilometraje, estado, combustible);
        this.cilindrada = cilindrada;
    }

    //GETTER Y SETTER

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    //EQUALS Y HASHCODE

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Moto moto = (Moto) o;
        return cilindrada == moto.cilindrada;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cilindrada);
    }
    //METODOS


    public String toString() {
        return super.toString()+
                "%nCilindrada:    %d cc%n" +cilindrada+
                   "══════════════════════════════";

    }

        @Override
        public Double calcularPrecioFinal() {
            double precio = getPrecioBase();
            if (cilindrada > 250) {
                precio *= 1.15;          // +15% por alta cilindrada
            }
            return precio;
        }

}
