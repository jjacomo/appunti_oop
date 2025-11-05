# MECCANISMI AVANZATI (OOP 2.0)
(Veramente anche i generici anche se vengono compilati indietro in cose della OOP 1.0 alla fine)
Veramente tutto quello della OOP 2.0 viene tradotto in cose della OOP 1.0 infatti la JVM alla fine non e' cambiata molto.

OOP 1.0 <- Nesting <- Inner <- Local <- Anonymous <- Lambda
Vengono tradotte cosi'

--- 
## Classi Innestate

In generale nella oop si tende a prediligere molte classi piccole invece che una singola gigante (per manutenzione e capire meglio il codice).

```java 
class A{
    //...
    static class B{
        //...
    }
}
```

* Tutto in un unico sorgente.
* La classe B puo' vedere i campi private di A.
* B deve essere dichiarata `static`.
* Per accedere a b devi fare `A.B mia_classe = new A.B();`
* In teoria il nome della classe partirebbe dal package:  `mio.package.A.B mia_classe = new mio.package.A.B();`
* In teoria potresti anche avere piu' livelli di nesting ma si preferisce non fare per ovvi motivi.

L'unica differenza (con lo scrivere le classi separate) e' il cambio del nome per accedere alla inner e la visibilita' dei campi privati

### ESEMPIO
```java
public class Counter {

    private int value ; // o protected ..

    public Counter ( int initialValue ) {
        this . value = initialValue ;
    }

    public void increment () {
        this . value ++;
    }

    public int getValue () {
        return this . value ;
    }

    public static class Multi extends Counter {
        ... // solito codice
        public Multi ( int initialValue ) {
            super ( initialValue ) ;
        }

        public void multiIncrement ( int n ) {
            for ( int i =0; i < n ; i ++) {
                this . increment () ;
            }
        } 
    }

    public static class Bidirectional extends Counter {
        ... // solito codice
    }
}
```

Per chiamare multiIncrement su un oggetto `Counter.Multi` devi fare `Counter.Multi.multiIncrement(3);`
(Multi in generale non puo' usare i metodi di Counter (in questo caso si perche' extends obv))

### ESEMPIO 2

```java
public class Range implements Iterable<Integer>{

    final private int start;
    final private int stop;

    public Range (final int start, final int stop) {
        this.start = start;
        this.stop = stop;
    }

    public java.util.Iterator<Integer> iterator() {
        return new Iterator(this.start, this.stop);
    }

    //invece di fare un altro sorgente ha piu' senso scriverlo qui direttamente
    private static class Iterator implements java.util.Iterator<Integer>{
        private int current;
        private final int stop;

        public Iterator (final int start, final int stop) {
            this.current = start;
            this.stop = stop;
        }

        public Integer next() {
            return this.current++;
        }

        public boolean hasNext() {
            return this.current <= this.stop;
        }

        public void remove() {}
    }
}
```

--- 
## Enclosing Instance

Se non metti static nella classe dentro (inner), la classe fuori (outer) puoi usare nella inner: 
`Outer.this.campodellaclasseOuter;`
Cioe' l'oggetto dentro adesso puo' vedere anche i campi di quello fuori


### ESEMPIO (miglioriamo range)
```java
public class Range2 implements Iterable<Integer> {

    private final int start;
    private final int stop;

    public Range2 (final int start, final int stop) {
        this.start = start;
        this.stop = stop;
    }

    public java.util.Iterator<Integer> iterator () {
        return this.newIterator();
    }

    private class Iterator implements java.util.Iterator<Integer> {

        private int current;

        public Iterator() {
            this.current = Range2.this.start; // this . current = start
        }

        public Integer next() {
            return this.current++;
        }

        public boolean hasNext() {
            return this.current <= Range2.this.stop;
        }

        public void remove() {}
    }
}
```

Addirittura le classi si possono ficcare dentro i metodi!!!

