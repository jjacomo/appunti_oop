# INTRO

Roba potente!
ı che carattere e'

Esempio di quanto puo' essere ganza
```java
public class UseStreamsOnPerson {
    public static void main ( String [] args ) {

        final List<Person> list = new ArrayList<>();
        list.add(new Person("Mario", "Cesena", 20000, "Teacher"));
        list.add(new Person("Rino", "Forlı'", 30000, "Football player"));
        list.add(new Person("Lino", "Cesena", 110000, "Chef", "Artist"));
        list.add(new Person("Ugo", "Cesena", 20000, "Secretary"));
        list.add(new Person("Marco", null, 4000, "Contractor"));

        // formattazione a singolo o doppio indent ...
        final double result = list.stream()
            .filter(p -> p.getCity().isPresent())
            .filter(p -> p.getCity().get().equals("Cesena"))
            .map(p -> p.getIncome())
            .reduce((a, b) -> a + b) // e' opportuno che sia una operazione associativa, non importa l'ordine con cui somma, alla fine somma tutto tra loro lo stesso (non per forza quello prima con quello dopo)
            .get() ; //la reduce returna un opzionale (lo stream potrebbe essere vuoto)

        System.out.println(result);

        // alternativa con iteratore : qual `e la pi `u leggibile ?
        double res2 = 0.0;
        for(final Person p : list) {
            if (p.getCity().isPresent() && p.getCity().get().equals("Cesena")){
                System.out.println(p);
                res2 = res2 + p.getIncome();
            }
        }
        System.out.println(res2);
    }
 }
```

Come funziona questa stregoneria?


