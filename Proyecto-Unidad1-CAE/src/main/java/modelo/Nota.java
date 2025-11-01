package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Nota {

    private int id;
    private String texto;
    private LocalDateTime fechaHora;

    public Nota (int id, String texto){
        this.id = id;
        this.texto = texto;
        this.fechaHora = LocalDateTime.now();
    }

    // --- NUEVO Constructor para Persistencia ---
    public Nota (int id, String texto, LocalDateTime fechaHora){
        this.id = id;
        this.texto = texto;
        this.fechaHora = fechaHora;
    }

    public int getId() { return id; }
    public String getTexto() { return texto; }
    public LocalDateTime getFechaHora() { return fechaHora; } // <-- NUEVO Getter

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Nota #%d | %s | \"%s\"", id, fechaHora.format(fmt), texto);
    }
}