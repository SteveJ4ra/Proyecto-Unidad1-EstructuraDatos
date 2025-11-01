package consola;

import modelo.Estado;
import servicio.GestorTickets;
import servicio.PersistenciaService; // <-- NUEVO

import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaCAE {

    private static final GestorTickets gestor = new GestorTickets();
    private static final PersistenciaService persistencia = new PersistenciaService(); // <-- NUEVO
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ejecutarMenuPrincipal();
    }

    private static void ejecutarMenuPrincipal() {
        int opcion = -1;
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1: recibirNuevoCaso(); break;
                    case 2: gestor.listarCasosEnEspera(); break;
                    case 3: iniciarAtencion(); break;
                    case 4: gestorAtencion(); break;
                    case 5: gestionarConsultaHistorial(); break; // <-- Modificado
                    case 6: ejecutarReportes(); break; // <-- NUEVO
                    case 7: deshacerGlobal(); break; // <-- NUEVO
                    case 8: rehacerGlobal(); break; // <-- NUEVO
                    case 9: cargarDatosSistema(); break; // <-- NUEVO
                    case 0:
                        promptGuardarDatos(); // <-- Modificado (Request #8)
                        salir = true;
                        break;
                    default:
                        System.err.println("Opción no válida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine();
            }
        }
        scanner.close(); // Cerrar scanner al salir
        System.out.println("Sistema CAE finalizado. ¡Adiós!");
    }

    private static void mostrarMenu() {
        System.out.println("\n===== CENTRO DE ATENCIÓN AL ESTUDIANTE (CAE) =====");
        System.out.println("1. Recepción de Nuevo Caso (Encolar)");
        System.out.println("2. Consultar Casos en Espera");
        System.out.println("3. Iniciar Atención del Siguiente Caso (Prioridad Urgente)");
        System.out.println("4. Gestionar Caso EN ATENCIÓN");
        System.out.println("5. Consultar Historial de Casos Finalizados");
        System.out.println("--- Administración y Reportes ---");
        System.out.println("6. Ejecutar Reporte Top-K (por N° de Notas)");
        System.out.println("7. Deshacer Acción Global (Undo) (" + gestor.undoGlobalCount() + ")");
        System.out.println("8. Rehacer Acción Global (Redo) (" + gestor.redoGlobalCount() + ")");
        System.out.println("9. Cargar Datos desde Archivo");
        System.out.println("0. Salir (y Guardar Cambios)");
        System.out.print("Seleccione una opción: ");
    }

    private static void recibirNuevoCaso() {
        // Lógica para pedir nombre y prioridad (Request #1)
        // Pedir "Nombre/Trámite".
        // Pedir "Prioridad (1: Normal, 2: Urgente)".
        // Llamar a gestor.recibirNuevoCaso(nombre, esUrgente)
        // Manejar excepciones de entrada (Request #10).
    }

    private static void iniciarAtencion() {
        // Llama a gestor.iniciarAtencion()
        // Muestra mensajes de éxito o error (Request #9).
    }

    private static void gestionarConsultaHistorial() {
        // --- Modificado (Request #7) ---
        // 1. Validar si hay tickets finalizados (gestor.hayTicketsFinalizados()).
        // 2. Listar *primero* todos los finalizados: gestor.listarTicketsFinalizados().
        // 3. Pedir el ID a consultar.
        // 4. Llamar a gestor.consultarHistorial(id).
        // 5. Manejar InputMismatchException (Request #10).
    }

    private static void ejecutarReportes() {
        // --- NUEVO (Request #11 / PDF) ---
        // 1. Pedir al usuario el valor de 'K'.
        // 2. Llamar a gestor.ejecutarReporteTopK(k).
        // 3. Manejar InputMismatchException (Request #10).
    }

    private static void deshacerGlobal() {
        // --- NUEVO (Request #5) ---
        // Llama a gestor.deshacerAccionGlobal()
        // Muestra mensajes (Request #9).
    }

    private static void rehacerGlobal() {
        // --- NUEVO (Request #5) ---
        // Llama a gestor.rehacerAccionGlobal()
        // Muestra mensajes (Request #9).
    }

    private static void cargarDatosSistema() {
        // --- NUEVO (Request #8) ---
        // 1. Advertir al usuario que los cambios actuales se perderán.
        // 2. Pedir confirmación (S/N).
        // 3. Si confirma, llamar a persistencia.cargarDatos(gestor).
        // 4. Mostrar mensajes (Request #9).
    }

    private static void promptGuardarDatos() {
        // --- NUEVO (Request #8) ---
        // 1. Preguntar "¿Desea guardar los cambios realizados? (S/N)".
        // 2. Si responde 'S', llamar a persistencia.guardarDatos(gestor).
        // 3. Mostrar mensajes (Request #9).
    }

    // --- Sub-Menú para Caso en Atención (Modificado) ---

    private static void gestorAtencion() {
        // ... (validación de ticketEnAtencion sin cambios) ...

        while (true) { // 'volver' se maneja con 'return'
            // Mostrar estado del ticket y contadores de Undo/Redo *del ticket*
            // System.out.println("4. Deshacer (Ticket) (" + gestor.undoTicketCount() + ")");
            // System.out.println("5. Rehacer (Ticket) (" + gestor.redoTicketCount() + ")");

            // ... (switch case) ...
            // case 4: gestor.deshacerAccionTicket(); break;
            // case 5: gestor.rehacerAccionTicket(); break;
            // case 6:
            //    if (finalizarCasoEnAtencion()) return; // Sale del submenú
            //    break;
            // case 0: return; // Sale del submenú
            // ... (default y catch) ...
        }
    }

    private static void cambiarEstadoEnAtencion() {
        // --- Modificado (Request #4) ---
        // Muestra los estados finales/internos:
        // 1. EN_PROCESO
        // 2. PENDIENTE_DOCS
        // 3. COMPLETADO
        // Llama a gestor.cambiarEstadoInterno(estadoSeleccionado)
        // Valida la transición (Request #9).
    }

    private static boolean finalizarCasoEnAtencion() {
        // --- Modificado (Request #4) ---
        // Ya no pide estado. Llama a gestor.finalizarCaso()
        // El gestor decidirá si re-encolar o finalizar según el estado actual.
        // Devuelve 'true' si el caso se finalizó/cerró, 'false' si hubo error
        // (p.ej. estado EN_ATENCION).
        return false;
    }

    // ... (registrarNotaEnAtencion y eliminarNotaEnAtencion sin cambios lógicos) ...
}