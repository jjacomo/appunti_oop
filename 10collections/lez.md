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

E' la radice della gerarchia delle collezioni

E' un interfaccia che definisie i metodi come `size()`, `add()`, `remove()`, `contains(Object o)`...

```java
public interface Collection <E > extends Iterable <E > {
    // Query Operations
    int size(); // number of elements
    boolean isEmpty(); // is the size zero ?
    boolean contains(Object o); // does it contain an element equal to o ?
    Iterator<E> iterator(); // yields an iterator
    Object[] toArray(); // convert to array of objects
    <T> T[] toArray(T[] a); // puts in ‘a ‘ , or create new if too small

    // Modification Operations
    boolean add(E e); // adds e
    boolean remove(Object o); // remove one element that is equal to o

    // Bulk Operations
    boolean containsAll(Collection<?> c); // contain all elements in c

    boolean addAll(Collection<? extends E> c); // add all elements in c
    boolean removeAll(Collection<?> c) ; // remove all elements in c
    boolean retainAll(Collection<?> c) ; // keep only elements in c
    void clear(); // remove all element

    // ... and other methods introduced in Java 8
 }
```

#### Creare collezioni immutabili:
```java
public class UseFactories {
    public static void main ( String [] s ) {
        // Metodi statici di creazione per Set e List immutabili

        final Set<Integer> set = Set.of(1, 2, 3, 4, 5, 6);
        System.out.println(set);

        final List<String>list = List.of("a", "b", "c", "a");
        System.out.println(list);

        final Set<String>set2 = Set.copyOf(list);
        System.out.println(set2);
    }
 }
```


### List

```java
public interface List<E> extends Collection<E> {
    // Additional Bulk Operations
    boolean addAll(int index, Collection<? extends E> c);

    // Positional Access Operations
    E get(int index); // get at position index
    E set(int index, E element); // set into position index
    void add(int index, E element); // add , shifting others
    E remove(int index); // remove at position index

    // Search Operations
    int indexOf(Object o); // first equals to o
    int lastIndexOf(Object o); // last equals to o

    // List Iterators (introdotto in java 8, e' come un iterator ma ha metodi anche per tornare indietro e darti l'index del prev e del next)
    ListIterator<E> listIterator(); // iterator from 0
    ListIterator<E> listIterator(int index); // .. from index

    // View
    List<E> subList(int fromIndex, int toIndex);
 }
```


## Una modalita' di progettazione da ricordare
* `Interfacce`: riportano le funzionalita' definitorie del concetto
* `Classi astratte`: fattorizzano codice comune alle varie implementazioni
    1. AbstractCollection, AbstractList, e AbstractSet;
    2. Realizzano “scheletri” di classi per collezioni, corrispondenti alla relative interfacce;
    3. Facilitano lo sviluppo di nuove classi aderenti alle interfacce;
* `Classi concrete`: realizzano le varie implementazioni

Valgono anche (soprattutto) per le collezioni.



## Set
* Nessun elemento duplicato (nel senso di Object.equals());
* Il problema fondamentale e' il metodo contains(), nelle soluzioni piu' naive (con iteratore) potrebbe applicare una ricerca sequenziale, e invece si richiedono in genere performance migliori;


### HashSet
Si usa il metodo `Object.hashCode()` come funzione di hash, usata per
posizionare gli elementi in uno store di elevate dimensioni;

* serve implementare un metodo per l'hashing e un per equals.

#### hashCode e equals
```java
public class Person{
    // altro ..

    public int hashCode() {
        return Objects.hash(name ,year);
    }

    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (!(obj instanceof Person)){
            return false;
        }
        Person other = (Person) obj;
        return Objects.equals(name ,other.name) && year == other.year;
    }
}
```

### TreeSet
Specializzazione di SortedSet e di NavigableSet. Gli elementi sono `ordinati`, e quindi organizzabili in un albero (red-black tree) per avere accesso in tempo logaritmico.

* serve implementare un comparator per decidere l'ordine

#### Comparable

```java
public interface Comparable<T> {
    /* returns : 0 (this == o), positive (this > o)
 negative (this < o) */
    public int compareTo(T o);
}
```

```java
public class Person implements Comparable < Person >{
    ...
    // Esempio di implementazione di compareTo :
    // - ordine di anno di nascita , e poi per nome ..
    public int compareTo(Person p){
        return (this.year != p.year)
        ? this.year - p.year
        : this.name.compareTo(p.name);
    }
}
```

#### Comparatore esterno

"Il giudice esterno"

```java
public interface Comparator<T>{
    // 0 if o1 == o2 , neg if o1 < o2 , pos is o1 > o2
    int compare(T o1, T o2);
}
```

```java
public class PersonComparator implements Comparator<Person>{
    // Confronto prima sul nome, poi sull’anno
    public int compare(Person o1, Person o2){
        return o1.getName().equals(o2.getName())
        ? o1.getYear() - o2.getYear() // Integer . compare ( o1 . getYear () , o2 . getYear () )
        : o1.getName().compareTo(o2.getName());
    }
}
```


