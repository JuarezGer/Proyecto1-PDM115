package ues.fia.proyecto1pdm115.modelos;

public class Medicamento {
    private String codMedicamento;
    private String nombreMedicamento;
    private String fechaVencimiento;
    private Integer cantidadInventario;
    private Double precioVenta;
    private String lote;

    public Medicamento() {
    }

    public Medicamento(String codMedicamento, String nombreMedicamento, String fechaVencimiento,
                       Integer cantidadInventario, Double precioVenta, String lote) {
        this.codMedicamento = codMedicamento;
        this.nombreMedicamento = nombreMedicamento;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidadInventario = cantidadInventario;
        this.precioVenta = precioVenta;
        this.lote = lote;
    }

    public String getCodMedicamento() {
        return codMedicamento;
    }

    public void setCodMedicamento(String codMedicamento) {
        this.codMedicamento = codMedicamento;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getCantidadInventario() {
        return cantidadInventario;
    }

    public void setCantidadInventario(Integer cantidadInventario) {
        this.cantidadInventario = cantidadInventario;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}
