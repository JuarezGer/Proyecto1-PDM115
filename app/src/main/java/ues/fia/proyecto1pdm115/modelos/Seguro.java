package ues.fia.proyecto1pdm115.modelos;

public class Seguro {

    private int idPoliza;
    private String duiPaciente;
    private int idAseguradora;
    private double porcentajeCobertura;
    private String tipoAsegurado;
    private double deductibleMedicina;
    private double deductibleOperacion;

    public Seguro() {
    }

    public Seguro(int idPoliza, String duiPaciente, int idAseguradora,
                  double porcentajeCobertura, String tipoAsegurado,
                  double deductibleMedicina, double deductibleOperacion) {
        this.idPoliza = idPoliza;
        this.duiPaciente = duiPaciente;
        this.idAseguradora = idAseguradora;
        this.porcentajeCobertura = porcentajeCobertura;
        this.tipoAsegurado = tipoAsegurado;
        this.deductibleMedicina = deductibleMedicina;
        this.deductibleOperacion = deductibleOperacion;
    }

    public int getIdPoliza() {
        return idPoliza;
    }

    public void setIdPoliza(int idPoliza) {
        this.idPoliza = idPoliza;
    }

    public String getDuiPaciente() {
        return duiPaciente;
    }

    public void setDuiPaciente(String duiPaciente) {
        this.duiPaciente = duiPaciente;
    }

    public int getIdAseguradora() {
        return idAseguradora;
    }

    public void setIdAseguradora(int idAseguradora) {
        this.idAseguradora = idAseguradora;
    }

    public double getPorcentajeCobertura() {
        return porcentajeCobertura;
    }

    public void setPorcentajeCobertura(double porcentajeCobertura) {
        this.porcentajeCobertura = porcentajeCobertura;
    }

    public String getTipoAsegurado() {
        return tipoAsegurado;
    }

    public void setTipoAsegurado(String tipoAsegurado) {
        this.tipoAsegurado = tipoAsegurado;
    }

    public double getDeductibleMedicina() {
        return deductibleMedicina;
    }

    public void setDeductibleMedicina(double deductibleMedicina) {
        this.deductibleMedicina = deductibleMedicina;
    }

    public double getDeductibleOperacion() {
        return deductibleOperacion;
    }

    public void setDeductibleOperacion(double deductibleOperacion) {
        this.deductibleOperacion = deductibleOperacion;
    }
}
