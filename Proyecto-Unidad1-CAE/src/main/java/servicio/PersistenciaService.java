package servicio;

import estructuras.cola.QuequeCAE;
import estructuras.lista.ListaNotas;
import modelo.Estado;
import modelo.Nota;
import modelo.Ticket;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.TreeMap;

public class PersistenciaService {

    // Rutas de los archivos CSV
    private static final String RUTA_TICKETS = "data_tickets.csv";
    private static final String RUTA_NOTAS = "data_notas.csv";
    private static final String RUTA_CONTADORES = "data_contadores.csv";
    private static final String DELIMITADOR = ",";

    public void guardarDatos(GestorTickets gestor) {
        // Lógica para guardar datos (Request #8)
        // 1. Abrir 'PrintWriter' para RUTA_TICKETS, RUTA_NOTAS, RUTA_CONTADORES.
        // 2. Guardar contadores: Ticket.getNextGeneradorId()
        // 3. Iterar sobre 'colaUrgente', 'colaNormal' y 'ticketsFinalizados'.
        //    - Por cada ticket:
        //      - Escribir en RUTA_TICKETS: id,nombre,estado,esUrgente,nextNotaId
        //      - Iterar sobre la 'listaNotas' del ticket:
        //        - Escribir en RUTA_NOTAS: ticketId,notaId,fechaHora,texto
        // 4. Manejar 'IOException' (Request #10).
    }

    public void cargarDatos(GestorTickets gestor) {
        // Lógica para cargar datos (Request #8)
        // 1. Limpiar el gestor: gestor.limpiarTodo()
        // 2. Cargar contadores de RUTA_CONTADORES y setear Ticket.setGeneradorId()
        // 3. Cargar tickets:
        //    - Leer RUTA_TICKETS línea por línea.
        //    - Parsear los datos.
        //    - Crear 'Ticket' (usando el constructor de persistencia).
        //    - Almacenarlos temporalmente (p.ej. en un TreeMap<Integer, Ticket>).
        // 4. Cargar notas:
        //    - Leer RUTA_NOTAS línea por línea.
        //    - Parsear (ticketId, notaId, fecha, texto).
        //    - Buscar el ticket en el TreeMap temporal.
        //    - Añadir la nota al ticket (usando el constructor de nota de persistencia).
        // 5. Repoblar el gestor:
        //    - Iterar el TreeMap de tickets cargados.
        //    - Según el 'estado' del ticket, encolarlo en 'colaUrgente', 'colaNormal'
        //      o añadirlo a 'ticketsFinalizados'.
        // 6. Manejar 'FileNotFoundException', 'IOException' (Request #10).
    }
}