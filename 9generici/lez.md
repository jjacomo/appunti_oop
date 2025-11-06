# INTRO

Forme di riuso nella oop:
* `Composizione`: Un oggetto e' ottenuto per composizione di oggetti di altre classi;

* `Estensione`: Una nuova classe e' ottenuta riusando il codice di una classe pre-esistente;

* `Polimorfismo inclusivo (subtyping)`: Una funzionalita' realizzata per lavorare su valori/oggetti del tipo A, puo' lavorare con qualunque valore/oggetto del sottotipo B (p.e., se B estende la classe A, o se B implementa l’interfaccia A);

* `Polimorfismo parametrico (Java/C# generics, C++ templates,..)`: Una funzionalita' (classe o metodo) generica e' costruita in modo tale da lavorare uniformemente su valori/oggetti _indipendentemente dal loro tipo_: tale tipo diventa quindi una sorta di parametro addizionale;


### Voglio implementare IntVector

ok lo faccio ma adesso ne vorrei uno anche per i boolean, e per i float e per i... OK COSI' NON VA.
Sarebbe bello poter implementare un vector generico `XVector` che li comprende tutti...

Si potrebbe implementare facendo un ObjectVector (ossia un vettore di Object) e poi fare dei cast di int nel caso di IntVector e cosi' via...
```java
public class UseObjectList {
    public static void main ( String [] s ) {
        final ObjectList list = new ObjectList(10, new ObjectList(20, new ObjectList(30, new ObjectList(40, null))));
        // Cast necessari , eccezioni possibili
        final int first = (Integer) list.getHead(); // Unboxing
        final int second = (Integer) list.getTail().getHead();
        final int third = (Integer) list.getTail().getTail().getHead();
        System.out.println(first + " " + second + " " + third);
        System.out.println(list.toString());
        System.out.println(list.getLength());

        // Usabile anche con le stringhe
        final ObjectList list2 = new ObjectList("a", new ObjectList("b", new ObjectList("c", new ObjectList("d", null))));
        System.out.println(list2.toString());
    }
}
```

E questa era la soluzione adottata fino a java 5. Ma poi veniva fuori che era pieno di classi definite come ObjectVector o ObjectList...
E non si capiva piu' niente di che cosa contenessere effettivamente.
=>

## Generici

Dato un frammento di codice F che lavora su un certo tipo, diciamo String, se potrebbe anche lavorare in modo uniforme con altri. . .
. . . lo si rende parametrico, sostituendo a String una sorta di variabile X (chiamata `type-variable`, ossia una variabile che contiene un tipo)
A questo punto, quando serve il frammento di codice istanziato sulle stringhe, si usa `F<String>`, ossia si richiede che X diventi String
Quando serve il frammento di codice istanziato sugli integer, si usa `F<Integer>`

#### Esempio

```java
/* Classe generica in X :
 - X `e il tipo degli elementi della lista */
public class List<X>{
    private final X head; // Testa della lista, tipo X
    private final List<X> tail; // Coda della lista, tipo List<X>

    public List(final X head, final List <X> tail){
        this.head = head;
        this.tail = tail;
    }

    public X getHead(){
        return this.head;
    }

    public List<X> getTail() {
        return this.tail;
    }

    // getLength () e toString () invariate
    ...
}
```

E il suo utilizzo:

```java
public class UseList {
    public static void main ( String [] s ) {
        final List<Integer> list = new List<Integer>(10, // Autoboxing
            new List<Integer>(20,
                new List<Integer>(30,
                    new List<Integer>(40, null))));
        // Cast NON necessari
        final int first = list.getHead(); // Unboxing
        final int second = list.getTail().getHead();
        final int third = list.getTail().getTail().getHead();
        System.out.println(first + " " + second + " " + third);
        System.out.println(list.toString());
        System.out.println(list.getLength());

        // Usabile anche con le stringhe
        final List<String> list2 = new List<String>("a",
            new List<String>("b",
                new List<String>("c",
                    new List<String>("d", null))));
        System.out.println(list2.toString());
    }
}
```


Posso avere anche piu' di una type variable
```java
public class C<X,Y>{
    //puoi usare X e Y qui dentro come un qualunque tipo
}
```


### Interfacce generiche

```java
interface I<X,Y>{..}
```

Per creare contratti uniformi che non devono dipendere dai tipi utilizzati.
Esempio notevole: 

## Iterator

Usati per accedere a una sequenza di elementi

(versione semplificata)
```java
public interface Iterator<E> {
    // torna il prossimo elemento dell’ iterazione
    E next();

    // dice se vi saranno altri elementi
    boolean hasNext();

    /* Nota : non `e noto cosa succede se si chiama next() quando hasNext() ha dato esito falso */
 }
```

Implementazione con IntRangeIterator

```java
/* Itera tutti i numeri interi fra ’ start ’ e ’ stop ’ inclusi */
 public class IntRangeIterator implements Iterator<Integer>{

    private int current; // valore corrente
    private final int stop; // valore finale

    public IntRangeIterator(final int start, final int stop){
        this.current = start;
        this.stop = stop;
    }

    public Integer next(){
        return this.current++;
    }

    public boolean hasNext() {
        return this.current <= this.stop;
    }
}
```

## Metodi generici

Un metodo che `lavora` su qualche argomento e/o valore di ritorno in modo independente dal suo tipo effettivo.
Tale tipo viene quindi astratto in una type-variable del metodo.

```java
public static <E> void printAll(final Iterator<E> iterator){ //ora posso lavorare con un iterator generico, prima dovevo per forza definire un Iterator<Integer> ad es
    while (iterator.hasNext()) {
        System.out.println("Elemento: " + iterator.next());
    }
}
```

In UML per fare cio' e' un po' incasinato ma di base si aggiunge un riquadretto trattegiato di fianco al nome della classe con scritto X (var generica)



## Java Wildcards

Esistono situazioni in cui un metodo debba accettare come argomento non solo oggetti di un tipo `C<T>`, ma di ogni `C<S>` dove `S <: T`

```java
// Accetta qualunque Vector <T > con T <: Number
// Vector < Integer > , Vector < Double > , Vector < Float > ,...
void m(Vector <? extends Number> arg) {...} // arg e' READ ONLY

// Accetta qualunque Vector <T >
void m(Vector <?> arg) {...} //arg non puo' ne essere letto ne essere scritto
//pero' puoi ad esempio contare quanti ce ne sono nel vector e cose di questo tipo

// Accetta qualunque Vector <T > con Integer <: T
// Vector < Integer > , Vector < Number > , e Vector < Object > solo !
 void m(Vector <? super Integer> arg) {...} // arg e' WRITE ONLY
```




