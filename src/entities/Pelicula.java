package entities;

public class Pelicula {
    private String id;
    private String title;
    private String originalTitle;
    private String originalLanguage;
    private String genres;
    private String releaseDate;
    private long budget;
    private long revenue;
    private boolean adult;
    private String belongsToCollection;
    private String homepage;
    private String imdbId;
    private String overview;
    private String productionCompanies;
    private String productionCountries;
    private double runtime;
    private String spokenLanguages;
    private String status;
    private String tagline;
    
    // Estadísticas calculadas
    private int totalEvaluaciones;
    private double calificacionPromedio;
    
    public Pelicula(String id, String title, String originalTitle, String originalLanguage, 
                   String genres, String releaseDate, long budget, long revenue, 
                   boolean adult, String belongsToCollection, String homepage, 
                   String imdbId, String overview, String productionCompanies, 
                   String productionCountries, double runtime, String spokenLanguages, 
                   String status, String tagline) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.genres = genres;
        this.releaseDate = releaseDate;
        this.budget = budget;
        this.revenue = revenue;
        this.adult = adult;
        this.belongsToCollection = belongsToCollection;
        this.homepage = homepage;
        this.imdbId = imdbId;
        this.overview = overview;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.runtime = runtime;
        this.spokenLanguages = spokenLanguages;
        this.status = status;
        this.tagline = tagline;
        this.totalEvaluaciones = 0;
        this.calificacionPromedio = 0.0;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }
    
    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }
    
    public String getGenres() { return genres; }
    public void setGenres(String genres) { this.genres = genres; }
    
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    
    public long getBudget() { return budget; }
    public void setBudget(long budget) { this.budget = budget; }
    
    public long getRevenue() { return revenue; }
    public void setRevenue(long revenue) { this.revenue = revenue; }
    
    public boolean isAdult() { return adult; }
    public void setAdult(boolean adult) { this.adult = adult; }
    
    public String getBelongsToCollection() { return belongsToCollection; }
    public void setBelongsToCollection(String belongsToCollection) { this.belongsToCollection = belongsToCollection; }
    
    public String getHomepage() { return homepage; }
    public void setHomepage(String homepage) { this.homepage = homepage; }
    
    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }
    
    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }
    
    public String getProductionCompanies() { return productionCompanies; }
    public void setProductionCompanies(String productionCompanies) { this.productionCompanies = productionCompanies; }
    
    public String getProductionCountries() { return productionCountries; }
    public void setProductionCountries(String productionCountries) { this.productionCountries = productionCountries; }
    
    public double getRuntime() { return runtime; }
    public void setRuntime(double runtime) { this.runtime = runtime; }
    
    public String getSpokenLanguages() { return spokenLanguages; }
    public void setSpokenLanguages(String spokenLanguages) { this.spokenLanguages = spokenLanguages; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }
    
    public int getTotalEvaluaciones() { return totalEvaluaciones; }
    public void setTotalEvaluaciones(int totalEvaluaciones) { this.totalEvaluaciones = totalEvaluaciones; }
    
    public double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
}
