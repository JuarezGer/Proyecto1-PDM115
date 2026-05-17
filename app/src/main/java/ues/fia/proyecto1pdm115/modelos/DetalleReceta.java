package ues.fia.proyecto1pdm115.modelos;

public class DetalleReceta {
    private Integer idDetalleReceta;
    private Integer idReceta;
    private Integer cantidad;
    private String instrucciones;
    private Double precioUnitarioHistorico;
    private Double subTotalItem;

    public DetalleReceta() {
    }

    public DetalleReceta(Integer idDetalleReceta, Integer idReceta,
                         Integer cantidad, String instrucciones,
                         Double precioUnitarioHistorico,
                         Double subTotalItem) {

        this.idDetalleReceta = idDetalleReceta;
        this.idReceta = idReceta;
        this.cantidad = cantidad;
        this.instrucciones = instrucciones;
        this.precioUnitarioHistorico = precioUnitarioHistorico;
        this.subTotalItem = subTotalItem;
    }

    public Integer getIdDetalleReceta() {
        return idDetalleReceta;
    }

    public void setIdDetalleReceta(Integer idDetalleReceta) {
        this.idDetalleReceta = idDetalleReceta;
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Double getPrecioUnitarioHistorico() {
        return precioUnitarioHistorico;
    }

    public void setPrecioUnitarioHistorico(Double precioUnitarioHistorico) {
        this.precioUnitarioHistorico = precioUnitarioHistorico;
    }

    public Double getSubTotalItem() {
        return subTotalItem;
    }

    public void setSubTotalItem(Double subTotalItem) {
        this.subTotalItem = subTotalItem;
    }
}
