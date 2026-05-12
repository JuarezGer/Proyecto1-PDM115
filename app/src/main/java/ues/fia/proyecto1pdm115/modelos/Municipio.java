package ues.fia.proyecto1pdm115.modelos;

public class Municipio {
    private String codMunicipio,nombreMunicipio,codDepartamento;
    public Municipio(){}
    public Municipio(String codMunicipio,String nombreMunicipio){
        this.codMunicipio=codMunicipio;
        this.nombreMunicipio=nombreMunicipio;
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    @Override
    public String toString(){
        return codMunicipio+" - "+nombreMunicipio;
    }
}