```java
public class Range3 implements Iterable<Integer>{

    private final int start;
    private final int stop;

    public Range3 (final int start, final int stop) {
        this.start = start;
        this.stop = stop;
    }

    public java.util.Iterator<Integer> iterator() {
        class Iterator implements java.util.Iterator<Integer>{

            private int current;

            public Iterator() {
                this.current = Range3.this.start;
            }

            public Integer next() {
                return this.current++;
            }

            public boolean hasNext() {
                return this.current <= Range3.this.stop;
            }

            public void remove() {}
        }
        return new Iterator();
    }
}
```

Pragmaticamente non si usano quasi mai...

--- 
## Classi Anonime

Invece di definire una classe dentro un metodo e' molto piu' comune fare una classe anonima


```java
public class Range4 implements Iterable<Integer>{

    private final int start;
    private final int stop;

    public Range4 (final int start, final int stop) {
        this.start = start;
        this.stop = stop;
    }

    public java.util.Iterator<Integer> iterator() {
        return new java.util.Iterator<Integer>() { // classe anonima
            // Non ci puo' essere costruttore !
            private int current = start; // o anche Range4.this.start

            public Integer next() {
                return this.current++;
            }
            public boolean hasNext () {
                return this.current <= stop; // o anche Range4.this.stop
            }
            public void remove() {}
        }; // questo `e il ; del return !!
    }
}
```

(Vienen ricompilata uguale a Range3)
Serve per fare estensioni al volo ad una classe. Quello sopra e' la tipica applicazione delle classi anonime.
Non abusarne per robe inutili.

Si usano per indicare le "strategie" come per implementare gli iterator / strategies / ...

--- 
## ENUM
funzionano bene se gli elementi enumerati sono sempre quelli

```java
public enum Region {
    ABRUZZO, BASILICATA, CALABRIA, CAMPANIA, EMILIA_ROMAGNA,
    FRIULI_VENEZIA_GIULIA, LAZIO, LIGURIA, LOMBARDIA, MARCHE,
    MOLISE, PIEMONTE, PUGLIA, SARDEGNA, SICILIA, TOSCANA,
    TRENTINO_ALTO_ADIGE, UMBRIA, VALLE_D_AOSTA, VENETO;
}
```


```java
import java.util.*;

public class UseEnum {
    public static void main ( String [] args ) {
        final List<Region> list = new ArrayList<>();

        list.add(Region.LOMBARDIA);
        list.add(Region.PIEMONTE);
        list.add(Region.EMILIA_ROMAGNA);

        for (finalRegion r : list) {
            System.out.println(r.toString());
        }
    }
}
```

Anche le enum sono classi in java!!!
Infatti esiste gia' il metodo `toString();` o `name();`
(equals serve per due oggetti diversi che rappresentano la stessa cosa, qui si puo' usare ==)
metodo `ordinal();` ti dice il numero corrispondente

E poi si potrebbero fare anche cosi':

```java
public enum Region {
    ABRUZZO("Abruzzo"),
    BASILICATA("Basilicata"),
    CALABRIA("Calabria"),
    CAMPANIA("Campania"),
    EMILIA_ROMAGNA("Emilia Romagna"),
    FRIULI_VENEZIA_GIULIA("Friuli Venezia Giuila"),
    LAZIO("Lazio"),
    LIGURIA("Liguria"),
    LOMBARDIA("Lombardia"),
    MARCHE("Marche"),
    MOLISE("Molise"),
    PIEMONTE("Piemonte"),
    PUGLIA("Puglia"),
    SARDEGNA("Sardegna"),
    SICILIA("Sicilia"),
    TOSCANA("Toscana"),
    TRENTINO_ALTO_ADIGE("Trentino Alto Adige"),
    UMBRIA("Umbria"),
    VALLE_D_AOSTA("Valle D’Aosta"),
    VENETO("Veneto");

    private final String actualName;

    private Region(final String actualName) {
        this.actualName = actualName;
    }

    public String getName() {
        return this.actualName;
    }
```
