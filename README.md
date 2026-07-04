# Analizador de Datos Cinematográficos (UMovie)

Aplicación de consola en Java desarrollada para el procesamiento y análisis de grandes volúmenes de datos cinematográficos utilizando un motor completo de estructuras de datos propias. El sistema procesa datasets públicos de películas, créditos y calificaciones, permitiendo ejecutar consultas estadísticas con medición de tiempos de ejecución en milisegundos.

## Características Técnicas
* **Sin dependencias externas:** Todo el procesamiento de archivos CSV y el parseo de estructuras complejas (simulando formato JSON) se realiza mediante lógica nativa de manipulación de Strings.
* **Estructuras de Datos Propias (TADs):** Implementación desde cero de la biblioteca completa de estructuras de datos (Listas, Pilas, Colas, Tablas Hash, Heaps y Árboles Binarios de Búsqueda) optimizadas para la gestión de memoria con grandes volúmenes de registros.
* **Algoritmos de Ordenamiento y Búsqueda:** Lógica de ordenación y filtrado integrada para la generación de rankings masivos en tiempo real y el cálculo de métricas estadísticas complejas (como la mediana de calificaciones).

## Consultas Implementadas
El sistema resuelve las siguientes consultas solicitadas por el menú de la aplicación:
1. **Top 5 de películas con más calificaciones por idioma** (filtrado para en, fr, it, es, pt).
2. **Top 10 de películas con mejor calificación media** otorgada por los usuarios.
3. **Top 5 de colecciones cinematográficas con mayores ingresos generados** (incluye películas individuales tratadas como colecciones de un único elemento).
4. **Top 10 de directores con mejor calificación**, basado en la mediana de sus películas (filtrando directores con más de 1 película y más de 100 evaluaciones totales).
5. **Actor con más calificaciones recibidas por cada mes del año**.
6. **Usuarios con mayor cantidad de calificaciones** desglosadas por género cinematográfico.

## Estructura del Proyecto
```text
src/
├── entities/       # Clases de soporte (Pelicula, Director, Actor, Usuario, etc.)
├── tad/            # Biblioteca completa de Estructuras de Datos Propias
│   ├── exceptions/ # Excepciones personalizadas del motor
│   ├── hash/       # Implementación de Tabla Hash
│   ├── heap/       # Implementación de Heap (Montículo)
│   ├── list/       # Implementación de Lista Dinámica e Iteradores
│   ├── queue/      # Implementación de Cola
│   ├── stack/      # Implementación de Pila
│   └── tree/       # Implementación de Árbol Binario de Búsqueda
└── UMovie.java     # Clase principal, controladores de carga y menú de usuario
