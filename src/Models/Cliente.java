package Models;

import java.time.LocalDate;

public class Cliente extends Persona{
    private String direccion;
    private LocalDate fechaAlta;

    //constructor
    public Cliente(String nombre, String dni, String apellido, String telefono, String email, String direccion, LocalDate fechaAlta) {
        super(nombre, dni, apellido, telefono, email);
        this.direccion = direccion;
        this.fechaAlta = fechaAlta;
    }

    //get y set
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    //metodos
    @Override
    public String toString() {
        return "\n*****Cliente*****" +
                super.toString()+
                ", Direccion: " + direccion +
                ", Fecha de Alta: " + fechaAlta ;
    }
}
