# INTRO

Progettazione e Patterns.

## Progettazione

4 fasi:

1. `Analisi`:
    Si definisce in modo preciso il `problema` da risolvere.
    "Voglio fare clone identico di pacman". Ho gia' completamente definito il problema.
    Si definiscono i requisiti del problema.
    Si fornisce il modello del dominio (la descrizione del dominio in cui opera quel software, se devo programmare gestionale per uno stabilimento balneare devo conoscere tutto degli stabilimenti, se programmo pacman devo essere "esperto di pacman", conoscere tutte le regole...).
    Definisce solo le aspettative di input output, non si occupa di come fare la roba concretamente nel software.
    Come il software appare e funziona.
    Si lavora pensando a concetti (infatti si usano anche diagrammi UML per rappresentare il modello di dominio)

2. `Design` (Progettazione):
    * Si definisce la struttura complessiva del software del sistema.
        "Bene, lo faccio in java", "vediamo che package fare, e le classi principali", "che relazione hanno tra loro queste parti, come comunicano".
        Si danno alcuni elementi per guidare poi la programmazione.
        Si definisce solo la struttura complessiva del sistema in termini dei principali moduli (classi, o meglio interfacce) di cui esso e' composto e delle relazioni macroscopiche (“uses”, “has-a” o “is-a”) fra di essi.
    * Design di Dettaglio: si va a piu' basso livello.
        Descrive interfacce, classi astratte e concrete che rappresentano la soluzione si principali problemi individuati nell'analisi.

3. `Implementazione`:

4. `Post codifica` (collaudo, manutenzione, deployment):


## Progettazione

* architetturale
se hai MVC fai vedere l'UML generale con model, view e controller.
Fai vedere i concetti principali e come parlano tra di loro.
* di dettaglio
descrive le relazioni tra le classi `piu' importanti`.
ancora diagrammi UML.


`MVC` e' un `pattern` architetturale. Ce ne sono altri ma vediamo solo questo.


#### Consigli

es. Risiko
Darsi 2-3 obbiettivi intermedi.
obb 1: hai la mappa e puoi cliccare nei territori per aumentare i carriarmatini
obb 2: puoi spostare i carriarmatini in altri territori e fare battaglie
obb 3: aggiungi feature X e sistema grafica

Ogni obbiettivo apprioccialo a cascata: le fasi di sviluppo in ordine temporale.


# Pattern di progettazione

Nascono da un libro (famosissimo): Design Patterns: Elements of Reusable Object-Oriented Software.
Questo libro propone 23 pattern con ognuno un suo nome che ha preso piede ed e' usato tutt'ora.
Non li hanno inventati questi autori, hanno preso delle codebase pubbliche che risolvevano problemi diversi e hanno individuato aspetti simili per risolvere problemi simili (patterns).

Li puoi usare a priori, prima ancora di scrivere codice so' gia' che usero' questo pattern.
Oppure in `refactoring`, vedo che la soluzione puo' essere organizzata meglio con dato pattern ("si progetta mentre si sviluppa" (anche)).

Il refactoring in particolare e' inevitabile: di solito implementando si inizia con un `prototipo`, poi si `espande` il prototipo e infine si fa quasi sempre `refactoring` perche' probabilmente non sara' scritto nel modo migliore e piu' efficente (designamente parlando).

Non passi a scrivere altro codice finche' non hai rifattorizzato bene il codice precedente.

## Principi:

* DRY: Don’t repeat yourself
    Never let two pieces of code be the same or even very similar in your code base
* KISS: Keep it simple, stupid
    Always favour the simplest solution if adequate, understandability is key!
* SRP: Single Responsibility Principle
    Each component (class) should have one reason to change
    ⇒ should be split, so that each class has one reason to change only!
* OCP: Open-closed principle
    Each component (class) should be closed to change but open to extension
    ⇒ anticipate change, design so that a change is a new class to add
* DIP: Dependency-inversion principle
    High-level modules should not depend on low-level modules, rather on abstractions
    ⇒ Do not depend on others’ implementations, abstract them into interfaces!

