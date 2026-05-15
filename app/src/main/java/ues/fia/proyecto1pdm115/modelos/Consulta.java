package ues.fia.proyecto1pdm115.modelos;
public class Consulta {

    private int idConsulta;
    private String duiPaciente;
    private String duiDoctor;
    private String codEmergencia;
    private String fechaConsulta;
    private String diagnostico;
    private float cargoTotalConsulta;
    private int pagaMedicamento; // 1 para Sí, 0 para No

    public Consulta() {
    }

    public Consulta(int idConsulta, String duiPaciente, String duiDoctor, String codEmergencia,
                    String fechaConsulta, String diagnostico, float cargoTotalConsulta,
                    int pagaMedicamento) {
        this.idConsulta = idConsulta;
        this.duiPaciente = duiPaciente;
        this.duiDoctor = duiDoctor;
        this.codEmergencia = codEmergencia;
        this.fechaConsulta = fechaConsulta;
        this.diagnostico = diagnostico;
        this.cargoTotalConsulta = cargoTotalConsulta;
        this.pagaMedicamento = pagaMedicamento;
    }

    // Getters y Setters
    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getDuiPaciente() {
        return duiPaciente;
    }

    public void setDuiPaciente(String duiPaciente) {
        this.duiPaciente = duiPaciente;
    }

    public String getDuiDoctor() {
        return duiDoctor;
    }

    public void setDuiDoctor(String duiDoctor) {
        this.duiDoctor = duiDoctor;
    }

    public String getCodEmergencia() {
        return codEmergencia;
    }

    public void setCodEmergencia(String codEmergencia) {
        this.codEmergencia = codEmergencia;
    }

    public String getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public float getCargoTotalConsulta() {
        return cargoTotalConsulta;
    }

    public void setCargoTotalConsulta(float cargoTotalConsulta) {
        this.cargoTotalConsulta = cargoTotalConsulta;
    }

    public int getPagaMedicamento() {
        return pagaMedicamento;
    }

    public void setPagaMedicamento(int pagaMedicamento) {
        this.pagaMedicamento = pagaMedicamento;
    }
}