package ues.fia.proyecto1pdm115.modelos;

public class Departamento {
    private String codDepartamento,nombreDepartamento;
    public Departamento(){}
    public Departamento(String codDepartamento,String nombreDepartamento){
        this.codDepartamento=codDepartamento;
        this.nombreDepartamento=nombreDepartamento;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    @Override
    public String toString(){
        return codDepartamento+" - "+nombreDepartamento;
    }
}
