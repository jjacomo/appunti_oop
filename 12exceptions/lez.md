# INTRO

1. Errori a run-time e necessita' di una loro gestione;
2. Tipi di eccezioni/errori in Java;
3. Istruzione throw;
4. Costrutto try-catch-finally;
5. Dichiarazioni throws;

### Errori compile time

* sono quelli piu' grossolani, sono intercettati dal compilatore;
* quindi rientrano nella fase dell’implementazione, sono innocui;
* un linguaggio con strong typing consente di identificarne molti a compile-time;
* editor moderni spesso li rendono “edit-time”;
* non sono considerabili “bug”, ma situazioni temporanee nello sviluppo.

### Errori a tempo di esecuzione (run-time)

* sono condizioni anomale dovute alla dinamica del sistema;
    ▶ parametri anomali a funzioni, errori nell’uso delle risorse di sistema,..
* in genere `e possibile:
    (i) identificare/descrivere dove potrebbero accadere, 
    (ii) intercettarli e 
    (iii) gestirli prevedendo procedure di compensazione (rimedio al problema che le ha causate);
* alcuni linguaggi (come Java/C#, non il C) forniscono costrutti di alto livello per agevolarne la gestione
* nella programmazione moderna li si cerca di associare a bug

---

* Gli errori a run-time in Java sono rappresentati da oggetti della classe `java.lang.Throwable`, vengono “lanciati”:
    1. da esplicite istruzioni del tipo: `throw <exception-object>`;
    2. o, direttamente dalla JVM per cause legate al “sistema operativo”
* Tali oggetti portano informazioni utili a capire la causa dell’errore
* Si puo' dichiarare se un metodo potra' lanciare una eccezione:
```java
metodo() throws classeDiUnEccezione{..}
```

* Si pu`o intercettare una eccezione e porvi rimedio:
```java
try{ <instructions> } catch(classeDiUnEccezione <var>){...}
```


---


## Tipi di errori


1. Errori: `java.lang.Error` e sottoclassi
    * Dovute a un problema “serio” (e non risolvibile) interno alla JVM
    * Di norma una applicazione non si deve preoccupare di intercettarli (non ci sarebbe molto di piu' da fare che interrompere l’esecuzione)
    * (tipo out of memory, stack overflow, ...)
=> niente da fare

2. Eccezioni unchecked: `java.lang.RuntimeException` e sottoclassi
    * Causate da un bug nella programmazione
    * Di norma una applicazione non si deve preoccupare di intercettarli (dovrebbero essere risolti tutti in fase di debugging del sistema)
    * (tipo null pointer, class cast exception, ...)
    * Interrompono l'applicazione
    * stampano su stderr (System.err) un messaggio che include lo StackTrace
=> `throw`

3. Eccezioni checked: i `java.lang.Throwable` tranne le precedenti
    * Causate da un problema prevedibile ma non rimediabile a priori
    * Le applicazione `devono` dichiarli esplicitamente, e vanno intercettati e gestiti esplicitamente
    * (tipo IO exception, Interrupted exception, ...)
=> `throws`, `try-catch`

```java
try { <body-maybe-throwing-an-exception>}
catch (<throwable-class> <var>) { <handler-body>}
catch (<throwable-class> <var>) { <handler-body>}
...
catch (<throwable-class> <var>) { <handler-body>}
finally { <completion-body>} // clausola finale opzionale
```


### Eccezioni comuni da usare
* `NullPointerException`: quando viene passato un riferimento null inatteso
* `IllegalArgumentException`: quando un input non ha le caratteristiche richieste
* `IndexOutOfBoundsExecption`: quando un indice/posizione e' fuori dai limiti imposti
* `IllegalStateException`: quando la chiamata di metodo e' fatta al momento sbagliato
* `OperationNotSupportedException`: quando la classe non supporta questa funzionalita'


---

## Creare una nuova classe di eccezioni

* Un sistema potrebbe richiedere nuovi tipi di eccezioni, che rappresentano eventi specifici collegati al dominio applicativo
    ▶ Persona gia' presente (in un archivio cittadini)
    ▶ Lampadina esaurita (in una applicazione domotica)
* Semplicemente si fa una estensione di Exception o RuntimeException
    ▶ a seconda che la si voglia checked o unchecked
    ▶ per il momento stiamo considerando solo le unchecked
* Non vi sono particolari metodi da ridefinire di solito.
Solo ricordarsi di chiamare correttamente il costruttore del padre
* Se si vuole incorporare una descrizione articolata della causa dell’eccezione, la si puo' inserire nei campi dell’oggetto tramite il costruttore o metodi setter..


```java
public class MyException extends RuntimeException {
    // tengo traccia degli argomenti che hanno causato il problema
    private final String[] args;

    public MyException(final String s, final String[] args){
        super(s);
        this.args = args;
    }

    // modifico la toString per evidenziare this.args
    public String toString(){
        String str = "Stato argomenti: ";
        str = str + java.util.Arrays.toString(args);
        str = str + "\n" + super.toString();
        return str;
    }
 }
```

```java
public class UseMyException {
    public static void main(String[] s){
        try { // attenzione alla formattazione di questo esempio !
            final int a = Integer.parseInt(s[0]);
            final int b = Integer.parseInt(s[1]);
            final RangeIterator r = new RangeIterator(a, b);
            System.out.print(r.next() + " ");
            System.out.print(r.next() + " ");
            System.out.println(r.next());
        } catch (Exception e){
            final String str = "Rilancio di: " + e;
            RuntimeException e2 = new MyException(str, s);
            throw e2;
        }
    }
 }
 /* Esempio : java UseMyException 10 13.1
Exception in thread " main " Stato argomenti : [10 , 13.1]
it . unibo . apice . oop . p13exceptions . classes . MyException : Rilancio di : java . lang
. NumberFo rma tE xce pt ion : For input string : "13.1"
at it . unibo . apice . oop . p13exceptions . classes . UseMyException . main (
UseMyException . java :14)
 */
```


---

## Checked

Quelle viste finora, dovute ad un bug di programmazione.
Quindi sono da catturare opzionalmente, perche' rimediabili.
⇒ ..le linee guida piu' moderne le sconsigliano

Checked: `Exception` o sottoclassi ma non di RuntimeException.

Rappresentano errori non riconducibili ad una scorretta programmazione, ma ad eventi abbastanza comuni anche nel sistema una volta installato e funzionante.
    ▶ Funzionamento non normale, ma non tale da interrompere l’applicazione (p.e., l’utente fornisce un input errato inavvertitamente)
    ▶ Un problema con l’interazione col S.O. (p.e., file inesistente)

I metodi che le lanciano lo devono dichiarare esplicitamente (`throws`)
Chi chiama tali metodi deve obbligatoriamente gestirle
    ▶ o catturandole con un try-catch
    ▶ o rilanciandole al chiamante con la throws


```java
import java.io.*;

public class IOFromKeyboard {
    private static final BufferedReader KBD = new BufferedReader(new InputStreamReader (System.in));

    private static int getIntFromKbd() throws IOException{
        return Integer.parseInt(KBD.readLine());
    }

    public static void main(String[] args){
        try {
            System.out.print("Inserisci un numero: ");
            final int a = getIntFromKbd();
            System.out.println("Hai inserito il num: " + a);
        } catch (IOException e){
            System.out.println("Errore di I/O: " + e);
        } catch (NumberFormatException e){
            System.out.println(e);
        }
    }
 }
```











