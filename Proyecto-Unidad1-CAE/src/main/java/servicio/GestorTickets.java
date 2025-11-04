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
        Estado estadoInicial = esUrgente ? Estado.URGENTE : Estado.EN_COLA;
        Ticket nuevo = new Ticket(nombreCliente, estadoInicial, esUrgente);

        if (esUrgente)
            colaUrgente.enqueque(nuevo);
        else
            colaNormal.enqueque(nuevo);

        AccionRecibirTicket accion = new AccionRecibirTicket(nuevo, esUrgente ? colaUrgente : colaNormal);
        undoRedoGlobal.registrarAccion(accion);

        System.out.printf("Se ha registrado un nuevo ticket para %s (%s)%n",
                nombreCliente, esUrgente ? "urgente" : "normal");
    }

    public void listarCasosEnEspera() {
        // Lógica para mostrar el contenido de 'colaUrgente', 'colaNormal'
        // y también 'ticketEnAtencion' si existe (Request #6).
        // Interactúa con: QuequeCAE.listar()
        System.out.println("Tickets urgentes en espera:");
        colaUrgente.listar();
        System.out.println("Tickets normales en espera:");
        colaNormal.listar();

        if (ticketEnAtencion != null)
            System.out.println("Actualmente se está atendiendo: " + ticketEnAtencion);
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
        if (ticketEnAtencion != null) {
            System.err.println("Ya se está atendiendo un ticket. Finalízalo antes de continuar.");
            return false;
        }

        Ticket siguiente = colaUrgente.dequeue();
        if (siguiente == null)
            siguiente = colaNormal.dequeue();

        if (siguiente == null) {
            System.err.println("No hay tickets pendientes por atender.");
            return false;
        }

        siguiente.cambiarEstado(Estado.EN_ATENCION);
        ticketEnAtencion = siguiente;
        undoRedoTicket.limpiar();

        AccionIniciarAtencion accion = new AccionIniciarAtencion(ticketEnAtencion,
                ticketEnAtencion.esUrgente() ? colaUrgente : colaNormal, this);
        undoRedoGlobal.registrarAccion(accion);

        System.out.println("Comenzando atención del ticket: " + ticketEnAtencion);
        return true;
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
        if (ticketEnAtencion == null) {
            System.err.println("No se está atendiendo ningún ticket en este momento.");
            return false;
        }

        Estado estadoActual = ticketEnAtencion.getEstado();
        AccionFinalizarCaso accion = new AccionFinalizarCaso(ticketEnAtencion,
                ticketEnAtencion.esUrgente() ? colaUrgente : colaNormal, ticketsFinalizados);

        if (estadoActual == Estado.COMPLETADO) {
            ticketsFinalizados.put(ticketEnAtencion.getId(), ticketEnAtencion);
            System.out.println("El ticket se ha completado correctamente: " + ticketEnAtencion);
        } else if (estadoActual == Estado.PENDIENTE_DOCS || estadoActual == Estado.EN_PROCESO) {
            if (ticketEnAtencion.esUrgente())
                colaUrgente.enqueque(ticketEnAtencion);
            else
                colaNormal.enqueque(ticketEnAtencion);
            System.out.println("El ticket se ha devuelto a la cola para continuar más tarde: " + ticketEnAtencion);
        } else {
            System.err.println("No se puede finalizar un ticket en este estado: " + estadoActual);
            return false;
        }

        undoRedoGlobal.registrarAccion(accion);
        ticketEnAtencion = null;
        undoRedoTicket.limpiar();

        return true;
    }

    // --- Métodos de gestión de ticket (usan undoRedoTicket) ---
    public boolean registrarNota(String texto) {
        // Lógica sin cambios, pero ahora usa 'undoRedoTicket'.
        // ... (validar ticketEnAtencion) ...
        // Nota nuevaNota = ticketEnAtencion.agregarNota(texto);
        // AccionAgregarNota accion = new AccionAgregarNota(ticketEnAtencion, nuevaNota);
        // undoRedoTicket.registrarAccion(accion);
        if (ticketEnAtencion == null) {
            System.err.println("No se está atendiendo ningún ticket para agregar una nota.");
            return false;
        }

        Nota nuevaNota = ticketEnAtencion.agregarNota(texto);
        AccionAgregarNota accion = new AccionAgregarNota(ticketEnAtencion, nuevaNota);
        undoRedoTicket.registrarAccion(accion);

        System.out.println("Se agregó una nueva nota: \"" + texto + "\"");
        return true;
    }

    public boolean eliminarNota(int idNota) {
        // Lógica sin cambios, pero ahora usa 'undoRedoTicket'.
        // ... (validar ticketEnAtencion) ...
        // Nota notaEliminada = ticketEnAtencion.eliminarNota(idNota);
        // ... (if notaEliminada != null) ...
        // AccionEliminarNota accion = new AccionEliminarNota(ticketEnAtencion.getListaNotas(), notaEliminada);
        // undoRedoTicket.registrarAccion(accion);
        if (ticketEnAtencion == null) {
            System.err.println("No se está atendiendo ningún ticket para eliminar una nota.");
            return false;
        }

        Nota notaEliminada = ticketEnAtencion.eliminarNota(idNota);
        if (notaEliminada == null) {
            System.err.println("No existe ninguna nota con el ID " + idNota);
            return false;
        }

        AccionEliminarNota accion = new AccionEliminarNota(ticketEnAtencion.getListaNotas(), notaEliminada);
        undoRedoTicket.registrarAccion(accion);

        System.out.println("Se eliminó la nota: \"" + notaEliminada.getTexto() + "\"");
        return true;
    }

    public boolean cambiarEstadoInterno(Estado nuevoEstado) {
        // Lógica sin cambios, pero ahora usa 'undoRedoTicket'.
        // ... (validar ticketEnAtencion) ...
        // Estado estadoAnterior = ticketEnAtencion.getEstado();
        // ... (validar transición [cite: 39-44] y que no sea el mismo estado) ...
        // ticketEnAtencion.cambiarEstado(nuevoEstado);
        // AccionCambiarEstado accion = new AccionCambiarEstado(ticketEnAtencion, estadoAnterior, nuevoEstado);
        // undoRedoTicket.registrarAccion(accion);
        if (ticketEnAtencion == null) {
            System.err.println("No se está atendiendo ningún ticket para cambiar su estado.");
            return false;
        }

        Estado estadoAnterior = ticketEnAtencion.getEstado();
        if (estadoAnterior == nuevoEstado) {
            System.err.println("El ticket ya está en ese estado.");
            return false;
        }

        ticketEnAtencion.cambiarEstado(nuevoEstado);
        AccionCambiarEstado accion = new AccionCambiarEstado(ticketEnAtencion, estadoAnterior, nuevoEstado);
        undoRedoTicket.registrarAccion(accion);

        System.out.printf("Estado cambiado de %s a %s%n", estadoAnterior, nuevoEstado);
        return true;
    }

    // --- Métodos de Undo/Redo (Ticket vs Global) ---
    public boolean deshacerAccionTicket() {
        // Llama a undoRedoTicket.deshacer()
        return undoRedoTicket.deshacer();
    }

    public boolean rehacerAccionTicket() {
        // Llama a undoRedoTicket.rehacer()
        return undoRedoTicket.rehacer();
    }

    public boolean deshacerAccionGlobal() {
        // --- NUEVO (Request #5) ---
        // Llama a undoRedoGlobal.deshacer()
        return undoRedoGlobal.deshacer();
    }

    public boolean rehacerAccionGlobal() {
        // --- NUEVO (Request #5) ---
        // Llama a undoRedoGlobal.rehacer()
        return undoRedoGlobal.rehacer();
    }

    // --- Métodos de Consulta y Reportes ---
    public void listarTicketsFinalizados() {
        // --- NUEVO (Request #7) ---
        // Itera sobre 'ticketsFinalizados.values()' y los imprime.
        // Muestra (ID, Nombre, Estado)
        if (ticketsFinalizados.isEmpty()) {
            System.out.println("Todavía no se ha finalizado ningún ticket.");
            return;
        }

        System.out.println("Listado de tickets finalizados:");
        for (Map.Entry<Integer, Ticket> entry : ticketsFinalizados.entrySet()) {
            System.out.println("  " + entry.getValue());
        }
    }

    public void consultarHistorial(int idTicket) {
        // Lógica sin cambios. Busca en 'ticketsFinalizados'.
        Ticket t = ticketsFinalizados.get(idTicket);
        if (t == null) {
            System.err.println("No existe un ticket con el ID " + idTicket);
            return;
        }
        System.out.println("Historial del ticket " + idTicket + ":");
        t.getListaNotas().recorrer();
    }

    public void ejecutarReporteTopK(int k) {
        // --- NUEVO (Request #11 / PDF [cite: 49]) ---
        // 1. Crear una lista unificada de *todos* los tickets (colas + finalizados).
        // 2. Ordenar la lista usando un Comparator basado en 'ticket.getListaNotas().getTamanio()'.
        // 3. Imprimir los primeros 'k' elementos.
        // Interactúa con: ListaNotas.getTamanio()
        List<Ticket> todos = new ArrayList<>();

        for (var nodo = colaUrgente.getFrente(); nodo != null; nodo = nodo.getSiguiente())
            todos.add(nodo.getDato());
        for (var nodo = colaNormal.getFrente(); nodo != null; nodo = nodo.getSiguiente())
            todos.add(nodo.getDato());
        todos.addAll(ticketsFinalizados.values());

        todos.sort(Comparator.comparingInt(t -> -t.getListaNotas().getTamanio()));

        System.out.printf("Top %d tickets con más notas registradas:%n", k);
        todos.stream().limit(k).forEach(System.out::println);
    }

    // --- Getters y Métodos de Utilidad ---
    public Ticket getTicketEnAtencion() { return ticketEnAtencion; }
    public void setTicketEnAtencion(Ticket ticket) { this.ticketEnAtencion = ticket; }
    public boolean hayTicketsFinalizados() { return !ticketsFinalizados.isEmpty(); }
    public int undoTicketCount() { return undoRedoTicket.getSize(); } // <-- NUEVO
    public int redoTicketCount() { return undoRedoTicket.getRedoSize(); } // <-- NUEVO
    public int undoGlobalCount() { return undoRedoGlobal.getSize(); } // <-- NUEVO
    public int redoGlobalCount() { return undoRedoGlobal.getRedoSize(); } // <-- NUEVO

    // --- Métodos para Persistencia ---
    public QuequeCAE getColaNormal() { return colaNormal; }
    public QuequeCAE getColaUrgente() { return colaUrgente; }
    public TreeMap<Integer, Ticket> getTicketsFinalizados() { return ticketsFinalizados; }

    public void limpiarTodo() {
        // --- NUEVO: Para cargar datos ---
        // Limpia todas las estructuras de datos antes de cargar desde archivo.
        // Interactúa con: PersistenciaService.cargarDatos()
        colaNormal.limpiar();
        colaUrgente.limpiar();
        ticketsFinalizados.clear();
        ticketEnAtencion = null;
        undoRedoGlobal.limpiar();
        undoRedoTicket.limpiar();
    }
}
