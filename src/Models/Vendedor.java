package Models;

import Exception.DatosInvalidosException;

public class Vendedor extends Persona{
    private String legajo;
    private Double porcentajeComision;

    //constructor
    public Vendedor(String nombre, String dni, String apellido, String telefono, String email, String legajo, Double porcentajeComision) throws DatosInvalidosException {
        super(nombre, dni, apellido, telefono, email);
        setLegajo(legajo);
        setPorcentajeComision(porcentajeComision);
    }
    public Vendedor(Integer id, String nombre, String dni, String apellido, String telefono, String email, String legajo, Double porcentajeComision) throws DatosInvalidosException {
        super(id, nombre, dni, apellido, telefono, email);
        setLegajo(legajo);
        setPorcentajeComision(porcentajeComision);
    }
    //get y set
    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) throws DatosInvalidosException {
        if(legajo == null || legajo.trim().isEmpty()){
            throw new DatosInvalidosException("El legajo no puede estar vacio");
        }
        if(!legajo.trim().matches("[\\p{L}0-9-]+")){
            throw new DatosInvalidosException("El legajo solo puede contener letras, numeros y guiones");
        }
        this.legajo = legajo.trim().toUpperCase();
    }

    public Double getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(Double porcentajeComision) throws DatosInvalidosException {
        if(porcentajeComision < 0 || porcentajeComision > 100){
            throw new DatosInvalidosException("El porcentaje de comision debe estar entre 0 y 100");
        }
        this.porcentajeComision = porcentajeComision;
    }

    //metodos
    public Double calcularComision (Double monto) throws DatosInvalidosException{
        if(monto <= 0){
            throw new DatosInvalidosException("El monto de la venta no puede ser negativo");
        }
        return monto *porcentajeComision /100;
    }

    @Override
    public String toString() {

        return super.toString().replace("PERSONA", "VENDEDOR") +"\n" +
                "  Legajo:        " + legajo + "\n" +
                "  Comisión:      " + porcentajeComision + "%" + "\n"+
                "╚═════════════════════════════════╝";
    }
}
