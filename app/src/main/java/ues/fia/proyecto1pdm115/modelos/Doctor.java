package ues.fia.proyecto1pdm115.modelos;

public class Doctor {
    private String duiDoctor,nombreDoctor,apellidoDoctor,idUsuario;
    private int idHospital;

    public Doctor(){}
    public Doctor(String duiDoctor,String nombreDoctor,String apellidoDoctor){
        this.duiDoctor=duiDoctor;
        this.nombreDoctor=nombreDoctor;
        this.apellidoDoctor=apellidoDoctor;
    }

    public String getDuiDoctor() {
        return duiDoctor;
    }

    public void setDuiDoctor(String duiDoctor) {
        this.duiDoctor = duiDoctor;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getApellidoDoctor() {
        return apellidoDoctor;
    }

    public void setApellidoDoctor(String apellidoDoctor) {
        this.apellidoDoctor = apellidoDoctor;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdHospital() {
        return idHospital;
    }

    public void setIdHospital(int idHospital) {
        this.idHospital = idHospital;
    }

    @Override
    public String toString(){
        return duiDoctor+" - "+nombreDoctor + " "+apellidoDoctor;
    }
}
