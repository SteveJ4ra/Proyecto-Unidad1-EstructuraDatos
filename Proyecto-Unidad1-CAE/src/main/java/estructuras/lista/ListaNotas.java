package estructuras.lista;

import modelo.Nota;

public class ListaNotas {

    private NodoNota cabeza;



    // ... (constructor, estaVacia, insertarInicio, eliminar, mostrar sin cambios) ...

    public int getTamanio() {
        // --- NUEVO: Para Reporte Top-K [cite: 49] ---
        // Cuenta el número de nodos en la lista.
        // Interactúa con: GestorTickets.ejecutarReporteTopK()
        int contador = 0;
        NodoNota actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public NodoNota getCabeza() {
        // --- NUEVO: Para Persistencia ---
        // Permite a PersistenciaService recorrer la lista de notas.
        return cabeza;
    }

    // ... (el resto de la clase: insertarInicio, eliminar, mostrar) ...
    public ListaNotas() {
        this.cabeza = null;
    }

    public boolean estaVacia(){
        return cabeza == null;
    }

    public void insertarInicio(Nota n){
        NodoNota nuevo = new NodoNota(n);
        nuevo.setSiguiente(cabeza);
        cabeza = nuevo;
    }

    public Nota eliminar(int id){
        if (estaVacia()) return null;

        if(cabeza.getDato().getId() == id){
            Nota notaEliminada = cabeza.getDato();
            cabeza = cabeza.getSiguiente();
            return notaEliminada;
        }

        NodoNota actual = cabeza;
        while(actual.getSiguiente()!=null && actual.getSiguiente().getDato().getId()!=id){
            actual=actual.getSiguiente();
        }

        if(actual.getSiguiente() == null) return null;

        Nota notaEliminada = actual.getSiguiente().getDato();
        actual.setSiguiente(actual.getSiguiente().getSiguiente());
        return notaEliminada;
    }

    public void mostrar(){
        if(estaVacia()){
            System.out.println("  (No hay notas registradas)");
            return;
        }
        NodoNota aux = cabeza;
        while(aux!=null){
            System.out.println("    " + aux.getDato());
            aux = aux.getSiguiente();
        }
    }
}