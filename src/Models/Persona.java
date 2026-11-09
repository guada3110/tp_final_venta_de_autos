package Models;

import Interface.IIdentificable;
import Exception.*;
import java.util.Objects;

public abstract class Persona implements IIdentificable {
    private String nombre;
    private String dni;
    private String apellido;
    private String telefono;
    private String email;
    private static Integer contadorId=0;
    private Integer id;
    //constructor
    public Persona(String nombre, String dni, String apellido, String telefono, String email) throws DatosInvalidosException{
        setNombre(nombre);
        setDni(dni);
        setApellido(apellido);
        setTelefono(telefono);
        setEmail(email);
        this.id = ++contadorId;
    }
    // Constructor para reconstruir una persona que ya existe en la base de datos.
// Recibe el id guardado en vez de generarlo.
    public Persona(Integer id, String nombre, String dni, String apellido, String telefono, String email) throws DatosInvalidosException {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El id debe ser mayor a 0");
        }
        setNombre(nombre);
        setDni(dni);
        setApellido(apellido);
        setTelefono(telefono);
        setEmail(email);
        this.id = id;
        // Si el id que viene de la base es mayor al contador, se actualiza el contador
        if (id > contadorId) {
            contadorId = id;
        }
    }
    //get y set
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws DatosInvalidosException {
        if(nombre == null||nombre.trim().isEmpty()){
            throw new DatosInvalidosException("El nombre es obligatorio");
        }
        if(!nombre.trim().matches("[\\p{L} '-]+")){
            throw new DatosInvalidosException("El nombre solo puede tener letras");
        }
        this.nombre = nombre.trim();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) throws DatosInvalidosException {
        if(dni==null||dni.trim().isEmpty()){
            throw new DatosInvalidosException("El dni es obligatorio");
        }
        if(!dni.trim().matches("\\d{7,8}")){
            throw new DatosInvalidosException("El dni debe tener 7 u 8 digitos");
        }
        this.dni = dni.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws DatosInvalidosException {
        if(apellido==null||apellido.trim().isEmpty()){
            throw new DatosInvalidosException("El apellido es obligatorio");
        }
        if(!apellido.trim().matches("[\\p{L} '-]+")){
            throw new DatosInvalidosException("El apellido solo puede tener letras");
        }
        this.apellido = apellido.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono)  throws DatosInvalidosException {
        if(telefono==null||telefono.trim().isEmpty()){
            throw new DatosInvalidosException("El telefono es obligatorio");
        }
        if (!telefono.trim().matches("\\+?[0-9-]{6,20}")){
            throw new DatosInvalidosException("El telefono no tiene un formato valido");
        }
        this.telefono = telefono.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws DatosInvalidosException {
        if(email==null||email.trim().isEmpty()){
            throw new DatosInvalidosException("El email es obligatorio");
        }
        if(!email.trim().matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+")){
            throw new DatosInvalidosException("El email no tiene un formato valido");
        }
        this.email = email.trim();
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

        return  "╔════════════ PERSONA ════════════╗" + "\n" +
                "  Nombre:   " + getNombreCompleto() + "\n" +
                "  DNI:      " + dni + "\n" +
                "  Teléfono: " + telefono + "\n" +
                "  Email:    " + email + "\n" +
                "  ID:       " + id;
    }
}
