package ues.fia.proyecto1pdm115.modelos;

public class Receta {
    private Integer idReceta;
    private Integer idConsulta;
    private String fechaEmision;
    private String estadoReceta;

    public Receta() {
    }

    public Receta(Integer idReceta, Integer idConsulta, String fechaEmision, String estadoReceta) {
        this.idReceta = idReceta;
        this.idConsulta = idConsulta;
        this.fechaEmision = fechaEmision;
        this.estadoReceta = estadoReceta;
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstadoReceta() {
        return estadoReceta;
    }

    public void setEstadoReceta(String estadoReceta) {
        this.estadoReceta = estadoReceta;
    }
}
