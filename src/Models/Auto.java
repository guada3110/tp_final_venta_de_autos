package Models;
import Enum.*;


import Exception.*;

public class Auto extends Vehiculo{
    private Integer cantidadPuertas;
    private String tipoCarroceria;


    //CONTRUCTOR
    public Auto(String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, TipoCombustible combustible, Integer cantidadPuertas, String tipoCarroceria) throws DatosInvalidosException {
        super(marca, modelo, anio, precioBase, kilometraje,  combustible);
      setCantidadPuertas(cantidadPuertas);
      setTipoCarroceria(tipoCarroceria);
    }

    public Auto(Integer id, String marca, String modelo, Integer anio, Double precioBase, Integer kilometraje, EstadoVehiculo estado, TipoCombustible combustible, Integer cantidadPuertas, String tipoCarroceria) throws DatosInvalidosException {
        super(id, marca, modelo, anio, precioBase, kilometraje, estado, combustible);
        setCantidadPuertas(cantidadPuertas);
        setTipoCarroceria(tipoCarroceria);
    }

    //GETTER Y SETTER

    public int getCantidadPuertas() {
        return cantidadPuertas;
    }

    //Asigna la cantidad de puertas del vehiculo.
    // si se carga un dato invalido salta la exception al no estar entre 2 y 5
    public void setCantidadPuertas(Integer cantidadPuertas) throws DatosInvalidosException {
        if(cantidadPuertas<2 || cantidadPuertas>5){
            throw new DatosInvalidosException("La cantidad de puertas debe estar entre 2 y 5");
        }
        this.cantidadPuertas = cantidadPuertas;
    }

    public String getTipoCarroceria() {
        return tipoCarroceria;
    }

    // Asigna el tipo de carroceria y verifica su formato
    public void setTipoCarroceria(String tipoCarroceria) throws DatosInvalidosException {
        if(tipoCarroceria == null|| tipoCarroceria.trim().isEmpty()){
            throw new DatosInvalidosException("El tipo de carroceria no puede estar vacio");
        }
        if(!tipoCarroceria.trim().matches("[\\p{L} -]+")){
            throw new DatosInvalidosException("El tipo de carroceria solo puede contener letras, espacios y guiones");
        }
        this.tipoCarroceria = tipoCarroceria.trim();
    }

    //METODOS

    // Metodo heredado de la clase Vehiculo en la cual calculamos el precio final del auto
    // en base a su tipo de carroceria
    public Double calcularPrecioFinal() {
        Double precio = getPrecioBase();
        if (tipoCarroceria.equalsIgnoreCase("SUV")) {
            precio *= 1.10;          // +10% por ser SUV
        }
        return precio;
    }
    @Override
    public String toString() {
        return super.toString() + "\n" +
                "  Puertas:       " + cantidadPuertas + "\n" +
                "  Carrocería:    " + tipoCarroceria + "\n" +
                "══════════════════════════════";
    }




}