S. O. L. I. D.
R  C  S  S  I
P  P  P  P  P


## Classificazione

* Creazionali: Riguardano la creazione degli oggetti
* Strutturali: Riguardano la composizione di classi/oggetti
* Comportamentali: Riguardano la interazione e distribuzione di
responsabilit`a fra classi/oggetti


## Patterns

### Proxy

```java
public class Factorial {
    // Problem : add caching for input -> output , checking input
    public static int factorial(int i){
        return i == 0 ? 1 : i * factorial(i - 1);
    }
}
```

Quando mi richiedono di calcolare lo stesso fattoriale vado prima a guardare se lo avevo gia' salvato e uso quello. Faccio prima. SRP. OCP (se c'e' una modifica prevedibile, questa dovrebbe essere sempre poter implementata senza toccare la classe di partenza).

```java
public class Factorial {
    private final static Map<Integer, Integer> factCache = new HashMap<>();

    public static int factorial(int i){
        if (!factCache.containsKey(i)){
            factCache.put(i, factorial2(i));
        }
        return factCache.get(i);
    }

    private static int factorial2(int i){
        return i == 0 ? 1 : i * factorial2(i - 1);
    }
 }
```

Questa gestione della cache viola OCP perche' modifica la classe del fattoriale.
Prima era fattoriale puro che funzionava, adesso e' un po' piu' brutto.

```java
public interface Factorial {
    int factorial(int i);
}
```

```java
public class FactorialImpl implements Factorial{
    public int factorial(int i){
        return i == 0 ? 1 : i * factorial(i - 1);
    }
}
```

```java
public class FactorialWithCache implements Factorial { // e' il proxy
    // codice che decide che implementazione usare,
    private final Factorial base = new FactorialImpl(); // composizione

    private final Map<Integer, Integer> map = new HashMap<>();

    // codice che usa l'implementazione
    public int factorial(int i) {
        return this.map.computeIfAbsent(i, base::factorial); // cosi' non 
    }
 }
```

In UseFactorial si lavora con l'interfaccia che pero' sara' una new FactorialWithCache o FactorialImpl a seconda di quella che voglio usare.
Il fatto che si lavori con l'interfaccia e' molto piu' facile aggiungere nuove implementazioni di fattoriale.


Intento del proxy:
Controllare l’accesso ad un oggetto per:
ottimizzare il costo della sua inizializzazione/use
fornire accesso trasparente alla rete
fornire limitazioni alla sua funzionalit`a (p.e., solo lettura)

Lo abbiamo gia' visto col metodo `List.of` che crea un proxy che si mette prima della lista e rigetta le richieste di metodi non supportati (add, remove,. ..)


### Strategy

Praticamente le lambda le hanno create anche per favorire l'uso del pattern strategy.
E' usato ovunque anche quando non te ne accorgi: Layout Componenti (boxLayout, Grid, ...), Strategie di confronto tra due elementi per sorting (Comparable), Strategie di map, filter...

Anticipation of change. Sai che c'e' un metodo di una classe che probabilmente andra' modificato di frequente.
Ad esempio in BankAccount hai il metodo preleva che deve calcorare ogni volta la tariffa.
Solo che il modo in cui e' calcolata la tariffa puo' cambiare spesso... 
Allora si crea un'interfaccia FeeCalculator con il metodo per calcolare la tariffa.
Cosi' in BankAccount lavori sempre con l'interfaccia che pero' puo' essere implementata da classi diverse.

* Intento:
    Definisce una famiglia di algoritmi, e li rende interscambiabili, ossia usabili in modo trasparente dai loro clienti

```java
@FunctionalInterface
public interface BankOperationFees {

int fee ( int operationAmount ) ;
}
```

```java
public class S t a n d a r d B a n k O p e r a t i o n F e e s implements BankOperationFees {

public int fee ( int operationAmount ) {
return operationAmount < 1000 ? 1 : 2;
}
}
```


