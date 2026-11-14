package Models;

import java.time.LocalDate;
import Exception.DatosInvalidosException;

public class Cliente extends Persona{
    private String direccion;
    private LocalDate fechaAlta;

    //constructor
    public Cliente(String nombre, String dni, String apellido, String telefono, String email, String direccion, LocalDate fechaAlta) throws DatosInvalidosException {
        super(nombre, dni, apellido, telefono, email);
        setDireccion(direccion);
        setFechaAlta(fechaAlta);
    }
    public Cliente(Integer id, String nombre, String dni, String apellido, String telefono, String email, String direccion, LocalDate fechaAlta) throws DatosInvalidosException {
        super(id, nombre, dni, apellido, telefono, email);
        setDireccion(direccion);
        setFechaAlta(fechaAlta);
    }
    //get y set
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) throws DatosInvalidosException {
        if(direccion==null|| direccion.trim().isEmpty()){
            throw new DatosInvalidosException("La direccion no puede estar vacia");
        }
        this.direccion = direccion.trim();
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta)throws DatosInvalidosException {
        if(fechaAlta==null){
            throw new DatosInvalidosException("La fecha de alta no puede estar vacia");
        }
        if(fechaAlta.isAfter(LocalDate.now())){
            throw new DatosInvalidosException("La fecha de alta no puede ser posterior a hoy");
        }
        this.fechaAlta = fechaAlta;
    }

    //metodos
    @Override
    public String toString() {
        return super.toString() + "\n" +
                "  Dirección:     " + direccion + "\n" +
                "  Fecha de alta: " + fechaAlta + "\n" +
                "══════════════════════════════";
    }
}
