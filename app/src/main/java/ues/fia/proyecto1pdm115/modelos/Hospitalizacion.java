package ues.fia.proyecto1pdm115.modelos;

public class Hospitalizacion {
    private Integer idHospitalizacion;
    private Integer idConsulta;
    private String fechaInicioHosp;
    private String fechaFinHosp;
    private String motivoIngreso;
    private Double costoHospitalizacion;

    public Hospitalizacion() {
    }

    public Hospitalizacion(Integer idHospitalizacion, Integer idConsulta,
                           String fechaInicioHosp, String fechaFinHosp, String motivoIngreso,
                           Double costoHospitalizacion) {
        this.idHospitalizacion = idHospitalizacion;
        this.idConsulta = idConsulta;
        this.fechaInicioHosp = fechaInicioHosp;
        this.fechaFinHosp = fechaFinHosp;
        this.motivoIngreso = motivoIngreso;
        this.costoHospitalizacion = costoHospitalizacion;
    }

    public Integer getIdHospitalizacion() {
        return idHospitalizacion;
    }

    public void setIdHospitalizacion(Integer idHospitalizacion) {
        this.idHospitalizacion = idHospitalizacion;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getFechaInicioHosp() {
        return fechaInicioHosp;
    }

    public void setFechaInicioHosp(String fechaInicioHosp) {
        this.fechaInicioHosp = fechaInicioHosp;
    }

    public String getFechaFinHosp() {
        return fechaFinHosp;
    }

    public void setFechaFinHosp(String fechaFinHosp) {
        this.fechaFinHosp = fechaFinHosp;
    }

    public String getMotivoIngreso() {
        return motivoIngreso;
    }

    public void setMotivoIngreso(String motivoIngreso) {
        this.motivoIngreso = motivoIngreso;
    }

    public Double getCostoHospitalizacion() {
        return costoHospitalizacion;
    }

    public void setCostoHospitalizacion(Double costoHospitalizacion) {
        this.costoHospitalizacion = costoHospitalizacion;
    }
}
