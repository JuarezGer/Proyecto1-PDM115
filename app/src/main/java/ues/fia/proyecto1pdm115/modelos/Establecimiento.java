package ues.fia.proyecto1pdm115.modelos;

public class Establecimiento {

    private int idEstablecimiento;
    private String nombreEstablecimiento;
    private String telefonoEstablecimiento;
    private String direccionEstablecimiento;

    public Establecimiento() {
    }

    public Establecimiento(int idEstablecimiento, String nombreEstablecimiento,
                           String telefonoEstablecimiento, String direccionEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
        this.nombreEstablecimiento = nombreEstablecimiento;
        this.telefonoEstablecimiento = telefonoEstablecimiento;
        this.direccionEstablecimiento = direccionEstablecimiento;
    }

    public int getIdEstablecimiento() {
        return idEstablecimiento;
    }

    public void setIdEstablecimiento(int idEstablecimiento) {
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }

    public String getTelefonoEstablecimiento() {
        return telefonoEstablecimiento;
    }

    public void setTelefonoEstablecimiento(String telefonoEstablecimiento) {
        this.telefonoEstablecimiento = telefonoEstablecimiento;
    }

    public String getDireccionEstablecimiento() {
        return direccionEstablecimiento;
    }

    public void setDireccionEstablecimiento(String direccionEstablecimiento) {
        this.direccionEstablecimiento = direccionEstablecimiento;
    }
}
