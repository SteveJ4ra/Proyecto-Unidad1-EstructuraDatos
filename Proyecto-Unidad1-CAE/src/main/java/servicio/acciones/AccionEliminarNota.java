package servicio.acciones;

import estructuras.lista.ListaNotas;
import modelo.Accion;
import modelo.Nota;

public class AccionEliminarNota extends Accion {

    private final ListaNotas listaNotas;
    private final Nota notaEliminada; // Guardamos la Nota que se eliminó

    public AccionEliminarNota(ListaNotas listaNotas, Nota notaEliminada) {
        super("ELIMINAR_NOTA", "Nota ID " + notaEliminada.getId() + " eliminada.");
        this.listaNotas = listaNotas;
        this.notaEliminada = notaEliminada;
    }

    // Ejecutar (para Redo): Re-elimina la nota (si se encuentra, ya que fue deshecha)
    // Para simplificar, Redo revierte la acción de Deshacer.
    @Override
    public void ejecutar() {listaNotas.eliminar(notaEliminada.getId()); // Re-eliminar la nota
    }

    // Deshacer (para Undo): Re-inserta la nota eliminada.
    @Override
    public void deshacer() {
        // La re-inserción al inicio es la forma más sencilla de revertir la eliminación en una SLL.
        listaNotas.insertarInicio(notaEliminada);
    }
    @Override
    public String getResumenDetallado() {
        // notaEliminada está guardada
        // intentar averiguar si ListaNotas corresponde a un Ticket no es directo,
        // por eso devolvemos la info disponible (ID de nota y texto).
        return String.format("ELIMINAR_NOTA: Nota ID %d: \"%s\"", notaEliminada.getId(), notaEliminada.getTexto());
    }

}