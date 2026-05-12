package ues.fia.proyecto1pdm115.modelos;

public class Pago {

    private int idPago;
    private int idConsulta;
    private String tipoPago;
    private double montoTotal;
    private String fechaPago;

    public Pago() {
    }

    public Pago(int idPago, int idConsulta, String tipoPago, double montoTotal, String fechaPago) {
        this.idPago = idPago;
        this.idConsulta = idConsulta;
        this.tipoPago = tipoPago;
        this.montoTotal = montoTotal;
        this.fechaPago = fechaPago;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
