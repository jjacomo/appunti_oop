package it.unibo.apice.oop.p22game.first;

/**
 * Elementi principali della soluzione (GUI reattiva con game loop thread-safe):
 * - MVC con M,V e C definiti da interfacce, e dove C chiama M e V, mentre V chiama C
 * - C include un game loop (GL thread) che processa eventi contenuti in una LinkedBlockingQueue
 * - questa queue è riempita mano a mano che arrivano chiamate da V a C (via EDT thread)
 * - a intervalli di tempo regolari, GL preleva un evento e lo gestisce chiamando M, poi aggiorna V
 * - M gestisce in modo completo una rappresentazione completamente virtuale della scena di gioco
 * - le chiamate a V sono gestite via SwingUtilities.invokeLater, così che siano gestite da EDT
 */

public class App {
    public static void main(String[] args) { 
        new ControllerImpl().loop();
    }
}
