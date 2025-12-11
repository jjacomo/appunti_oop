# Intro

Esempietto giochino mvc con game loop e programmazione concorrente.

La view riceve dati dal Controller (che parla col Model) e invia dati al Controller (per l'input dello user).
Parlando col Model il controller gli deve anche mandare l'aggiornamento del tempo passato.
Cosi' su computer a prestazioni diverse la navicella si muove veloce uguale (al massimo meno FPS)

LinkeBlockingQueue (da java.util.concurrent) e' una coda thread safe. Due thread possono scriverci insieme senza paura.
Ci metti dentro ad esempio tutti gli input dell'utente (gli eventi della view).

Se un thread aggiorna la view invocandone un metodo e non vuoi bloccare tutto, quel metodo della view deve usare SwingUtilities.invokeLater(() -> {...}).

Il model non ha thread, e' oop pura.
E' solo nel controller e nella view che gestisci i thread.

Il model lavora in un mondo virtuale con misure verosimili, senza curarsi della view.
Sara' dopo nella view che adatto le dimensioni usate dal model a una finestra del tuo schermo.
