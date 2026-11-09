package Models;
import Enum.*;

import java.util.Objects;
import Exception.*;
public class Moto extends Vehiculo{
    //ATRIBUTOS
    private Integer cilindrada;
    //CONSTRUCTOR
    public Moto(String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje,  TipoCombustible combustible, Integer cilindrada) throws DatosInvalidosException{
        super(marca, modelo, anio, precioBase, kilometraje, combustible);
        setCilindrada( cilindrada );
    }

    public Moto(Integer id, String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, EstadoVehiculo estado, TipoCombustible combustible, Integer cilindrada) throws DatosInvalidosException {
        super(id, marca, modelo, anio, precioBase, kilometraje, estado, combustible);
        setCilindrada(cilindrada);
    }
    //GETTER Y SETTER

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer cilindrada) throws DatosInvalidosException {
        if(cilindrada<50 || cilindrada>2500){
            throw new DatosInvalidosException("La cilindrada debe estar entre 50 y 2500 cc");
        }
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
    @Override
    public Double calcularPrecioFinal() {
        Double precio = getPrecioBase();
        if (cilindrada > 250) {
            precio *=  1.15;          // +15% por alta cilindrada
        }
        return precio;
    }

    public String toString() {
        return super.toString()+
                "%nCilindrada:    %d cc%n" +cilindrada+
                   "══════════════════════════════";

    }



}
