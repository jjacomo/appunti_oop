# INTRO

A cosa serve la programmazione concorrente

L'idea e' vedere i thread come modo per progettare applicazioni (entita') attive, non pensare agli oggetti che ricevono input e danno un output (passivo).

Non serve solo a aumentare le performance.

Ad esempio come fanno a funzionare le gui del tuo programma java se e' tutto in un singolo thread e quando tocchi qualcosa fa qualcosa.
Beh spoiler: in realta' sono multithread.

Thread = Filo = flusso di controllo che esegue operazioni sequenziali

## Esecuzione Concorrente != Parallela
La concorrente (puo' eseguire) esegue tutta nello stesso processore uno alla volta.
La parallela esegue in due processori logici contemporaneamente.

E' lo scheduler che decide che thread runna in quel momento.

## i Thread condividono la memoria

ma non lo `stack` (li ci stanno record di attivazione, punto di ritorno delle variabili, altre variabili...)


## Due tipi di thread

User o kernel.
Quando di base crei un thread in java ne crei uno kernel (ossia che viene gestito tutto dal sistema operativo)
Con java 21 hanno introdotto i virtual thread (user thread).

I thread sono pesanti (non come i processi ma comunque) e per questo hanno introdotto gli user thread che sono praticamente implementati nella jvm.


## Benefici

* reattività (responsiveness)
• la possibilità di creare thread dedicati all'interazione con l'utente,
concorrentemente all'esecuzione di operazioni a lungo termine, è
fondamentale per creare interfacce utente (UI) adeguate
* condivisione di risorse (resource sharing)
• thread di uno stesso processo condividono dati e codice, che non
devono quindi essere replicati
* performance (economy)
• la creazione e lo switch di contesto a livello di thread richiede molte
meno risorse temporali e spaziali che non la creazione e switch fra
processi.
* sfruttamento di architetture multiprocessore/multicore
• i benefici dell'uso di thread multipli si sentono ancor più su
architetture multiprocessore, dove thread distinti possono essere
messi in esecuzione concorrente su CPU diverse, aumentando
notevolmente la concorrenza. E poi e' scalabile, se ho cpu con 
numeri diversi di core li uso tutti lo stesso.

## terminologia
* Programmazione asincrona (asynchronous programming)
– esecuzione di computazioni in modo asincrono, non bloccante
* Programmazione distribuita (distributed programming)
– quando i processori sono distribuiti in rete e non c'è condivisione
fisica della memoria
* Programmazione multithread (non e' l'unico approcio per fare multithread)
– quando le parti in esecuzione concorrente sono rappresentate da
thread


## come fare in java

C implementa i thread con la libreria pthread.
GO e' nativamente concorrente (performa come il C) con le go-routines.

JAVA fa un po' a meta', resta object oriented (i thread sono oggetti) e con una libreria.
L'oggetto thread avra' il metodo start e poi avra' anche il codice che deve eseguire.
Il codice che deve eseguire questo thread sta dentro il suo metodo run.

```java
public class MyThread extends Thread {
    public void run(){
        // <corpo del thread>
        // ...
    }
```

Il metodo start fa partire il metodo run ma non semplicemente chiamandolo... (beh ovvio).

### Es

```java
public class Clock extends Thread {
    private int step;
    public Clock(int step){
        this.step=step;
    }
    public void run(){
        while (true) { // ti sembra strano? beh non lo e'
                       // molti sistemi devono rimanere attivi nel TEMPO
            System.out.println(new Date());
            try {
                Thread.sleep(step);
            } catch (Exception ex){
            }
        }
    }
}
```

```java
package oop.concur;
import java.io.*;
public class TestClock {
    static public void main(String[] args) throws Exception {
        Clock clock = new Clock(1000);
        clock.start();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));
        String input = null;
        do {
            input = reader.readLine();
            System.out.println("eco: " + input);
        } while(!input.equals("exit"));
        System.exit(0);
    }
}
```

In questo esempio il main (mainthread) e il thread clock che fanno concorrenza tra di loro.

Il metodo run ovviamente non lo chiami mai tu esplicitamente. Ma allora perche' e' public?


### altri metodi di Thread
`sleep(long ms)`: in millisecondi
`Thread(String name)`: costruttore col nome
Hanno rimosso metodi come `stop()` perche' non si dovrebbe poter fermare un thread da fuori quel thread

## altro modo di implementare thread

Le `classi che implementano runnable`

```java
public class MyRun implements Runnable {
    public void run(){
        // ...
    }
}
// ...
Thread th = new Thread(new MyRun());
// Thread th = new Thread(new MyStrategy()); pattern strategy
// con lambda (Java vers. >= 1.8)
Thread th1 = new Thread(() -> { ... });
```


## tool per monitorare thread

`JConsole`: e' gia' nel JDK che ti fa vedere tipo task manager ma per i thread della tua applicazione java.
`VisualVM`: lo vediamo la prossima lezione. E' ancora piu' carino, ti permette anche di fare il profiling.



### altro esempio CONCURRENT SORTING

Un componente attivo che ordina le due meta' e un altro che le mergia
Il master suddivide il lavoro di due worker (thread) di ordinare la lista.
Il master deve aspettare che abbiano finito (chiamando il metodo `join` di entrambi i thread) e poi fare il merge.

Come fare a sfruttare piu' di 2 processori?
Beh c'e' un metodo `AvailableProcessors()` che ti dice quanti processori (logici) ha la tua macchina.
Cosi' il vettore invece che dividerlo in sole due parti lo divido col numero di processori che ho.
Perche' ovviamente li voglio sfruttare tutti!!!


### altro esempio FALLING WORDS

molto carino le parole cadono come matrix.
Con un singolo thread sarebbe un po' incasinato ma coi thread e' molto piu' semplice.
Una parola, un thread. Ognuno con la sua velocita'.

## Terminazione ti un thread

Il metodo stop cme gia' detto non funziona piu'.
Si fa con un flag `stopped` cosi' appena finisce quella istanza del metodo run si stoppa.
Run in questo caso e' una sezione critica.

```java
public class StoppableClock extends Thread {
    private int step;
    private volatile boolean stopped; // volatile e' per dire al compilatore di andare a 
                                      // controllare in memoria e non fare ottimizzazioni 
                                      // (potrebbe pensare che non sia modificabile perche' non lo chiama nessuno)
    public Clock(int step){
        this.step = step;
        this.stopped = false;
    }
    public void run(){
        while (!stopped) {
            System.out.println(new Date());
            try {
                sleep(step);
            } catch (Exception ex){
                System.out.println("Interrupted!");
            }
        }
    }
    public void forceStop(){ // metodo pubblico con cui cambio il flag
        stopped = true;
        interrupt(); // serve a svegliare un thread in caso sia bloccato (e' in sleep)
                     // e genera un eccezione
    }
}
```


### Esempio bouncing balls

FICO
Se invece dei thread facessi tutto inn un main loop la cpu andrebbe tutto in palla con solo 12 palle.
Invece coi thread ne ho generati 650 e avevo la cpu solo al 20%


#### I processori sono potenti


## GUI EVENT LOOP

Quando crei una finestra crei anche un eventDispatcherThread che si occupa di gestire eventi.
Se clicchi un JButton viene generato un evento che viene accodato.
Se in quell'evento c'e' associato un Listener allora quando passa sotto il vaglio dell'eventDispatcherThread che esegue il codice del listener.
E' oppurtuno non mettere codice molto pesante dentro un eventListener altrimenti si blocca tutto il Thread dei listener e se clicchi un'altro bottone ad esempio non fa nulla, e' freezato.
C'e' l'esempio carino nella repo.

Se mentre aspetti che il bottone ridiventi cliccabile lo clicchi ancora gli eventi vengono tutti accodati. 
Quindi quando finisce di elaborare il primo click parte subito il secondo e cosi' via.
Quindi e' tutto un po' in ritardo e laggoso.

Se per caso i metodi di ascolto devono eseguire codice gravoso e non vuoi bloccare tutto puoi delegare i compiti al controller che poi fa partire dei thread.



