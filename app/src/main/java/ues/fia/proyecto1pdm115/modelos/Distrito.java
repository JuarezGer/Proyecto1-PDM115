package ues.fia.proyecto1pdm115.modelos;

public class Distrito {
    private String codDistrito, codMunicipio,nombreDistrito;

    public Distrito(){
    }

    public Distrito(String codDistrito, String nombreDistrito){
        this.codDistrito=codDistrito;
        this.nombreDistrito=nombreDistrito;
    }
    public String getCodDistrito() {
        return codDistrito;
    }

    public void setCodDistrito(String codDistrito) {
        this.codDistrito = codDistrito;
    }

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getNombreDistrito() {
        return nombreDistrito;
    }

    public void setNombreDistrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }

    @Override
    public String toString(){
        return codDistrito+" - "+nombreDistrito;
    }
}
