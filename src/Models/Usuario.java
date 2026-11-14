package Models;

import Interface.IIdentificable;
import Exception.*;
import Enum.ERol;
import java.util.Objects;

public abstract class Usuario implements IIdentificable {
    private static Integer contadorId =0;
    private Integer id;
    private String nombreUsuario;
    private String contrasenia;

    //constructor
    public Usuario(String nombreUsuario, String contrasenia) throws DatosInvalidosException {
        setNombreUsuario(nombreUsuario);
        setContrasenia(contrasenia);
        this.id = ++contadorId;
    }

    // Constructor para reconstruir un usuario que ya existe en la base de datos.
    // Recibe el id guardado en vez de generarlo.
    public Usuario(Integer id, String nombreUsuario, String contrasenia) throws DatosInvalidosException {
        if (id == null || id <= 0) {
            throw new DatosInvalidosException("El id debe ser mayor a 0");
        }
        setNombreUsuario(nombreUsuario);
        setContrasenia(contrasenia);
        this.id = id;
        // Si el id que viene de la base es mayor al contador, se actualiza el contador
        if (id > contadorId) {
            contadorId = id;
        }
    }

    //get y set
    public static Integer getContadorId() {
        return contadorId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) throws DatosInvalidosException{
        if(nombreUsuario==null || nombreUsuario.trim().isEmpty()){
            throw new DatosInvalidosException("El nombre de usuario es obligatorio");
        }
        if(!nombreUsuario.trim().matches("[a-zA-Z0-9._]{4,20}")){
            throw new DatosInvalidosException("El nombre de usuario debe tener entre 4 y 20 caracteres(letras, numeros, punto o guion bajo)");
        }
        this.nombreUsuario = nombreUsuario.trim().toLowerCase();
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) throws DatosInvalidosException {
        if(contrasenia==null|| contrasenia.length()<4)
        {
            throw new DatosInvalidosException("La contrasenia debe tener al menos 4 caracteres");
        }
        if(contrasenia.contains(" ")){
            throw new DatosInvalidosException("La contrasenia no puede tener espacios");
        }
        this.contrasenia = contrasenia;
    }

    //metodos
    public Boolean contraseniaCorrecta(String contraseniaIngresada){
        return contrasenia.equals(contraseniaIngresada);
    }

    public abstract ERol getRol();
    public abstract  String getMenu();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "══════════ USUARIO ══════════" + "\n" +
                "  ID:            " + id + "\n" +
                "  Usuario:       " + nombreUsuario + "\n" +
                "  Rol:           " + getRol();
    }
}
