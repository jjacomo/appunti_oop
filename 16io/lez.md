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

Con `read` e `close` fai il 99% delle operazioni che ti servono.
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


---

## Pattern Decorator

Pattern Decorator (i pattern sono tipo iterator) applicato a output/inputStream;
Questo implementa l'interfaccia Output/InputStream ma a sua volta si compone di altri Output/InputStream.
Un esempio di pattern decorator e' il `DataInputStream` o (`DataOutputStream`);
Assieme ai soliti metodi di Output/InputStream ha anche i metodi `WriteInt()` e `WriteUTF()`.
C'e' anche il `BufferedInputStream`: se faccio 4 read diverse con DataInputStream faccio 4 read separate (piu' dispendiose anche se pensi all'internet): quindi si e' pensato a BufferedInputStream per 


```java
import java.io.*;

public class UseDataStream {
    public static void main(String[] args) throws IOException{
        try ( //try with resources
        final OutputStream file = new FileOutputStream(UseFile.FILE_NAME);
        final DataOutputStream dstream = new DataOutputStream(file); //Decorazione!
    ) {
            dstream.writeBoolean(true);
            dstream.writeInt(10000);
            dstream.writeUTF("Ciao");
            dstream.writeDouble(5.2);
        }
        try (
        final InputStream file2 = new FileInputStream(UseFile.FILE_NAME);
        final DataInputStream dstream2 = new DataInputStream(file2);
    ) {
            System.out.println(dstream2.readBoolean()); // Do not change order
            System.out.println(dstream2.readInt());
            System.out.println(dstream2.readUTF());
            System.out.println(dstream2.readDouble());
        }
    }
 }
```


DataOutputStream ==>   BufferedOutputStream (per performance (di solito non lo usiamo)) => FileOutputStream
    ^                          ^                                                                 ^
    |                          |                                                                 |
chiama i metodi             chiama i metodi di                                      Lavora finalmente sul file
di BufferedInputStream          FileOutputStream                                        vero e proprio.


Si puo' creare una `pipeline` di Decorators diversi aggiungendo nuovi stadi in mezzo tra DataOutputStream e FileOutputStream (tipo CheckedOutputStream (aggiunge checksum), CipherOutputStream, ...)


---

## Java Serialization

Salvare Oggetti su file
Ma si puo' automaticamente salvare ad esempio un Person("Mario") e poi leggerlo da file proprio come tipo Persona (Problema di serializzazione/deserializzazione degli oggetti (e' il termine riferito agli oggetti per decodifica e codifica)).
Oppure si puo' fare una write di una list di Students?  e poi leggerla e averla gia' pronta?
Sarebbe bellissimo... e si puo': Serialization.


Si fa con `ObjectInputStream` che ha un metodo aggiuntivo `readObject()` (ObjectOutputStream ha il metodo writeObject())

Serializzare un oggetto vuol dire stenderlo finche' tutti i suoi dati non stanno in 'fila' per metterlo in uno stream (SEQUENZA di 0 e 1).

Quando crei una classe Person affinche' sia serializzabile devi dichiararla cosi'
```java
public class Person implements Serializable{}
```

`Serializable` e' un'interfaccia `tag`, non ha dichiara nessun metodo, e' solo un modo per dire che una classe e' serializzabile.

La serialization usa la reflection per ricostruire gli oggetti (non il costruttore).

Se un oggetto e' dichiarato serilizzabile allora affinche' funzioni, anche tutti i suoi oggetti di cui si compone si deve essere serializable.
Se invece avessi un oggetto che si compone di un altro oggetto non serializable ma la voglio salvare lo stesso (ad esempio l'oggetto player si compone di un immagine che' e' gia' salvata in memoria e quindi non voglio salvarla ancora (anche perche' pesa molto)) posso dichiararlo transient.
```java
public class Person{
    public String name;
    public transient Object oggettoacasoperlesempio;
}
```


---

## Caching di toString()

Una volta che hai fatto una volta il toString() di un oggetto non dovresti avere bisogno di ricalcolarlo ogni volta che lo richiami, la concatenazione di Stringhe puo' essere molto onerosa!!

```java
public class CPerson implements java.io.Serializable {
    // Eclipse would ask to implement a serialVersionUID
    // private static final long serialVersionUID = -8985026380526620812 L ;
    private String name;
    private int birthYear;
    private boolean married;
    transient private String cachedToString = null;

    public CPerson(String name, int birthYear, boolean married) {
        this.name = name;
        this.birthYear = birthYear;
        this.married = married;
    }

    public String getName() {
        return this.name;
    }

    public int getBirthYear() {
        return this.birthYear;
    }

    public boolean isMarried() {
        return this.married;
    }
    private String computeToString() {
        return this.name + ":" + this.birthYear + ":" + (this.married ? "spos" : "non-spos");
    }

    public String toString() {
        if (this.cachedToString == null){
            System.err.println("Log: The cache is empty...");
            this.cachedToString = this.computeToString();
        }
        return this.cachedToString;
    }
}
```


## Serializzazione ad-hoc

Volendo puoi ridefinire il protocollo di serializzazione del tuo oggetto.
Non si usa spesso ma volendo si puo'. Vedi codice delle slide (p52) se ti interessa.


### problemino

Se fai una nuova versione della tua applicazione e cambi un oggetto che prima era serilizable e adesso provi a ricaricare un file che era stato salvato in una versione precedente del software. Vedi tutto rotto.
Fortunatamente esiste il `serialVersionUID`;


## Problema piu' grosso:

Pianini odia java Serialization.
Perche' java Serialization e' un formato chiuso, solo lui sa come decifrarlo col codice java. NO OPEN SOURCE.
E poi altri linguaggi diversi non possono leggere questi file e quindi in applicazioni grosse che sfruttano diversi linguaggi che non capiscono la java Serialization e' un problema.

Esiste una libreria che usano tutti i linguaggi per serializzare in modo quasi universale: `Protocol Buffer`.

Oppure creo un file di testo. (Pensa al JSON ad esempio, TOML, YAML, XML, CSV)
Che ha anche il vantaggio di essere modificabile in un editor testuale.


## File di Testo

Si usano altre classi, vedi esempi slide 67-70.
Bega delle codifiche diverse.

