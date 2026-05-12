package ues.fia.proyecto1pdm115.modelos;

public class Opcion_crud {
    private String idopcion,descripcion;
    private int num_crud;

    public Opcion_crud(){}

    public Opcion_crud(String idopcion, String descripcion) {
        this.idopcion = idopcion;
        this.descripcion = descripcion;
    }

    public String getIdopcion() {
        return idopcion;
    }

    public void setIdopcion(String idopcion) {
        this.idopcion = idopcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNum_crud() {
        return num_crud;
    }

    public void setNum_crud(int num_crud) {
        this.num_crud = num_crud;
    }

    @Override
    public String toString(){
        return idopcion + " - " + descripcion;
    }
}
