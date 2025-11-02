package servicio.acciones;

import modelo.Accion;
import modelo.Estado;
import modelo.Ticket;
import servicio.GestorTickets;

// Acción para Iniciar Atención (Undo/Redo GLOBAL)
public class AccionIniciarAtencion extends Accion {

    private GestorTickets gestor;
    private Ticket ticketAtendido;
    private boolean eraUrgente; // Necesitamos saber de qué cola salió

    public AccionIniciarAtencion(GestorTickets gestor, Ticket ticketAtendido) {
        super("INICIAR_ATENCION", "Ticket ID " + ticketAtendido.getId());
        this.gestor = gestor;
        this.ticketAtendido = ticketAtendido;
        // Capturamos la urgencia ANTES de que cambie el estado
        this.eraUrgente = (ticketAtendido.getEstado() == Estado.URGENTE);
    }

    @Override
    public void ejecutar() {
        // REHACER: Vuelve a poner el ticket en 'ticketEnAtencion'

        // 1. Quitarlo de la cola (si el deshacer lo devolvió)
        Ticket t;
        if (eraUrgente) {
            t = gestor.getColaUrgente().eliminarPorId(ticketAtendido.getId());
        } else {
            t = gestor.getColaNormal().eliminarPorId(ticketAtendido.getId());
        }

        // 2. Ponerlo en atención
        gestor.setTicketEnAtencion(ticketAtendido);
        ticketAtendido.cambiarEstado(Estado.EN_ATENCION);
    }

    @Override
    public void deshacer() {
        // DESHACER: Devuelve el ticket a la cola de la que salió

        // 1. Quitar de 'enAtencion'
        gestor.setTicketEnAtencion(null);

        // 2. Devolverlo al FRENTE de su cola original
        if (eraUrgente) {
            ticketAtendido.cambiarEstado(Estado.URGENTE);
            gestor.getColaUrgente().enquequeAlFrente(ticketAtendido);
        } else {
            ticketAtendido.cambiarEstado(Estado.EN_COLA);
            gestor.getColaNormal().enquequeAlFrente(ticketAtendido);
        }
    }

    @Override
    public String getResumenDetallado() {
        return String.format("INICIAR ATENCIÓN: Ticket #%d (%s) movido a EN ATENCIÓN",
                ticketAtendido.getId(), eraUrgente ? "Urgente" : "Normal");
    }
}