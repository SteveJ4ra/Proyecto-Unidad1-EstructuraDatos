package servicio.acciones;

import modelo.Accion;
import modelo.Ticket;
import servicio.GestorTickets;

public class AccionIniciarAtencion extends Accion {

    private GestorTickets gestor;
    private Ticket ticketAtendido;

    public AccionIniciarAtencion(GestorTickets gestor, Ticket ticketAtendido) {
        super("INICIAR_ATENCION", "Ticket ID " + ticketAtendido.getId());
        this.gestor = gestor;
        this.ticketAtendido = ticketAtendido;
    }

    @Override
    public void ejecutar() {
        // REHACER: Vuelve a poner el ticket en 'ticketEnAtencion'.
        // Esto es complejo: debe simular 'iniciarAtencion' solo para este ticket.
        // (Simplicación: Asumimos que al rehacer, las colas están como estaban).
    }

    @Override
    public void deshacer() {
        // DESHACER: Quita el ticket de 'ticketEnAtencion' y lo devuelve
        // al FRENTE de su cola original (para que sea el próximo en salir de nuevo).
        // Interactúa con: GestorTickets -> QuequeCAE.enquequeAlFrente()
    }
}
