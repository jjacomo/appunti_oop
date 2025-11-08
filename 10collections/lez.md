# INTRO

Java Collections Framework.
Iterators, for each, collezioni, liste, set, hashset, treeset.

`Iterable` <-(estesa da) `Collection`     <-    (estesa da)    `List`, `Queue`, `Set` 
   ^                     ^                                         ^
   |                     |                                         |
interfaccia          interfaccia                ancora interfacce (sempre piu' definite)
                         ^                                         ^
                         |                                         |
                aggiunge operazioni come la                        |
                rimozione e aggiunta di cose                       |
                                                                   |
                                            Ad esempio list consente di accedere per indice e ha un ordine;
                                     Set invece non ha ordine, queue invece ha solo add (dal fondo) e remove (alla fine)
                                                                   ^
                                                                   |
                                                                   |
                                Sono implementate dalle classi `ArrayList`/`LinkedList` per List, `HashSet` e `TreeSet` per i Set.


..
E poi ci sono le `Map`s (interface) che saranno implementate da `HashMap`...

Poi `Iterator` e `Comparable`...

E poi classi con funzionalita': `Collections` e `Arrays`



## For Each e Iterable
Abbiamo visto che puo' essere usato per iterare su un array in modo piu' astratto (compatto, leggibile).

```java
for(int i: array){...}
```

Java fornisce anche un meccanismo per usare il `foreach su qualunque collection`, in particolare, su qualunque oggetto che implementa l’interfaccia `java.lang.Iterable<X>`

L’interfaccia `Iterable` ha un metodo per generare e restituire un (nuovo) `Iterator`.
Un iteratore e' un oggetto con metodi:
* `next()`;
* `hasNext()`;
* (e `remove()`).

Dato l’oggetto coll che implementa Iterable<T> allora il foreach diventa:
```java
for(T element: coll){...}
```


Interfacce per l'iterazione:

```java
package java.lang;
import java.util.Iterator;
public interface Iterable<T> {
    /* *
    * Returns an iterator over a set of elements of type T.
    *
    * @return an Iterator.
*/
    Iterator<T> iterator(); // quidni una classe che implementa iterable deve dichiarare un metodo che si chiama iterator e returna un iterator
 }

// ------------------------------------
package java.util;
public interface Iterator<E> {  // l'iterator deve essere fatto cosi'
    boolean hasNext();
    E next();
    void remove(); // throws Unsupported Operation Exception
}

// ------------------------------------
package java.util;
public interface Collection<E> implements Iterable<E> {..} // la collection DEVE implementare iterable
```

### ESEMPIO


```java
public class Range implements Iterable<Integer> {

    private final int start;
    private final int stop;

    public Range(final int start, final int stop){
        this.start = start;
        this.stop = stop;
    }

    public java.util.Iterator<Integer> iterator() {
        return new RangeIterator(this.start, this.stop);
    }
 }

// __________________________________________

public class UseRange{
    public static void main(String[] s) {
        for (final int i : new Range(5, 12)) {
            System.out.println(i);
            // 5 6 7 8 9 10 11 12
        }
    }
}


//-------------------------------------------

class RangeIterator implements java.util.Iterator<Integer>{

    private int current;
    private final int stop;

    public RangeIterator(final int start, final int stop) {
        this.current = start;
        this.stop = stop;
    }

    public Integer next(){
        return this.current++;
    }

    public boolean hasNext(){
        return this.current <= this.stop;
    }

    public void remove(){
    }
}
```


---

## Collection

E' un interfaccia che definisie i metodi come `size()`, `add()`, `remove()`, `contains(Object o)`...





