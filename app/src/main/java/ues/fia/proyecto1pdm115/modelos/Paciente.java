package ues.fia.proyecto1pdm115.modelos;

public class Paciente {

    private String duiPaciente;
    private Integer idPoliza;
    private String codDistrito;
    private String primerNombrePaciente;
    private String segundoNombrePaciente;
    private String primerApellidoPaciente;
    private String segundoApellidoPaciente;
    private String fechaNacimientoPaciente;
    private String generoPaciente;
    private String telefonoPaciente;

    public Paciente() {
    }

    public Paciente(String duiPaciente, Integer idPoliza, String codDistrito,
                    String primerNombrePaciente, String segundoNombrePaciente,
                    String primerApellidoPaciente, String segundoApellidoPaciente,
                    String fechaNacimientoPaciente, String generoPaciente,
                    String telefonoPaciente) {
        this.duiPaciente = duiPaciente;
        this.idPoliza = idPoliza;
        this.codDistrito = codDistrito;
        this.primerNombrePaciente = primerNombrePaciente;
        this.segundoNombrePaciente = segundoNombrePaciente;
        this.primerApellidoPaciente = primerApellidoPaciente;
        this.segundoApellidoPaciente = segundoApellidoPaciente;
        this.fechaNacimientoPaciente = fechaNacimientoPaciente;
        this.generoPaciente = generoPaciente;
        this.telefonoPaciente = telefonoPaciente;
    }

    public String getDuiPaciente() {
        return duiPaciente;
    }

    public void setDuiPaciente(String duiPaciente) {
        this.duiPaciente = duiPaciente;
    }

    public Integer getIdPoliza() {
        return idPoliza;
    }

    public void setIdPoliza(Integer idPoliza) {
        this.idPoliza = idPoliza;
    }

    public String getCodDistrito() {
        return codDistrito;
    }

    public void setCodDistrito(String codDistrito) {
        this.codDistrito = codDistrito;
    }

    public String getPrimerNombrePaciente() {
        return primerNombrePaciente;
    }

    public void setPrimerNombrePaciente(String primerNombrePaciente) {
        this.primerNombrePaciente = primerNombrePaciente;
    }

    public String getSegundoNombrePaciente() {
        return segundoNombrePaciente;
    }

    public void setSegundoNombrePaciente(String segundoNombrePaciente) {
        this.segundoNombrePaciente = segundoNombrePaciente;
    }

    public String getPrimerApellidoPaciente() {
        return primerApellidoPaciente;
    }

    public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
        this.primerApellidoPaciente = primerApellidoPaciente;
    }

    public String getSegundoApellidoPaciente() {
        return segundoApellidoPaciente;
    }

    public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
        this.segundoApellidoPaciente = segundoApellidoPaciente;
    }

    public String getFechaNacimientoPaciente() {
        return fechaNacimientoPaciente;
    }

    public void setFechaNacimientoPaciente(String fechaNacimientoPaciente) {
        this.fechaNacimientoPaciente = fechaNacimientoPaciente;
    }

    public String getGeneroPaciente() {
        return generoPaciente;
    }

    public void setGeneroPaciente(String generoPaciente) {
        this.generoPaciente = generoPaciente;
    }

    public String getTelefonoPaciente() {
        return telefonoPaciente;
    }

    public void setTelefonoPaciente(String telefonoPaciente) {
        this.telefonoPaciente = telefonoPaciente;
    }
}
