package entities;
import tad.list.MyArrayListImpl;

public class Director {
    private String nombre;
    private MyArrayListImpl<String> peliculas;
    private int totalEvaluaciones;
    private double medianaCalificacion;
    
    public Director(String nombre) {
        this.nombre = nombre;
        this.peliculas = new MyArrayListImpl<>(100000000);
        this.totalEvaluaciones = 0;
        this.medianaCalificacion = 0.0;
    }
    
    public void agregarPelicula(String idPelicula) {
        this.peliculas.add(idPelicula);
    }
    
    public int getCantidadPeliculas() {
        return peliculas.getSize();
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public MyArrayListImpl<String> getPeliculas() { return peliculas; }
    public void setPeliculas(MyArrayListImpl<String> peliculas) { this.peliculas = peliculas; }
    
    public int getTotalEvaluaciones() { return totalEvaluaciones; }
    public void setTotalEvaluaciones(int totalEvaluaciones) { this.totalEvaluaciones = totalEvaluaciones; }
    
    public double getMedianaCalificacion() { return medianaCalificacion; }
    public void setMedianaCalificacion(double medianaCalificacion) { this.medianaCalificacion = medianaCalificacion; }
}