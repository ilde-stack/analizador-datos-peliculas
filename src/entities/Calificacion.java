package entities;

public class Calificacion {
    private String idPelicula;
    private String idUsuario;
    private double rating;
    private long timestamp;
    
    public Calificacion(String idPelicula, String idUsuario, double rating, long timestamp) {
        this.idPelicula = idPelicula;
        this.idUsuario = idUsuario;
        this.rating = rating;
        this.timestamp = timestamp;
    }
    
    public String getIdPelicula() { return idPelicula; }
    public void setIdPelicula(String idPelicula) { this.idPelicula = idPelicula; }
    
    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}