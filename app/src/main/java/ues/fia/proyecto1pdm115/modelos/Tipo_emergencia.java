package ues.fia.proyecto1pdm115.modelos;

public class Tipo_emergencia {

    private String cod_emergencia;
    private String prioridad;
    private float costo_emergencia;


    public Tipo_emergencia() {
    }


    public Tipo_emergencia(String cod_emergencia, String prioridad, float costo_emergencia) {
        this.cod_emergencia = cod_emergencia;
        this.prioridad = prioridad;
        this.costo_emergencia = costo_emergencia;
    }

    public String getCod_emergencia() {
        return cod_emergencia;
    }

    public void setCod_emergencia(String cod_emergencia) {
        this.cod_emergencia = cod_emergencia;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public float getCosto_emergencia() {
        return costo_emergencia;
    }

    public void setCosto_emergencia(float costo_emergencia) {
        this.costo_emergencia = costo_emergencia;
    }
}