La lista viene prima di tutto trasformata in uno `stream` con il metodo `.stream()` che hanno tutti gli Iterable.
I suoi elementi sono "Lazy" non vengono caricati subito ma quando servono. (finche' non chiami la reduce (uella che fa davvero la computazione hai costruito solo un `impalcatura`))

Poi chiamo la filter che crea un altro stream (collegato al precedente (in realta' e' un wrapper attorno))
E poi una su di questo altro stream chiamo un altro filter che ne crea un altro ancora.
Poi la map e ne crea un terzo.
Fino ad ora non ho fatto ancora niente di computazione...

A questo punto ho il mio ultimo stream prima di chiamare la reduce che non crea un altro stream ma returna un Optional.

Lazy perche'? ==>
La reduce lavora chiedendo allo stream creato da map il primo elemento, a sua volta map lo chiede allo stream piu' interno che lo chiede allo stream creato dalla prima filter che lo chiede alla lista stessa!!
A questo punto la lista da il suo primo elemento allo stream del primo filtro, questo stream `guarda se passa il filtro` (la persona ha la citta'?) e lo passa a quello dopo, anche lo stream dopo controlla se puo' passare (e' cesena?), fino ad ora stiamo ancora lavorando con stream di persone ma, adesso si arriva allo stream di map che converte da uno stream di persone a uno di Interi (l'income delle persone).
E cosi' poi vengono sommati e returnati in un Optional.


Ogni stream intermedio non e' una copia della lista, ma puoi concettualmente pensare come una lista di persone (e poi integer) che man mano passano ai filtri.

In pratica questo approccio e' un po' piu' lento della sua versione coi for brutale maaaa...
e' compatibile col `multithreading!!!!!` (in effetti con un for il multithreading non lo fai, oddio cioe' lo fai ma e' taaanto lavoro da fare)
Ogni thread gestisce un passo intermedio degli stream.

E cosi' semplicemente se scrivi

```java
final double result = list.parallelStream()
    .filter(p -> p.getCity().isPresent())
    .filter(p -> p.getCity().get().equals("Cesena"))
    .map(p -> p.getIncome())
    .reduce((a, b) -> a + b)
    .get();
```



Una volta che crei la pipeline e chiami la reduce tutta la struttura ha terminata il suo lavoro e lo stream si `chiude`. Consuma lo stream (ricorda il Consumer (funzioni da T a void))
Se vuoi rifare un operazione simile devi ricrearla da capo.

Collection -> stream -> filtri -> altri stream -> chiusura -> risultato

### Come si crea uno stream

* Le `collection` generano Stream (hanno tutte il metodo `.stream()` e `parallelStream`).
* `Stream.empty()` ne genera uno vuoto
* `Stream.of("a","b","c")` si capisce
* `Stream.iterate(0, i -> i+1)` crea uno stream INFINITO! (quando raggiungi 2^32 - 1 va a -2^32 e continua cosi' in loop). Alla fine uno stream e la versione funzionale degli iterator. In casi di stream infiniti hanno inventato apposta la `.limit(20)` per limitarlo (in questo caso ai primi 20 elementi). In realta' c'e' la versione di iterate con tre input per fare la stessa cosa: `.iterate(0, i -> i<20, i -> i + 1)` (assomiglia moltissimo a un for)
* `Stream.generate(() -> Math.random())` genera uno stream infinito di numeri random. Dentro a generate devi metterci un `Supplier`, una funzione (lamda) che ha input niente e returna della roba. Anche qui devi metterci un limit 
* `Stream.concat(Stream1, Stream2, ...)` dentro ci metti degli stream e lui te ne crea uno unico

### Trasformazioni di stream

* `.filter(predicato)`: mi crea uno stream con <= elementi (in numero)(passando solo chi passa il predicato)
* `.map(i -> "ciao: " + i)`: returna uno stream con lo stesso numero di elementi ma di cui cambia il tipo
* `.flatMap()`: e' un ibrido delle due qui sopra ma anche piu' forte: trasforma ogni elemento in altri stream di grandezze variabili (anche vuoti) ma poi appiattisce (flat) gli stream secondari (quindi trasforma ogni elemento in uno, nessuno o piu' elementi).
* `.distinct()`: e' praticamente un filtro che quando incontra di nuovo lo stesso elemento non lo passa allo stream dopo (filtra i doppioni) (poco performante quando ho stream gigaenormi)
* `.sorted((i, j) -> j - i)`: Ordina gli elementi secondo il criterio che gli hai passato. Lo stadio di sorted nella pipeline degli stream e' corposo perche' ha bisogno per forza di chiedere subito tutti gli elementi allo stream verso la sorgente (non e' Lazy) per metterli ordinati e passarli a quelo a valle.
* `.peek(i -> print(ciao))`: dentro ci metti del codice che vuoi eseguire per ogni elemento che e' passato fino a li. Non trasforma.
* `.skip()`:

#### chiusure

* `.reduce()`: c'e' anche la versione a tre elementi
* `.forEach()`
* `.count()`: conta gli elementi e poi chiudi
* `.anyMatch(predicate)`: da true se c'e' almeno un elemento che soddisfa il predicato
* `.findAny()`: non ho capito c'e' qualcosa che centra col multithreading


Esempi

```java
public class UseTransformations {
    public static void main(String[] args){
        final List<Integer> li = List.of(10, 20, 30, 5, 6, 7, 10, 20, 100);

        System.out.print("All \t\t:");
        li.stream()
            .forEach(i -> System.out.print(" " + i)); // ovviamente e' abbastanza inutile creare uno stream e chiuderlo subito

        System.out.print("\nFilter(>10)\t: ");
        li.stream()
            .filter(i -> i > 10) // fa passare solo certi elementi
            .forEach(i -> System.out.print(" " + i));

        System.out.print("\nMap(N : i + 1)\t: ");
        li.stream()
            .map(i -> "N: " + (i + 1)) // trasforma ogni elemento
            .forEach(i -> System.out.print(" " + i));

        System.out.print("\nflatMap(i, i + 1)\t: ");
        li.stream()
            .flatMap(i -> List.of(i, i + 1).stream()) // trasforma e appiattisce
            .map(String::valueOf) // invece del forEach ..
            .map(" "::concat)
            .forEach(System.out::print);
    }
 }
```

```java
public class UseTransformations2 {
    public static void main(String[] args){
        final List<Integer> li = List.of(10, 20, 30, 5, 6, 7, 10, 20, 100);

        System.out.print("\nDistinct\t: ");
        li.stream().distinct() // elimina le ripetizioni
            .forEach(i -> System.out.print(" " + i));

        System.out.print("\nSorted(down)\t: ");
        li.stream().sorted((i , j) -> j - i) // ordina
            .forEach(i -> System.out.print(" " + i));

        System.out.print("\nPeek(.)\t\t: ");
        li.stream().peek(i -> System.out.print(".")) // una azione ognuno
            .forEach(i -> System.out.print(" " + i));

        System.out.print("\nLimit(5)\t: ");
        li.stream().limit(5) // solo i primi 5
            .forEach(i -> System.out.print(" " + i));

        System.out.print("\nSkip(5)\t\t: ");
        li.stream().skip(5) // salta i primi 5
            .forEach(i -> System.out.print(" " + i));
    }
 }
```


## Stream di file

Posso creare uno stream di stringhe da un file (una stringa per riga) e poi ci puoi lavorare come per tutti gli altri stream.
```java
final Path filePath = FileSystems.getDefault().getPath(aFile);

System.out.println("Contenuto of " + aFile);
Files.lines(filePath).forEach(System.out::println);

System.out.println("Contenuto of " + aFile + "in altra codifica");
Files.lines(filePath, StandardCharsets.ISO_8859_1).forEach(System.out::println);
```




btw esistono anche stream specializzati per la gestione di tipi primitivi (IntStream, LongStream, ...) che permettono di aumentare le performance ma non ci interessano per questo corso.

btw esiste la classe bigInteger per fare numeri infinitamente grandi (fino a quando hai memoria nel computer)
