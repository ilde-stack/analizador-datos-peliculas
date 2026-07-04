package entities;
import tad.list.MyArrayListImpl;

public class Actor {
    private String nombre;
    private MyArrayListImpl<String> peliculas;
    
    public Actor(String nombre) {
        this.nombre = nombre;
        this.peliculas = new MyArrayListImpl<>(100000000);
    }
    
    public void agregarPelicula(String idPelicula) {
        this.peliculas.add(idPelicula);
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public MyArrayListImpl<String> getPeliculas() { return peliculas; }
    public void setPeliculas(MyArrayListImpl<String> peliculas) { this.peliculas = peliculas; }
}
