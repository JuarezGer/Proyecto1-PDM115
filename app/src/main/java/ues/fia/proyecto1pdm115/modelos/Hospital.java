package ues.fia.proyecto1pdm115.modelos;

public class Hospital {
    private Integer idHospital;
    private String codDistrito,nombreHospital,telefonoHospital;

    public Hospital(){}

    public Hospital(Integer idHospital, String nombreHospital){
        this.idHospital=idHospital;
        this.nombreHospital=nombreHospital;
    }

    public Integer getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(Integer idHospital) {
        this.idHospital = idHospital;
    }

    public String getCodDistrito() {
        return codDistrito;
    }

    public void setCodDistrito(String codDistrito) {
        this.codDistrito = codDistrito;
    }

    public String getNombreHospital() {
        return nombreHospital;
    }

    public void setNombreHospital(String nombreHospital) {
        this.nombreHospital = nombreHospital;
    }

    public String getTelefonoHospital() {
        return telefonoHospital;
    }

    public void setTelefonoHospital(String telefonoHospital) {
        this.telefonoHospital = telefonoHospital;
    }

    @Override
    public String toString(){
        return idHospital+" - "+nombreHospital;
    }
}
