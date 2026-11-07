package Models;

public class Vendedor extends Persona{
    private String legajo;
    private Double porcentajeComision;

    //constructor
    public Vendedor(String nombre, String dni, String apellido, String telefono, String email, String legajo, Double porcentajeComision) {
        super(nombre, dni, apellido, telefono, email);
        this.legajo = legajo;
        this.porcentajeComision = porcentajeComision;
    }

    //get y set
    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public Double getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(Double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    //metodos
    public Double calcularComision (Double monto){
        return monto *porcentajeComision;
    }

    @Override
    public String toString() {

        return super.toString().replace("PERSONA", "VENDEDOR") +"\n" +
                "  Legajo:        " + legajo + "\n" +
                "  Comisión:      " + porcentajeComision + "%" + "\n"+
                "╚═════════════════════════════════╝";
    }
}
