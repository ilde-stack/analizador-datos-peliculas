package entities;
import tad.list.MyArrayListImpl;

public class Coleccion {
    private String id;
    private String nombre;
    private MyArrayListImpl<String> idPeliculas;
    private long ingresosGenerados;
    
    public Coleccion(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.idPeliculas = new MyArrayListImpl<>(100000000);
        this.ingresosGenerados = 0;
    }
    
    public void agregarPelicula(String idPelicula) {
        this.idPeliculas.add(idPelicula);
    }
    
    public int getCantidadPeliculas() {
        return idPeliculas.getSize();
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public MyArrayListImpl<String> getIdPeliculas() { return idPeliculas; }
    public void setIdPeliculas(MyArrayListImpl<String> idPeliculas) { this.idPeliculas = idPeliculas; }
    
    public long getIngresosGenerados() { return ingresosGenerados; }
    public void setIngresosGenerados(long ingresosGenerados) { this.ingresosGenerados = ingresosGenerados; }
}