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

        gestor.setTicketEnAtencion(ticket);

        if(estadoOriginal == Estado.COMPLETADO) {
            gestor.getTicketEnAtencion().put(ticket.getId(), ticket);
        } else {
            if(ticket.esUrgente()) {
                gestor.getColaUrgente().enqueque(ticket);
            } else {
                gestor.getColaNormal().enqueque(ticket);
            }
        }
        // REHACER: Vuelve a finalizar el caso.
        // Saca el ticket de 'ticketEnAtencion' y lo mueve
        // a 'ticketsFinalizados' o lo re-encola, según su estado.
    }


    // DESHACER: Revierte la finalización.
    @Override
    public void deshacer() {

        // 1. Saca el ticket de 'ticketsFinalizados' O de la cola donde fue re-encolado.
        if(estadoOriginal == Estado.COMPLETADO) {
            gestor.getTicketsFinalizados().remove(ticket.getId());
        } else {
            Ticket t;
            if (ticket.esUrgente()) {
                t = gestor.getColaUrgente().eliminarPorId(ticket.getId());
            } else {
                t = gestor.getColaNormal().eliminarPorId(ticket.getId());
            }
        }
        // 2. Lo restaura en 'ticketEnAtencion'.
        gestor.setTicketEnAtencion(ticket);

        // 3. Restaura su 'estadoOriginal'.
        gestor.cambiarEstadoInterno(Estado.EN_ATENCION);
    }

    @Override
    public String getResumenDetallado() {
        return String.format("FINALIZAR/RE-ENCOLAR: Ticket #%d movido a %s",
                ticket.getId(), estadoOriginal);
    }
}