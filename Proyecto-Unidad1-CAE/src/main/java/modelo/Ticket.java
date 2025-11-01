package modelo;

import estructuras.lista.ListaNotas;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {
    private static final AtomicInteger GENERADOR_ID = new AtomicInteger(1);
    private int id;
    private String nombreCliente;
    private Estado estado;
    private ListaNotas listaNotas;
    private AtomicInteger generadorIdNota = new AtomicInteger(1);

    private boolean esUrgente; // <-- NUEVO: Para saber a qué cola regresar

    // Constructor modificado
    public Ticket(String nombreCliente, Estado estadoInicial, boolean esUrgente) {
        this.id = GENERADOR_ID.getAndIncrement();
        this.nombreCliente = nombreCliente;
        this.estado = estadoInicial;
        this.esUrgente = esUrgente; // <-- NUEVO
        this.listaNotas = new ListaNotas();
    }

    // Constructor para persistencia (permite setear ID)
    public Ticket(int id, String nombreCliente, Estado estado, boolean esUrgente, int nextNotaId) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.estado = estado;
        this.esUrgente = esUrgente;
        this.listaNotas = new ListaNotas();
        this.generadorIdNota = new AtomicInteger(nextNotaId);

        // Actualizar el generador estático si este ID es mayor
        GENERADOR_ID.set(Math.max(GENERADOR_ID.get(), id + 1));
    }

    // --- Getters y Setters nuevos o modificados ---

    public boolean esUrgente() {
        return esUrgente;
    }

    public void setEsUrgente(boolean esUrgente) {
        this.esUrgente = esUrgente;
    }

    public ListaNotas getListaNotas() { return listaNotas; }
    public int getId() { return id; }
    public String getNombreCliente() { return nombreCliente; }
    public Estado getEstado() { return estado; }

    public void cambiarEstado(Estado nuevoEstado){
        this.estado = nuevoEstado;
    }

    // --- Métodos para Persistencia ---

    public static void setGeneradorId(int id) {
        // Método estático para reiniciar el generador de IDs al cargar datos
        GENERADOR_ID.set(id);
    }

    public static int getNextGeneradorId() {
        // Método para guardar el siguiente ID
        return GENERADOR_ID.get();
    }

    public int getNextNotaId() {
        // Método para guardar el siguiente ID de nota de *este* ticket
        return generadorIdNota.get();
    }

    // ... (métodos agregarNota, eliminarNota, toString sin cambios) ...
    public Nota agregarNota(String texto){
        int idNota = generadorIdNota.getAndIncrement();
        Nota nueva = new Nota(idNota, texto);
        listaNotas.insertarInicio(nueva);
        return nueva;
    }

    // Sobrecarga para persistencia
    public Nota agregarNota(int idNota, String texto, LocalDateTime fechaHora){
        Nota nueva = new Nota(idNota, texto, fechaHora);
        listaNotas.insertarInicio(nueva);
        return nueva;
    }

    public Nota eliminarNota(int idNota){
        return listaNotas.eliminar(idNota);
    }

    @Override
    public String toString() {
        return String.format("Ticket ID: %d | Cliente: %s | Estado: %s | Prioridad: %s",
                id, nombreCliente, estado, esUrgente ? "URGENTE" : "NORMAL");
    }
}