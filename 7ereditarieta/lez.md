# INTRO

Ereditarieta', protected, overriding, super, final


### ES riuso con composizione
```java
public class MultiCounter2 {
    private final Counter counter;
    public MultiCounter2(final int initialValue){
        this.counter = new Counter(initialValue);
    }

    public void increment(){
        this.counter.increment();
    }

    public int getValue(){
        return this.counter.getValue();
    }

    /* Nuovo metodo */
    public void multiIncrement(final int n){
        for (int i = 0; i < n; i++){
            this.counter.increment();
        }
    }
}
```

## Riuso via Ereditarieta

Meccanismo per `specializzare` una classe esistente, ereditando i suoi metodi e aggiungendone di nuovi.

```java
public class MultiCounter extends Counter {
    /*
* I costruttori vanno ridefiniti . Devono tuttavia richiamare
* con super (...) quelli ereditati dalla sopraclasse
*/
    public MultiCounter(int initialValue){
        super(initialValue);
    }
    // increment e getValue automaticamente ereditati

    // si aggiunge multiIncrement
    public void multiIncrement(final int n){
        for (int i = 0; i < n; i++){
            this.increment();
        }
    }
}
```
E' come counter ma a cui aggiungo un nuovo costruttore (che in realta' richiama quello del padre) e multiIncrement


In `UML` e' una linea con triangolo pieno

---

## Livello Protected

E' intermedio tra public e private.
Consente l'accesso alle sottoclassi (il private no) ma e' comunque preferibile usare private con getter e setter.
Consente l'accesso da tutto il package.

---

## Overriding

Quando si crea una nuova classe per estensione, molto spesso non e'sufficiente aggiungere nuove funzionalita'.
A volte serve anche modificare alcune di quelle disponibili, eventualmente anche stravolgendone il funzionamento originario.

```java
public class LimitCounter extends ExtendibleCounter {

    /* Aggiungo un campo , che tiene il limite */
    protected final int limit;

    public LimitCounter(final int limit){
        super(0);
        this.limit = limit;
    }

    public boolean isOver(){
        return this.getValue() == this.limit;
    }

    /* Overriding del metodo increment() */
    public void increment(){
        if (!this.isOver()){
            super.increment();
            // super in questo caso chiama la increment del padre
        }
    }
}
```

---

## Final

Non solo per i campi (che se dichiarati final possono essere modificati una sola volta nell'inizializzazione) ma anche per metodi e classi:
* Un `metodo final` e' una metodo che non puo' essere overridato
* Un `classe final` e' una classe che non puo' essere estesa (ereditata)

Ad esempio la classe String e' final

---

## Classe Object

Ogni classe eredita sempre da Object.
E' la radice della gerarchia delle classi in java

Fornisce metodi di utilita' generale:
* `toString()`, che stampa informazioni sulla classe e la posizione in memoria dell’oggetto;
* `clone()`, per clonare un oggetto;
* `equals()` e `hashCode()`, usati nelle collection;
* `notify()` e `wait()`, usati nella gestione dei thread;


