package servicio.acciones;

import modelo.Accion;
import modelo.Ticket;
import servicio.GestorTickets;

// Acción para recibir un ticket
public class AccionRecibirTicket extends Accion {

    private GestorTickets gestor;
    private Ticket ticket;

    public AccionRecibirTicket(GestorTickets gestor, Ticket ticket) {
        super("RECIBIR_TICKET", "Ticket ID " + ticket.getId() + (ticket.esUrgente() ? " URGENTE" : " NORMAL"));
        this.gestor = gestor;
        this.ticket = ticket;
    }

    @Override
    public void ejecutar() {
        // REHACER: Vuelve a encolar el ticket en la cola correcta.
        // Interactúa con: GestorTickets -> QuequeCAE.enqueque()
    }

    @Override
    public void deshacer() {
        // DESHACER: Elimina el ticket (por ID) de la cola correcta.
        // Interactúa con: GestorTickets -> QuequeCAE.eliminarPorId()
    }
}