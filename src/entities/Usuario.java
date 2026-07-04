package entities;
import tad.list.MyArrayListImpl;

public class Usuario {
    private String id;
    private MyArrayListImpl<Calificacion> calificaciones;
    
    public Usuario(String id) {
        this.id = id;
        this.calificaciones = new MyArrayListImpl<>(100000000);
    }
    
    public void agregarCalificacion(Calificacion calificacion) {
        this.calificaciones.add(calificacion);
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public MyArrayListImpl<Calificacion> getCalificaciones() { return calificaciones; }
    public void setCalificaciones(MyArrayListImpl<Calificacion> calificaciones) { this.calificaciones = calificaciones; }
}
