// UMovie.java - Clase principal
import entities.*;
import tad.list.MyArrayListImpl;
import java.io.*;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UMovie {
    private MyArrayListImpl<Pelicula> peliculas;
    private MyArrayListImpl<Usuario> usuarios;
    private MyArrayListImpl<Coleccion> colecciones;
    private MyArrayListImpl<Director> directores;
    private MyArrayListImpl<Calificacion> calificaciones;
    private MyArrayListImpl<Actor> actores;

    private Scanner sc;
    private long inicio;
    private long fin;

    public UMovie() {
        this.sc = new Scanner(System.in);
        this.peliculas = new MyArrayListImpl<>(100000000);
        this.usuarios = new MyArrayListImpl<>(100000000);
        this.colecciones = new MyArrayListImpl<>(100000000);
        this.directores = new MyArrayListImpl<>(100000000);
        this.calificaciones = new MyArrayListImpl<>(100000000);
        this.actores = new MyArrayListImpl<>(100000000);
    }

    public void menuPrincipal() {
        int opcion = 0;

        while (opcion != 3) {
            System.out.println("Seleccione la opción que desee:");
            System.out.println("1. Carga de datos");
            System.out.println("2. Ejecutar consultas");
            System.out.println("3. Salir");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Debe ser un entero entre 1 y 3");
                continue;
            }

            switch (opcion) {
                case 1:
                    inicio = System.currentTimeMillis();
                    cargarDatos();
                    fin = System.currentTimeMillis();
                    System.out.println("Carga de datos exitosa, tiempo de ejecución de la carga: " +
                            (fin - inicio) + "ms");
                    break;
                case 2:
                    menuSecundario();
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
        sc.close();
    }

    private void cargarDatos() {
        try {
            // Cargar películas desde movies_metadata.csv
            cargarPeliculas();
            
            // Cargar créditos desde credits.csv
            cargarCreditos();
            
            // Cargar calificaciones desde ratings.csv
            cargarCalificaciones();

            // Calcular estadísticas después de cargar todo
            calcularEstadisticas();
            
        } catch (IOException e) {
            System.out.println("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void cargarPeliculas() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("movies_metadata.csv"));
        String linea;
        boolean primeraLinea = true;

        while ((linea = br.readLine()) != null) {
            if (primeraLinea) {
                primeraLinea = false;
                continue; // Saltar header
            }

            String[] campos = parsearCSV(linea);
            if (campos.length >= 19) {
                try {
                    // Campos: adult,belongs_to_collection,budget,genres,homepage,id,imdb_id,original_language,original_title,overview,production_companies,production_countries,release_date,revenue,runtime,spoken_languages,status,tagline,title
                    boolean adult = campos[0].equals("TRUE");
                    String belongsToCollection = campos[1];
                    long budget = parsearLong(campos[2]);
                    String genres = campos[3];
                    String homepage = campos[4];
                    String id = campos[5];
                    String imdbId = campos[6];
                    String originalLanguage = campos[7];
                    String originalTitle = campos[8];
                    String overview = campos[9];
                    String productionCompanies = campos[10];
                    String productionCountries = campos[11];
                    String releaseDate = campos[12];
                    long revenue = parsearLong(campos[13]);
                    double runtime = parsearDouble(campos[14]);
                    String spokenLanguages = campos[15];
                    String status = campos[16];
                    String tagline = campos[17];
                    String title = campos[18];  

                    Pelicula pelicula = new Pelicula(id, title, originalTitle, originalLanguage, genres, 
                                                   releaseDate, budget, revenue, adult, belongsToCollection, 
                                                   homepage, imdbId, overview, productionCompanies, 
                                                   productionCountries, runtime, spokenLanguages, status, tagline);
                    peliculas.add(pelicula);

                    // Procesar colección si existe
                    if (belongsToCollection != null && !belongsToCollection.trim().isEmpty() && !belongsToCollection.equals("null")) {
                        procesarColeccion(belongsToCollection, id);
                    }

                } catch (Exception e) {
                    // Continuar con la siguiente línea si hay error en esta
                    continue;
                }
            }
        }
        br.close();
    }

    private void cargarCreditos() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("credits.csv"));
        String linea;
        boolean primeraLinea = true;

        while ((linea = br.readLine()) != null) {
            if (primeraLinea) {
                primeraLinea = false;
                continue; // Saltar header
            }

            String[] campos = parsearCSV(linea);
            if (campos.length >= 3) {
                try {
                    String cast = campos[0];
                    String crew = campos[1];
                    String movieId = campos[2];

                    // Procesar cast (actores)
                    procesarCast(cast, movieId);
                    
                    // Procesar crew (directores)
                    procesarCrew(crew, movieId);

                } catch (Exception e) {
                    continue;
                }
            }
        }
        br.close();
    }

    private void cargarCalificaciones() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("ratings_1mm.csv"));
        String linea;
        boolean primeraLinea = true;

        while ((linea = br.readLine()) != null) {
            if (primeraLinea) {
                primeraLinea = false;
                continue; // Saltar header
            }

            String[] campos = parsearCSV(linea);
            if (campos.length >= 4) {
                try {
                    String userId = campos[0];
                    String movieId = campos[1];
                    double rating = Double.parseDouble(campos[2]);
                    long timestamp = Long.parseLong(campos[3]);

                    Calificacion calificacion = new Calificacion(movieId, userId, rating, timestamp);
                    calificaciones.add(calificacion);

                    // Crear/actualizar usuario
                    actualizarUsuario(userId, calificacion);

                } catch (Exception e) {
                    continue;
                }
            }
        }
        br.close();
    }

    private void calcularEstadisticas() {
        // Calcular estadísticas de películas
        for (int i = 0; i < peliculas.getSize(); i++) {
            Pelicula pelicula = peliculas.get(i);
            calcularEstadisticasPelicula(pelicula);
        }

        // Calcular estadísticas de directores
        for (int i = 0; i < directores.getSize(); i++) {
            Director director = directores.get(i);
            calcularEstadisticasDirector(director);
        }

        // Calcular ingresos de colecciones
        for (int i = 0; i < colecciones.getSize(); i++) {
            Coleccion coleccion = colecciones.get(i);
            calcularIngresosColeccion(coleccion);
        }
    }

    private void calcularEstadisticasPelicula(Pelicula pelicula) {
        MyArrayListImpl<Double> ratings = new MyArrayListImpl<>(100000000);
        
        for (int i = 0; i < calificaciones.getSize(); i++) {
            Calificacion cal = calificaciones.get(i);
            if (cal.getIdPelicula().equals(pelicula.getId())) {
                ratings.add(cal.getRating());
            }
        }

        pelicula.setTotalEvaluaciones(ratings.getSize());
        if (ratings.getSize() > 0) {
            double suma = 0;
            for (int i = 0; i < ratings.getSize(); i++) {
                suma += ratings.get(i);
            }
            pelicula.setCalificacionPromedio(suma / ratings.getSize());
        }
    }

    private void calcularEstadisticasDirector(Director director) {
        MyArrayListImpl<Double> ratings = new MyArrayListImpl<>(100000000);
        int totalEvaluaciones = 0;

        for (int i = 0; i < director.getPeliculas().getSize(); i++) {
            String movieId = director.getPeliculas().get(i);
            for (int j = 0; j < calificaciones.getSize(); j++) {
                Calificacion cal = calificaciones.get(j);
                if (cal.getIdPelicula().equals(movieId)) {
                    ratings.add(cal.getRating());
                    totalEvaluaciones++;
                }
            }
        }

        director.setTotalEvaluaciones(totalEvaluaciones);
        if (ratings.getSize() > 0) {
            // Calcular mediana
            ordenarRatings(ratings);
            double mediana;
            int size = ratings.getSize();
            if (size % 2 == 0) {
                mediana = (ratings.get(size/2 - 1) + ratings.get(size/2)) / 2.0;
            } else {
                mediana = ratings.get(size/2);
            }
            director.setMedianaCalificacion(mediana);
        }
    }

    private void calcularIngresosColeccion(Coleccion coleccion) {
        long ingresos = 0;
        for (int i = 0; i < coleccion.getIdPeliculas().getSize(); i++) {
            String movieId = coleccion.getIdPeliculas().get(i);
            Pelicula pelicula = buscarPelicula(movieId);
            if (pelicula != null) {
                ingresos += pelicula.getRevenue();
            }
        }
        coleccion.setIngresosGenerados(ingresos);
    }

    private void ordenarRatings(MyArrayListImpl<Double> ratings) {
        // Bubble sort simple
        for (int i = 0; i < ratings.getSize() - 1; i++) {
            for (int j = 0; j < ratings.getSize() - 1 - i; j++) {
                if (ratings.get(j) > ratings.get(j + 1)) {
                    Double temp = ratings.get(j);
                    ratings.add(ratings.get(j + 1), j);
                    ratings.add(temp, j + 1);
                }
            }
        }
    }

    // Métodos de procesamiento (sin cambios)
    private void procesarColeccion(String coleccionStr, String movieId) {
        if (coleccionStr.contains("'id':") && coleccionStr.contains("'name':")) {
            try {
                String idStr = extraerValorJSON(coleccionStr, "'id':");
                String nombre = extraerValorJSON(coleccionStr, "'name':'");
                
                if (idStr != null && nombre != null) {
                    Coleccion coleccion = buscarOCrearColeccion(idStr, nombre);
                    coleccion.agregarPelicula(movieId);
                }
            } catch (Exception e) {
                // Ignorar errores de parsing
            }
        }
    }

    private void procesarCast(String cast, String movieId) {
        if (cast != null && cast.contains("'name':")) {
            String[] actoresParts = cast.split("'name':");
            for (int i = 1; i < actoresParts.length; i++) {
                try {
                    String nombreActor = extraerNombreDeJSON(actoresParts[i]);
                    if (nombreActor != null) {
                        Actor actor = buscarOCrearActor(nombreActor);
                        actor.agregarPelicula(movieId);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    private void procesarCrew(String crew, String movieId) {
        if (crew != null && crew.contains("'job': 'Director'")) {
            String[] crewParts = crew.split("'name':");
            for (int i = 1; i < crewParts.length; i++) {
                try {
                    String parte = crewParts[i];
                    if (parte.contains("'job': 'Director'")) {
                        String nombreDirector = extraerNombreDeJSON(parte);
                        if (nombreDirector != null) {
                            Director director = buscarOCrearDirector(nombreDirector);
                            director.agregarPelicula(movieId);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    private void actualizarUsuario(String userId, Calificacion calificacion) {
        Usuario usuario = buscarUsuario(userId);
        if (usuario == null) {
            usuario = new Usuario(userId);
            usuarios.add(usuario);
        }
        usuario.agregarCalificacion(calificacion);
    }

    // Métodos auxiliares
    private String[] parsearCSV(String linea) {
        return linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private long parsearLong(String valor) {
        try {
            return Long.parseLong(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parsearDouble(String valor) {
        try {
            return Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String extraerValorJSON(String json, String clave) {
        int index = json.indexOf(clave);
        if (index == -1) return null;
        
        int start = index + clave.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        if (end == -1) end = json.length();
        
        String valor = json.substring(start, end).trim();
        return valor.replaceAll("^['\"]|['\"]$", "");
    }

    private String extraerNombreDeJSON(String jsonPart) {
        int start = jsonPart.indexOf("'");
        if (start == -1) return null;
        int end = jsonPart.indexOf("'", start + 1);
        if (end == -1) return null;
        return jsonPart.substring(start + 1, end);
    }

    private Coleccion buscarOCrearColeccion(String id, String nombre) {
        for (int i = 0; i < colecciones.getSize(); i++) {
            if (colecciones.get(i).getId().equals(id)) {
                return colecciones.get(i);
            }
        }
        Coleccion nueva = new Coleccion(id, nombre);
        colecciones.add(nueva);
        return nueva;
    }

    private Actor buscarOCrearActor(String nombre) {
        for (int i = 0; i < actores.getSize(); i++) {
            if (actores.get(i).getNombre().equals(nombre)) {
                return actores.get(i);
            }
        }
        Actor nuevo = new Actor(nombre);
        actores.add(nuevo);
        return nuevo;
    }

    private Director buscarOCrearDirector(String nombre) {
        for (int i = 0; i < directores.getSize(); i++) {
            if (directores.get(i).getNombre().equals(nombre)) {
                return directores.get(i);
            }
        }
        Director nuevo = new Director(nombre);
        directores.add(nuevo);
        return nuevo;
    }

    private Usuario buscarUsuario(String id) {
        for (int i = 0; i < usuarios.getSize(); i++) {
            if (usuarios.get(i).getId().equals(id)) {
                return usuarios.get(i);
            }
        }
        return null;
    }

    private Pelicula buscarPelicula(String id) {
        for (int i = 0; i < peliculas.getSize(); i++) {
            if (peliculas.get(i).getId().equals(id)) {
                return peliculas.get(i);
            }
        }
        return null;
    }

    private void menuSecundario() {
        int opcion = 0;

        while (opcion != 7) {
            System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
            System.out.println("2. Top 10 de las películas que mejor calificación media tienen por parte de los usuarios.");
            System.out.println("3. Top 5 de las colecciones que más ingresos generaron.");
            System.out.println("4. Top 10 de los directores que mejor calificación tienen.");
            System.out.println("5. Actor con más calificaciones recibidas en cada mes del año.");
            System.out.println("6. Usuarios con más calificaciones por género");
            System.out.println("7. Salir");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Debe ser un entero entre 1 y 7");
                continue;
            }

            switch (opcion) {
                case 1:
                    inicio = System.currentTimeMillis();
                    consulta1();
                    fin = System.currentTimeMillis();
                    System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + "ms");
                    break;
                case 2:
                    inicio = System.currentTimeMillis();
                    consulta2();
                    fin = System.currentTimeMillis();
                    System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + "ms");
                    break;
                case 3:
                    inicio = System.currentTimeMillis();
                    consulta3();
                    fin = System.currentTimeMillis();
                    System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + "ms");
                    break;
                case 4:
                    inicio = System.currentTimeMillis();
                    consulta4();
                    fin = System.currentTimeMillis();
                    System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + "ms");
                    break;
                case 5:
                    inicio = System.currentTimeMillis();
                    consulta5();
                    fin = System.currentTimeMillis();
                    System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + "ms");
                    break;
                case 6:
                    inicio = System.currentTimeMillis();
                    consulta6();
                    fin = System.currentTimeMillis();
                    System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + "ms");
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
    }

    // IMPLEMENTACIÓN DE CONSULTAS

    // 1. Top 5 de las películas que más calificaciones por idioma
    private void consulta1() {
        String[] idiomas = {"en", "fr", "it", "es", "pt"};
        
        for (String idioma : idiomas) {
            MyArrayListImpl<Pelicula> peliculasIdioma = new MyArrayListImpl<>(100000000);
            
            // Filtrar películas por idioma
            for (int i = 0; i < peliculas.getSize(); i++) {
                Pelicula p = peliculas.get(i);
                if (p.getOriginalLanguage().equals(idioma)) {
                    peliculasIdioma.add(p);
                }
            }
            
            // Ordenar por total de evaluaciones (descendente)
            ordenarPorEvaluaciones(peliculasIdioma);
            
            // Mostrar top 5
            int limite = Math.min(5, peliculasIdioma.getSize());
            for (int i = 0; i < limite; i++) {
                Pelicula p = peliculasIdioma.get(i);
                System.out.println(p.getId() + "," + p.getTitle() + "," + 
                                 p.getTotalEvaluaciones() + "," + p.getOriginalLanguage());
            }
        }
    }

    // 2. Top 10 de las películas que mejor calificación media tienen
    private void consulta2() {
        MyArrayListImpl<Pelicula> peliculasConCalificacion = new MyArrayListImpl<>(100000000);
        
        // Filtrar películas con calificaciones
        for (int i = 0; i < peliculas.getSize(); i++) {
            Pelicula p = peliculas.get(i);
            if (p.getTotalEvaluaciones() > 0) {
                peliculasConCalificacion.add(p);
            }
        }
        
        // Ordenar por calificación promedio (descendente)
        ordenarPorCalificacionPromedio(peliculasConCalificacion);
        
        // Mostrar top 10
        int limite = Math.min(10, peliculasConCalificacion.getSize());
        for (int i = 0; i < limite; i++) {
            Pelicula p = peliculasConCalificacion.get(i);
            System.out.println(p.getId() + "," + p.getTitle() + "," + 
                             String.format("%.2f", p.getCalificacionPromedio()));
        }
    }

    // 3. Top 5 de las colecciones que más ingresos generaron
    private void consulta3() {
        MyArrayListImpl<Coleccion> coleccionesConIngresos = new MyArrayListImpl<>(100000000);
        
        // Agregar colecciones existentes
        for (int i = 0; i < colecciones.getSize(); i++) {
            coleccionesConIngresos.add(colecciones.get(i));
        }
        
        // Agregar películas individuales como colecciones
        for (int i = 0; i < peliculas.getSize(); i++) {
            Pelicula p = peliculas.get(i);
            if (p.getBelongsToCollection() == null || p.getBelongsToCollection().isEmpty() || 
                p.getBelongsToCollection().equals("null")) {
                Coleccion col = new Coleccion(p.getId(), p.getTitle());
                col.agregarPelicula(p.getId());
                col.setIngresosGenerados(p.getRevenue());
                coleccionesConIngresos.add(col);
            }
        }
        
        // Ordenar por ingresos (descendente)
        ordenarPorIngresos(coleccionesConIngresos);
        
        // Mostrar top 5
        int limite = Math.min(5, coleccionesConIngresos.getSize());
        for (int i = 0; i < limite; i++) {
            Coleccion c = coleccionesConIngresos.get(i);
            StringBuilder ids = new StringBuilder("[");
            for (int j = 0; j < c.getIdPeliculas().getSize(); j++) {
                if (j > 0) ids.append(",");
                ids.append(c.getIdPeliculas().get(j));
            }
            ids.append("]");
            
            System.out.println(c.getId() + "," + c.getNombre() + "," + 
                             c.getCantidadPeliculas() + "," + ids.toString() + "," + 
                             c.getIngresosGenerados());
        }
    }

    // 4. Top 10 de los directores que mejor calificación tienen
    private void consulta4() {
        MyArrayListImpl<Director> directoresCalificados = new MyArrayListImpl<>(100000000);
        
        // Filtrar directores con más de 1 película y más de 100 evaluaciones
        for (int i = 0; i < directores.getSize(); i++) {
            Director d = directores.get(i);
            if (d.getCantidadPeliculas() > 1 && d.getTotalEvaluaciones() > 100) {
                directoresCalificados.add(d);
            }
        }
        
        // Ordenar por mediana de calificación (descendente)
        ordenarPorMediana(directoresCalificados);
        
        // Mostrar top 10
        int limite = Math.min(10, directoresCalificados.getSize());
        for (int i = 0; i < limite; i++) {
            Director d = directoresCalificados.get(i);
            System.out.println(d.getNombre() + "," + d.getCantidadPeliculas() + "," + 
                             String.format("%.2f", d.getMedianaCalificacion()));
        }
    }

    // 5. Actor con más calificaciones recibidas en cada mes del año
    private void consulta5() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        
        for (int mes = 1; mes <= 12; mes++) {
            MyArrayListImpl<ActorEstadistica> actoresDelMes = new MyArrayListImpl<>(100000000);
            
            // Para cada actor, contar calificaciones en ese mes
            for (int i = 0; i < actores.getSize(); i++) {
                Actor actor = actores.get(i);
                int calificacionesDelMes = 0;
                int peliculasDelMes = 0;
                
                for (int j = 0; j < actor.getPeliculas().getSize(); j++) {
                    String movieId = actor.getPeliculas().get(j);
                    
                    // Contar calificaciones de esta película en este mes
                    for (int k = 0; k < calificaciones.getSize(); k++) {
                        Calificacion cal = calificaciones.get(k);
                        if (cal.getIdPelicula().equals(movieId)) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(cal.getTimestamp() * 1000);
                            int mesCalificacion = calendar.get(Calendar.MONTH) + 1;
                            
                            if (mesCalificacion == mes) {
                                calificacionesDelMes++;
                                peliculasDelMes++;
                            }
                        }
                    }
                }
                
                if (calificacionesDelMes > 0) {
                    ActorEstadistica est = new ActorEstadistica(actor.getNombre(), 
                                                              peliculasDelMes, calificacionesDelMes);
                    actoresDelMes.add(est);
                }
            }
            
            // Encontrar el actor con más calificaciones
            if (actoresDelMes.getSize() > 0) {
                ActorEstadistica mejor = actoresDelMes.get(0);
                for (int i = 1; i < actoresDelMes.getSize(); i++) {
                    if (actoresDelMes.get(i).calificaciones > mejor.calificaciones) {
                        mejor = actoresDelMes.get(i);
                    }
                }
                
                System.out.println(meses[mes-1] + "," + mejor.nombre + "," + 
                                 mejor.peliculas + "," + mejor.calificaciones);
            }
        }
    }

    // 6. Usuarios con más calificaciones por género
    private void consulta6() {
        MyArrayListImpl<String> generos = obtenerTop10Generos();
        
        for (int i = 0; i < generos.getSize(); i++) {
            String genero = generos.get(i);
            Usuario mejorUsuario = null;
            int maxCalificaciones = 0;
            
            for (int j = 0; j < usuarios.getSize(); j++) {
                Usuario usuario = usuarios.get(j);
                int calificacionesGenero = contarCalificacionesPorGenero(usuario, genero);
                
                if (calificacionesGenero > maxCalificaciones) {
                    maxCalificaciones = calificacionesGenero;
                    mejorUsuario = usuario;
                }
            }
            
            if (mejorUsuario != null) {
                System.out.println(mejorUsuario.getId() + "," + genero + "," + maxCalificaciones);
            }
        }
    }

    // Métodos auxiliares para consultas
    private void ordenarPorEvaluaciones(MyArrayListImpl<Pelicula> peliculas) {
        for (int i = 0; i < peliculas.getSize() - 1; i++) {
            for (int j = 0; j < peliculas.getSize() - 1 - i; j++) {
                if (peliculas.get(j).getTotalEvaluaciones() < peliculas.get(j + 1).getTotalEvaluaciones()) {
                    Pelicula temp = peliculas.get(j);
                    peliculas.add(peliculas.get(j + 1), j);
                    peliculas.add(temp, j + 1);
                }
            }
        }
    }

    private void ordenarPorCalificacionPromedio(MyArrayListImpl<Pelicula> peliculas) {
        for (int i = 0; i < peliculas.getSize() - 1; i++) {
            for (int j = 0; j < peliculas.getSize() - 1 - i; j++) {
                if (peliculas.get(j).getCalificacionPromedio() < peliculas.get(j + 1).getCalificacionPromedio()) {
                    Pelicula temp = peliculas.get(j);
                    peliculas.add(peliculas.get(j + 1), j);
                    peliculas.add(temp, j + 1);
                }
            }
        }
    }

    private void ordenarPorIngresos(MyArrayListImpl<Coleccion> colecciones) {
        for (int i = 0; i < colecciones.getSize() - 1; i++) {
            for (int j = 0; j < colecciones.getSize() - 1 - i; j++) {
                if (colecciones.get(j).getIngresosGenerados() < colecciones.get(j + 1).getIngresosGenerados()) {
                    Coleccion temp = colecciones.get(j);
                    colecciones.add(colecciones.get(j + 1), j);
                    colecciones.add(temp, j + 1);
                }
            }
        }
    }

    private void ordenarPorMediana(MyArrayListImpl<Director> directores) {
        for (int i = 0; i < directores.getSize() - 1; i++) {
            for (int j = 0; j < directores.getSize() - 1 - i; j++) {
                if (directores.get(j).getMedianaCalificacion() < directores.get(j + 1).getMedianaCalificacion()) {
                    Director temp = directores.get(j);
                    directores.add(directores.get(j + 1), j);
                    directores.add(temp, j + 1);
                }
            }
        }
    }

    private MyArrayListImpl<String> obtenerTop10Generos() {
        MyArrayListImpl<GeneroContador> generos = new MyArrayListImpl<>(100000000);
        
        // Contar calificaciones por género
        for (int i = 0; i < calificaciones.getSize(); i++) {
            Calificacion cal = calificaciones.get(i);
            Pelicula pelicula = buscarPelicula(cal.getIdPelicula());
            
            if (pelicula != null && pelicula.getGenres() != null) {
                String[] generosArray = extraerGeneros(pelicula.getGenres());
                for (String genero : generosArray) {
                    if (genero != null && !genero.trim().isEmpty()) {
                        GeneroContador gc = buscarGeneroContador(generos, genero);
                        if (gc == null) {
                            generos.add(new GeneroContador(genero, 1));
                        } else {
                            gc.incrementar();
                        }
                    }
                }
            }
        }
        
        // Ordenar por cantidad (descendente)
        ordenarGenerosPorContador(generos);
        
        // Retornar top 10
        MyArrayListImpl<String> top10 = new MyArrayListImpl<>(100000000);
        int limite = Math.min(10, generos.getSize());
        for (int i = 0; i < limite; i++) {
            top10.add(generos.get(i).genero);
        }
        
        return top10;
    }

    private String[] extraerGeneros(String generosStr) {
        if (generosStr == null || generosStr.trim().isEmpty()) {
            return new String[0];
        }
        
        MyArrayListImpl<String> generos = new MyArrayListImpl<>(100000000);
        
        // Buscar patrones de género en formato JSON
        String[] parts = generosStr.split("'name':");
        for (int i = 1; i < parts.length; i++) {
            String genero = extraerNombreDeJSON(parts[i]);
            if (genero != null) {
                generos.add(genero);
            }
        }
        
        // Convertir a array
        String[] resultado = new String[generos.getSize()];
        for (int i = 0; i < generos.getSize(); i++) {
            resultado[i] = generos.get(i);
        }
        
        return resultado;
    }

    private GeneroContador buscarGeneroContador(MyArrayListImpl<GeneroContador> generos, String genero) {
        for (int i = 0; i < generos.getSize(); i++) {
            if (generos.get(i).genero.equals(genero)) {
                return generos.get(i);
            }
        }
        return null;
    }

    private void ordenarGenerosPorContador(MyArrayListImpl<GeneroContador> generos) {
        for (int i = 0; i < generos.getSize() - 1; i++) {
            for (int j = 0; j < generos.getSize() - 1 - i; j++) {
                if (generos.get(j).contador < generos.get(j + 1).contador) {
                    GeneroContador temp = generos.get(j);
                    generos.add(generos.get(j + 1), j);
                    generos.add(temp, j + 1);
                }
            }
        }
    }

    private int contarCalificacionesPorGenero(Usuario usuario, String genero) {
        int contador = 0;
        
        for (int i = 0; i < usuario.getCalificaciones().getSize(); i++) {
            Calificacion cal = usuario.getCalificaciones().get(i);
            Pelicula pelicula = buscarPelicula(cal.getIdPelicula());
            
            if (pelicula != null && pelicula.getGenres() != null) {
                String[] generos = extraerGeneros(pelicula.getGenres());
                for (String g : generos) {
                    if (g != null && g.equals(genero)) {
                        contador++;
                        break; // Solo contar una vez por película
                    }
                }
            }
        }
        
        return contador;
    }

    // Clases auxiliares internas
    private class ActorEstadistica {
        String nombre;
        int peliculas;
        int calificaciones;
        
        public ActorEstadistica(String nombre, int peliculas, int calificaciones) {
            this.nombre = nombre;
            this.peliculas = peliculas;
            this.calificaciones = calificaciones;
        }
    }

    private class GeneroContador {
        String genero;
        int contador;
        
        public GeneroContador(String genero, int contador) {
            this.genero = genero;
            this.contador = contador;
        }
        
        public void incrementar() {
            this.contador++;
        }
}
    public static void main(String[] args) {
        UMovie app = new UMovie();
        app.menuPrincipal();
    }
}
