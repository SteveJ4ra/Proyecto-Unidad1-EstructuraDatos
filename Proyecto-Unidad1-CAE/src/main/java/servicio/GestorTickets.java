package servicio;

import estructuras.cola.QuequeCAE;
import estructuras.pila.UndoRedoManager;
import modelo.Ticket;
import modelo.Estado;
import modelo.Nota;
import servicio.acciones.*; // Importa todas las acciones

import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class GestorTickets {

    // --- Dos colas: Normal y Urgente [cite: 34] ---
    private final QuequeCAE colaNormal;
    private final QuequeCAE colaUrgente;

    private Ticket ticketEnAtencion;

    // --- Dos managers de Undo/Redo (Request #5) ---
    private final UndoRedoManager undoRedoTicket; // Para acciones *dentro* de un ticket
    private final UndoRedoManager undoRedoGlobal; // Para acciones *del sistema* (encolar, atender, etc.)

    private final TreeMap<Integer, Ticket> ticketsFinalizados; // Estado: COMPLETADO

    public GestorTickets() {
        this.colaNormal = new QuequeCAE();
        this.colaUrgente = new QuequeCAE();
        this.undoRedoTicket = new UndoRedoManager();
        this.undoRedoGlobal = new UndoRedoManager(); // <-- NUEVO
        this.ticketsFinalizados = new TreeMap<>();
    }

    public void recibirNuevoCaso(String nombreCliente, boolean esUrgente) {
        // Lógica para crear el ticket, asignarle estado (EN_COLA o URGENTE)
        // y encolarlo en la cola correspondiente.
        // Debe registrar la acción en 'undoRedoGlobal'.
        // Interactúa con: QuequeCAE.enqueque(), AccionRecibirTicket
    }

    public void listarCasosEnEspera() {
        // Lógica para mostrar el contenido de 'colaUrgente', 'colaNormal'
        // y también 'ticketEnAtencion' si existe (Request #6).
        // Interactúa con: QuequeCAE.listar()
    }

    public boolean iniciarAtencion() {
        // Lógica de atención prioritaria (Request #2) [cite: 50]
        // 1. Validar que no haya ticket en atención.
        // 2. Intentar dequeue de 'colaUrgente'.
        // 3. Si 'colaUrgente' está vacía, intentar dequeue de 'colaNormal'.
        // 4. Si se obtiene un ticket, asignarlo a 'ticketEnAtencion',
        //    cambiar su estado a EN_ATENCION, limpiar 'undoRedoTicket'.
        // 5. Registrar la acción en 'undoRedoGlobal'.
        // Interactúa con: QuequeCAE.dequeue(), AccionIniciarAtencion
        return false;
    }

    public boolean finalizarCaso() {
        // Lógica para finalizar o re-encolar (Request #4)
        // 1. Validar que 'ticketEnAtencion' no sea null.
        // 2. Obtener el estado actual del ticket.
        // 3. Registrar la acción en 'undoRedoGlobal' (guardando el estado anterior).
        // 4. Si el estado es COMPLETADO:
        //    - Moverlo a 'ticketsFinalizados'.
        // 5. Si el estado es PENDIENTE_DOCS o EN_PROCESO (Request #4):
        //    - Re-encolarlo en 'colaUrgente' o 'colaNormal' (según 'ticket.esUrgente()').
        // 6. Si el estado es EN_ATENCION (no se puede finalizar).
        // 7. Limpiar 'ticketEnAtencion' y 'undoRedoTicket'.
        // Interactúa con: ticketsFinalizados.put(), QuequeCAE.enqueque(), AccionFinalizarCaso
        return false;
    }

    // --- Métodos de gestión de ticket (usan undoRedoTicket) ---

    public boolean registrarNota(String texto) {
        // Lógica sin cambios, pero ahora usa 'undoRedoTicket'.
        // ... (validar ticketEnAtencion) ...
        // Nota nuevaNota = ticketEnAtencion.agregarNota(texto);
        // AccionAgregarNota accion = new AccionAgregarNota(ticketEnAtencion, nuevaNota);
        // undoRedoTicket.registrarAccion(accion);
        return false;
    }

    public boolean eliminarNota(int idNota) {
        // Lógica sin cambios, pero ahora usa 'undoRedoTicket'.
        // ... (validar ticketEnAtencion) ...
        // Nota notaEliminada = ticketEnAtencion.eliminarNota(idNota);
        // ... (if notaEliminada != null) ...
        // AccionEliminarNota accion = new AccionEliminarNota(ticketEnAtencion.getListaNotas(), notaEliminada);
        // undoRedoTicket.registrarAccion(accion);
        return false;
    }

    public boolean cambiarEstadoInterno(Estado nuevoEstado) {
        // Lógica sin cambios, pero ahora usa 'undoRedoTicket'.
        // ... (validar ticketEnAtencion) ...
        // Estado estadoAnterior = ticketEnAtencion.getEstado();
        // ... (validar transición [cite: 39-44] y que no sea el mismo estado) ...
        // ticketEnAtencion.cambiarEstado(nuevoEstado);
        // AccionCambiarEstado accion = new AccionCambiarEstado(ticketEnAtencion, estadoAnterior, nuevoEstado);
        // undoRedoTicket.registrarAccion(accion);
        return false;
    }

    // --- Métodos de Undo/Redo (Ticket vs Global) ---

    public boolean deshacerAccionTicket() {
        // Llama a undoRedoTicket.deshacer()
        return false;
    }

    public boolean rehacerAccionTicket() {
        // Llama a undoRedoTicket.rehacer()
        return false;
    }

    public boolean deshacerAccionGlobal() {
        // --- NUEVO (Request #5) ---
        // Llama a undoRedoGlobal.deshacer()
        return false;
    }

    public boolean rehacerAccionGlobal() {
        // --- NUEVO (Request #5) ---
        // Llama a undoRedoGlobal.rehacer()
        return false;
    }

    // --- Métodos de Consulta y Reportes ---

    public void listarTicketsFinalizados() {
        // --- NUEVO (Request #7) ---
        // Itera sobre 'ticketsFinalizados.values()' y los imprime.
        // Muestra (ID, Nombre, Estado)
    }

    public void consultarHistorial(int idTicket) {
        // Lógica sin cambios. Busca en 'ticketsFinalizados'.
    }

    public void ejecutarReporteTopK(int k) {
        // --- NUEVO (Request #11 / PDF [cite: 49]) ---
        // 1. Crear una lista unificada de *todos* los tickets (colas + finalizados).
        // 2. Ordenar la lista usando un Comparator basado en 'ticket.getListaNotas().getTamanio()'.
        // 3. Imprimir los primeros 'k' elementos.
        // Interactúa con: ListaNotas.getTamanio()
    }

    // --- Getters y Métodos de Utilidad ---

    public Ticket getTicketEnAtencion() { return ticketEnAtencion; }
    public boolean hayTicketsFinalizados() { return !ticketsFinalizados.isEmpty(); }
    public int undoTicketCount() { return undoRedoTicket.getSize(); } // <-- NUEVO
    public int redoTicketCount() { return undoRedoTicket.getSize(); } // <-- NUEVO
    public int undoGlobalCount() { return undoRedoGlobal.getSize(); } // <-- NUEVO
    public int redoGlobalCount() { return undoRedoGlobal.getSize(); } // <-- NUEVO

    // --- Métodos para Persistencia ---

    public QuequeCAE getColaNormal() { return colaNormal; }
    public QuequeCAE getColaUrgente() { return colaUrgente; }
    public TreeMap<Integer, Ticket> getTicketsFinalizados() { return ticketsFinalizados; }

    public void limpiarTodo() {
        // --- NUEVO: Para cargar datos ---
        // Limpia todas las estructuras de datos antes de cargar desde archivo.
        // Interactúa con: PersistenciaService.cargarDatos()
    }
}