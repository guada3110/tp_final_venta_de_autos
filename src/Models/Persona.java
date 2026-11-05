package Models;

import java.util.Objects;

public abstract class Persona {
    private String nombre;
    private String dni;
    private String apellido;
    private String telefono;
    private String email;
    private static Integer contadorId=0;
    private Integer id;
    //constructor
    public Persona(String nombre, String dni, String apellido, String telefono, String email) {
        this.nombre = nombre;
        this.dni = dni;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.id = ++contadorId;
    }

    //get y set
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Integer getContadorId() {
        return contadorId;
    }

    public Integer getId() {
        return id;
    }
    public String getNombreCompleto(){
        return nombre+" "+apellido;
    }

    //metodos
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Persona persona)) return false;
        return Objects.equals(dni, persona.dni) && Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, id);
    }

    @Override
    public String toString() {
        return "\n Nombre: " + getNombreCompleto()  +
                ", DNI: " + dni +
                ", Telefono: " + telefono +
                ", Email: " + email +
                ", ID: " + id ;
    }
}
