package estructuras.cola;

import modelo.Ticket;
import modelo.Estado;

public class QuequeCAE {
    private NodoCola frente;
    private NodoCola fin;
    private int tamanio;

    public void enqueque(Ticket ticket) {
        NodoCola nuevo = new NodoCola(ticket);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        tamanio++;
        // Se elimina: ticket.cambiarEstado(Estado.EN_COLA);
        // Esta lógica ahora la maneja GestorTickets
    }

    public void enquequeAlFrente(Ticket ticket) {
        // --- NUEVO: Necesario para 'deshacer' una acción de 'iniciarAtencion' ---
        // Inserta un ticket al inicio de la cola (rompe FIFO, solo para Undo)
        // Interactúa con: AccionIniciarAtencion.deshacer()
    }

    public Ticket dequeue() {
        if (estaVacia()) {
            return null;
        }
        Ticket ticketAtendido = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return ticketAtendido;
    }

    public Ticket eliminarPorId(int id) {
        // --- NUEVO: Necesario para 'deshacer' una acción de 'recibirNuevoCaso' ---
        // Busca y elimina un ticket específico por su ID.
        // Esto es O(n) y es requerido para el Undo global.
        // Interactúa con: AccionRecibirTicket.deshacer()
        return null; // Devuelve el ticket eliminado o null si no se encuentra
    }

    public void listar() {
        if (estaVacia()) {
            System.out.println(" (Vacía)");
            return;
        }
        NodoCola actual = frente;
        while (actual != null) {
            // Se usa el toString() mejorado de Ticket
            System.out.println("  -> " + actual.getDato().toString()); // <-- toString() de Ticket
            actual = actual.getSiguiente();
        }
    }

    public NodoCola getFrente() {
        // --- NUEVO: Para iteración externa (Persistencia, Reportes) ---
        // Permite a PersistenciaService recorrer la cola sin modificarla.
        return frente;
    }

    public void limpiar() {
        // --- NUEVO: Para cargar datos ---
        // Resetea la cola antes de cargar nuevos datos desde persistencia.
        frente = null;
        fin = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }
}