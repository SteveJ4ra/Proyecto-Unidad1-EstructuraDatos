package servicio.acciones;

import modelo.Accion;
import modelo.Ticket;
import modelo.Estado;
import servicio.GestorTickets;

public class AccionFinalizarCaso extends Accion {

    private GestorTickets gestor;
    private Ticket ticket;
    private Estado estadoOriginal; // El estado ANTES de finalizar (PENDIENTE, COMPLETADO, etc.)

    public AccionFinalizarCaso(GestorTickets gestor, Ticket ticket, Estado estadoOriginal) {
        super("FINALIZAR_CASO", "Ticket ID " + ticket.getId() + " a " + ticket.getEstado());
        this.gestor = gestor;
        this.ticket = ticket;
        this.estadoOriginal = estadoOriginal; // El estado que tenía al *momento* de finalizar
    }

    @Override
    public void ejecutar() {

        gestor.setTicket
        // REHACER: Vuelve a finalizar el caso.
        // Saca el ticket de 'ticketEnAtencion' y lo mueve
        // a 'ticketsFinalizados' o lo re-encola, según su estado.
    }

    @Override
    public void deshacer() {
        // DESHACER: Revierte la finalización.
        // 1. Saca el ticket de 'ticketsFinalizados' O de la cola donde fue re-encolado.
        // 2. Lo restaura en 'ticketEnAtencion'.
        // 3. Restaura su 'estadoOriginal'.
    }
}