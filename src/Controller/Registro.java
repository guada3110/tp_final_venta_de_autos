package Controller;

import Interface.IIdentificable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import Exception.*;

public class Registro <T extends IIdentificable> {
    private Map<Integer, T> elementos;

    //Constructor
    public Registro() {
        this.elementos = new LinkedHashMap<>();
    }

    //Metodos

    public void agregar(T elemento) throws DatosInvalidosException {
        if(elemento == null){
            throw new DatosInvalidosException("No se puede agregar un elemento vacio");
        }
        if(elementos.containsKey(elemento.getId())){
            throw new DatosInvalidosException("Ya existe un elemento con el id "+ elemento.getId());
        }
        this.elementos.put(elemento.getId(), elemento);
    }

    public T buscarPorId(Integer id) throws EntidadNoEncontradaException {
        T elemento = elementos.get(id);
        if(elemento == null){
            throw new EntidadNoEncontradaException("No se encontro ningun elemento con el id "+id);
        }
        return elemento;
    }

    public void eliminar(Integer id) throws EntidadNoEncontradaException {
        if(!elementos.containsKey(id)){
            throw new EntidadNoEncontradaException("No se puede eliminar, no existe un elemento con el id "+id);
        }
        elementos.remove(id);
    }

    public String listarTodos() throws ColeccionVaciaException {
        if(elementos.isEmpty()){
            throw new ColeccionVaciaException("No hay elementos para mostrar");
        }
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer, T> entry : elementos.entrySet()){
            sb.append(entry.getKey()+": "+entry.getValue()+"\n");
        }
        return sb.toString();
    }
    // Devuelve una lista con todos los elementos.
    // Es una copia, asi desde afuera no se puede modificar el Map.
    public List<T> listar() {
        return new ArrayList<>(elementos.values());
    }

    public Integer cantidad(){
        return elementos.size();
    }

}
