# INTRO

Abbiamo gia' visto la `System.out.println();`
Nella lezione della reflection abbiamo usato la `System.control.readln()` che aspetta l'input dall'utente (e' boccante).
Poi abbiamo visto nelle gui che cliccando si puo' dare input (e anche cliccando tasti (swing intercetta pure l'input da tastiera)).

Ma c'e' un'altra classe di problemi: la nostra applicazione puo' dover avere caratteristiche di `persistenza`: servono dei file dove salvare informazioni che possono essere riprese quando riavvio di nuovo l'applicazione.
Devo salvarli nella memoria secondaria (persistente).

Posso scrivere file in molti modi diversi (ci metto int? double? oggetti? o del testo?).


---

## File System

* Il file system e' un modulo del S.O. che gestisce la memoria secondaria
* Maschera le diversita' di dispositivi fisici (HD, CD, DVD, BR, SSD,. . . )
* Maschera le diversita' di contenuti informativi (testi, filmati, archivi,. . . )
* Fornisce meccanismi per fornire prestazioni, concorrenza, robustezza


Cosa ci da il sistema operativo?
Le dir sono dei file che tengono puntatori ad altri file (sono delle liste o un array di liste).
Le dir hanno metadati e poi i nomi dei file che contengono.
I file invece hanno i metadati e un contenuto vero e proprio.


### File

Package `java.io.File`.

Il path del file non ha sintassi platform-independent:
⇒ “C:\Users\mirko\prova.txt” vs “/home/mirko/Prova.txt”
Un po' una bega per le applicazioni cross-platform ma vedremo come fare.


```java
var f = new java.io.File(/home/jacomo/prova);
// f ==> /home/jacomo/prova
f.exists();
// ==> true
f.isDIrectory();
// ==> true
f.canWrite();
// ==> true
f.length();
// ==> 4096 (byte)
```

l'oggetto file e' solo l'oggetto che usiamo per riferirci al file in questione.

Molti di questi metodi che lavorano sul file possono lanciare una `IOException()` che e' `checked` e quindi va per forza gestita

```java
public class UseFile {

    public static final String SEP = File.separator;
    // public static final String FILE_NAME = "/home/mirko/aula/oop/Prova.bin"; // non portabile !!
    public static final String FILE_NAME = System.getProperty("user.home") + SEP + "aula" + SEP + "oop " + SEP + "Prova.bin";

    private static Iterable<Method> accessors(Class<?> c) throws Exception{
        final Collection<Method> list = new ArrayList<>();
        for(final Method m : c.getMethods()){
            if(m.getParameterTypes().length == 0 && m.getName().matches("has.*|is.*|get.*|can.*")){ // REGEX
                list.add(m);
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception{
        final File f = new File(args.length == 0 ? FILE_NAME : args[0]);
        for(final Method m : accessors(File.class)){
            System.out.println(m.getName() + " " + m.invoke(f));
        }
    }
 }
// REGEX: http://docs.oracle.com/javase/tutorial/essential/regex/
```


---

Come si opera sui file?

## InputStream OutputStream 

Un file lo apro in lettura O in scrittura.
Di solito quando lo apro in lettura lo leggo tutto.
Di solito quando lo apro in scrittura lo scrivo tutto.

* Stream = flusso (di dati)
* Di base, gestiscono flussi binari (di byte) leggibili vs. scrivibili
* Sono classi astratte (e non interfacce. . . )
* Possono essere specializzate da “sottoclassi” e “decorazioni”, tra cui
    ▶ Per diverse sorgenti e destinazioni di informazione, ad esempio su file (`FileInputStream`) o su memoria (`ByteArrayInputStream`)
    ▶ Per diversi formati di informazione, ad esempio valori primitivi (DataInputStream) o interi oggetti Java (ObjectInputStream)
    ⇒ . . . e corrispondenti versioni Output

```java
public abstract class InputStream implements Closeable{
    // Reads the next byte (0 to 255, -1 is end-of-stream)
    public abstract int read() throws IOException; // se stacco la chiavetta mi deve tirare un eccezione

    public int read(byte b[]) throws IOException{...}

    public int read(byte b[], int off, int len) throws IOException{...}

    public long skip(long n) throws IOException{...}

    public int available() throws IOException{...}

    public void close() throws IOException{...} // e' l'equivalente della free

    public synchronized void mark(int readlimit){...}

    public synchronized void reset() throws IOException{...}

    public boolean markSupported(){...}
 }
```

Con read e close fai il 99% delle operazioni che ti servono.
Il resto e' roba mezza inutile.
Ad esempio skip(100) ti salta i primi 100 byte dalla lettura.


Di questa classe abbiamo 2 implementazioni:
`FileInputStream` e `ByteArrayInputStream`

### Uso della classe ByteArrayInputStream

```java
import java.io.*;

public class UseByteArrayStream {
    public static void main(String[] args) throws IOException{
        final byte[] b = new byte[]{10, 20, -1, 40, -58};
        final InputStream in = new ByteArrayInputStream(b);
        int c;
        try{
            while((c = in.read()) != -1){ // C - style
                System.out.println(c);
            }
        } finally { // assicura la chiusura anche con eccezioni
            in.close();
        }
    }
 }
```

Questa e' la struttura da utilizzare per leggere file (try-finally).
Questa cosa e' cosi' comune che hanno creato il costrutto `try-with-resources`

```java
import java.io.*;

public class UseTryWithResources {
    public static void main(String[] args) throws IOException{
        final byte[] b = new byte[]{10, 20, 30, 40, 50};
        int c ;
        try(final InputStream in = new ByteArrayInputStream(b)){
            while((c = in.read()) != -1){ // C - style
                System.out.println(c);
            }
        }
    }
}
// il compilatore la traduce paro paro a quella di sopra
```


Buco di 20 minuti almeno (la ludo mi ha detto che hanno trovato il mio portafoglio)


## Codifica e Decodifica

Come faccio a leggere altro che non siano byte?


* `codificatore`: sistema che traduce dati strutturati verso un formato di memorizzazione
* `decodificatore`: sistema duale che ritraduce da un formato di memorizzazione verso un dato strutturato
⇒ definendo una opportuna accoppiata codifica/decodifica e' possibile pensare di scrivere strutture dati via via piu' complesse in uno stream di byte
⇒ termini frequenti: “serializzazione/deserializzazione

Decorator

Interfaccia DataInput

Perso




















