package ues.fia.proyecto1pdm115.modelos;

public class Aseguradora {

    private int idAseguradora;
    private String nombreAseguradora;
    private String telefonoAseguradora;

    public Aseguradora() {
    }

    public Aseguradora(int idAseguradora, String nombreAseguradora, String telefonoAseguradora) {
        this.idAseguradora = idAseguradora;
        this.nombreAseguradora = nombreAseguradora;
        this.telefonoAseguradora = telefonoAseguradora;
    }

    public int getIdAseguradora() {
        return idAseguradora;
    }

    public void setIdAseguradora(int idAseguradora) {
        this.idAseguradora = idAseguradora;
    }

    public String getNombreAseguradora() {
        return nombreAseguradora;
    }

    public void setNombreAseguradora(String nombreAseguradora) {
        this.nombreAseguradora = nombreAseguradora;
    }

    public String getTelefonoAseguradora() {
        return telefonoAseguradora;
    }

    public void setTelefonoAseguradora(String telefonoAseguradora) {
        this.telefonoAseguradora = telefonoAseguradora;
    }
}
