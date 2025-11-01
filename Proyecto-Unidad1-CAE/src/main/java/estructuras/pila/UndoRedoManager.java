package estructuras.pila;

import modelo.Accion;

public class UndoRedoManager {
    private final PilaAcciones pilaUndo = new PilaAcciones();
    private final PilaAcciones pilaRedo = new PilaAcciones();

    public void registrarAccion(Accion accion) {
        if (accion == null) return;
        pilaUndo.push(accion);
        pilaRedo.limpiar();
    }

    // Deshace la última acción. Devuelve true si se pudo deshacer.
    public boolean deshacer() {
        if (pilaUndo == null || pilaUndo.estaVacia()) {
            System.err.println("No hay acciones para deshacer.");
            return false;
        }
        Accion accion = pilaUndo.pop();
        if (accion == null) {
            System.err.println("No hay acciones para deshacer.");
            return false;
        }

        accion.deshacer();
        pilaRedo.push(accion);

        // Mensaje detallado de lo que se deshizo
        System.out.println("Se deshizo: " + accion.getResumenDetallado());
        return true;
    }


    // Rehace la última acción deshecha. Devuelve true si se pudo rehacer.
    public boolean rehacer() {
        if (pilaRedo == null || pilaRedo.estaVacia()) {
            System.err.println("No hay acciones para rehacer.");
            return false;
        }

        Accion accion = pilaRedo.pop();
        if (accion == null) {
            System.err.println("No hay acciones para rehacer.");
            return false;
        }

        accion.ejecutar();
        pilaUndo.push(accion);

        // Mensaje detallado de lo que se rehizo
        System.out.println("Se rehizo: " + accion.getResumenDetallado());
        return true;
    }

    public int undoCount() { return pilaUndo.getSize(); }
    public int redoCount() { return pilaRedo.getSize(); }

    public void limpiarHistorial() {
        pilaUndo.limpiar();
        pilaRedo.limpiar();
    }
}
