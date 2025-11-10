# INTRO

Type-erasure, polimorfismo vincolato, wildcards.

## Type-erasure

I generici esistono solo a tempo di compilazione ma a runtime vengono cancellati:

```java
List<String> lista1 = new ArrayList<>();
List<Integer> lista2 = new ArrayList<>();
```

A prima vista, sembrano due tipi diversi, ma dopo la compilazione, diventano entrambi:
```java
List lista1 = new ArrayList();
List lista2 = new ArrayList();
```

Quello che il compilatore fa e' questo:
```java
public class Box<T> {
    T value;
    void set(T v) { value = v; }
    T get() { return value; }
}

// DIVENTA ->

public class Box {
    Object value;
    void set(Object v) { value = v; }
    Object get() { return value; }
}

```

### Conseguenze:

#### Niente informazioni di tipo a runtime

Non puoi sapere quale tipo generico ha una collezione a esecuzione:
```java
List<String> nomi = new ArrayList<>();
if (nomi instanceof List<String>) { } // ERRORE: il tipo generico sparisce!
```

Il compilatore non lo permette perché a runtime `List<String>` e `List<Integer>` sono identici.

#### Non puoi creare istanze di tipi generici

```java
public class Box<T> {
    T valore;

    public Box() {
        valore = new T(); // ERRORE: non si può!
    }
}
```
Il compilatore non sa quale tipo concreto rappresenti T a runtime, quindi non può creare l’oggetto.

#### Non puoi creare array di tipi generici

```java
List<String>[] arrayDiListe = new List<String>[10]; // ERRORE
```

Questo è vietato perché gli array mantengono informazioni di tipo a runtime, mentre i generics no → combinandoli si creerebbero incoerenze.

### Es implementazione Vector<X>
```java
public class Vector<X>{
    private final static int INITIAL_SIZE = 10;

    private Object[] elements; // Deposito elementi, non posso usare X[]!!
    private int size; // Numero di elementi

    public Vector() { // Inizialmente vuoto
        this.elements = new Object[INITIAL_SIZE];
        this.size = 0;
    }

    public void addElement(final X e){
        if (this.size == elements.length) {
            this.expand(); // Se non c’`e spazio
        }
        this.elements[this.size] = e;
        this.size++;
    }

    public X getElementAt(final int position){
        // unchecked warning
        return (X)this.elements[position];
    }
    public int getLength(){
        return this.size;
    }

    private void expand(){ // Raddoppio lo spazio ..
        final Object[] newElements = new Object[this.elements.length * 2];
        for(int i = 0; i < this.elements.length; i++){
            newElements[i] = this.elements[i];
        }
        this.elements = newElements;
    }

    public String toString(){
        String s = " | ";
        for (int i = 0; i < size; i++){
            s = s + this.elements[i] + " | ";
        }
        return s;
    }
 }
```

## Polimorfismo vincolato

Data una classe `C<X>`, X puo' essere istanziato a qualunque sottotipo di Object.
In effetti la definizione class `C<X>{..}` equivale a `class C<X extends Object>{..}`

In generale invece, puo' essere opportuno vincolare in qualche modo le possibili istanziazioni di X, ad essere sottotipo di un tipo piu' specifico di Object.
```java
class C<X extends D>{..}
```
In tal caso, dentro C, si potra' assumere che gli oggetti di tipo X rispondano ai metodi della classe D.


## Java wildcards


DAI NON C'E' BISOGNO CIAO







