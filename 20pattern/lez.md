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

---

## Principi:

* `DRY`: Don’t repeat yourself
    Never let two pieces of code be the same or even very similar in your code base
* `KISS`: Keep it simple, stupid
    Always favour the simplest solution if adequate, understandability is key!
* `SRP`: Single Responsibility Principle
    Each component (class) should have one reason to change
    ⇒ should be split, so that each class has one reason to change only!
* `OCP`: Open-closed principle
    Each component (class) should be closed to change but open to extension
    ⇒ anticipate change, design so that a change is a new class to add
* `DIP`: Dependency-inversion principle
    High-level modules should not depend on low-level modules, rather on abstractions
    ⇒ Do not depend on others’ implementations, abstract them into interfaces!

S. O. L. I. D.
R  C  S  S  I
P  P  P  P  P


## Classificazione

* Creazionali: Riguardano la creazione degli oggetti
* Strutturali: Riguardano la composizione di classi/oggetti
* Comportamentali: Riguardano la interazione e distribuzione di
responsabilita' fra classi/oggetti


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

---

### Strategy

Praticamente le lambda le hanno create anche per favorire l'uso del pattern strategy.
E' usato ovunque anche quando non te ne accorgi: Layout Componenti (boxLayout, Grid, ...), Strategie di confronto tra due elementi per sorting (Comparable), Strategie di map, filter...

Anticipation of change.
Sai che c'e' un metodo di una classe che probabilmente `andra' modificato di frequente`.
Ad esempio in BankAccount hai il metodo preleva che deve calcorare ogni volta la tariffa.
Solo che il modo in cui e' calcolata la tariffa puo' cambiare spesso... 
Allora si crea un'`interfaccia` FeeCalculator con il metodo per calcolare la tariffa.
Cosi' in BankAccount lavori sempre con l'interfaccia che pero' puo' essere implementata da classi diverse.
Non dipende da un dettaglio implementativo (non e' un'implementazione) ma una interfaccia.
Principio DIP: il mio oggetto non dipende piu' da una implementazioni, le implementazioni devono aderire (dipendono) alla mia interfaccia!
OCP: Posso cambiare sempre implementazione con altri oggetti che implementano l'interfaccia (sono interscambiabili tanto per definizione)

Quando usi un pattern devi seguire il suo `intento` e la sua `struttura`.

* Intento:
    Definisce una famiglia di algoritmi, e li rende interscambiabili, ossia usabili in modo trasparente dai loro clienti

* Struttura:
    BankAccount si compone (e' un suo campo) di un BankOperationFee (che e' un'interfaccia).
    Esistono diverse implementazioni per questa interfaccia (scelgo quello che voglio).
    Se BankAccount non si componesse ma `usasse` un BankOperationFees nel suo metodo withdraw
    allora si tratterebbe sempre di un pattern strategy.

```java
@FunctionalInterface // una interfaccia con un solo metodo e' funzionale (puoi implementarla con una lambda)
public interface BankOperationFees {
    int fee(int operationAmount);
}
```

```java
public class StandardBankOperationFees implements BankOperationFees{
    public int fee(int operationAmount){
        return operationAmount < 1000 ? 1 : 2;
    }
}
```

---

### Template Method (simile a strategy)

Strategy usa la composizione.
Questo usa la ereditarieta'.
Ho un conto corrente generale.
I BankAccount specifici invece implementeranno la computeFee e erediteranno da quella generale.

* Intento:
    Definisce lo scheletro (template) di un algoritmo (o comportamento),
    lasciando l’indicazione di alcuni suoi aspetti alle sottoclassi.

```java
public interface BankAccount{ //l'interfaccia ci va sempre
    int getAmount();
    void withdraw();
}

public abstract class AbstractBankAccount implements BankAccount{ // conto corrente generale
    private int amount;

    public AbstractBankAccount(int amount){
        this.amount = amount;
    }
    public int getAmount(){
        return this.amount;
    }

    public void withdraw(int n){ // e' un template method (chiama un metodo astratto)
                                 // infatti alcune cose le dice ma altre no, e' un template, uno schema
        this.amount = this.amount - n - fee(n);
    }
    //potrebbero esserci anche altri template method

    protected abstract int fee(int n); // le sottoclassi devono implementare questo
}

public class BasicBankAccount extends AbstractBankAccount{ // implementazione vera e propria
    public BasicBankAccount(int amount){
        super(amount);
    }

    protected fee(int n){
        return n/3;
    }
}
```

#### Stratey vs Template Method

Nel strategy va specificato solo una strategia a una classe.

Nel template invece ci sono proprio diverse classi (diverse implementazioni) da usare per cambiare la fee del conto corrente.
Uno svantaggio del template e' che e' un po' meno impattante, allo stesso tempo pero' posso ridefinire altre operazioni (non solo la fee).

Il template lo usi quando non ci sono poi cosi' tante possibili implementazioni (tipo una banca rende disponibili solo 3 tipi di conti correnti) e allora ha senso creare tre classi diverse (tipo BasicBankAccount, GoldBankAccount, SilverBankAccount) e basta. 
Lo strategy invece quando le implementazioni di fee possono essere davvero un sacco e cambiare continuamente nel tempo (non ha senso creare 100mila classi).

---

### Decorator

* Intento
    Aggiunge ad un oggetto ulteriori responsabilita', dinamicamente, e in modo piu'
    flessibile (e componibile) rispetto all’ereditarieta'

Ad esempio un BankAccount ha due implementazioni, un GoldBankAccount e un BasicBankAccount
Voglio creare una classe con tutte e due le implementazioni portate da entrambi.
Ho sempre interfaccia BankAccount.
Ci sono diverse implementazioni di BankAccount ognuna delle quali implementa un singolo aspetto.
Ogni implementazione si `compone` di un BankAccount!
Ho un decorator che e' una classe BankAccountDecorator che implementa BankAccount e si compone di un BankAccount.
In questo modo posso implementare altri BankAccount che implementano operazioni specifiche e implementano il Decorator.

public class

```java
public class UseBankAccount {
    public static void main(string[] args){ // Definition of the item to test 
        final BankAccount b = new WithFee(new BasicBankAccount (100)); 
        final BankAccount b2 = new BasicBankAccount (100);
        final BankAccount b3 = new Rigid(new BasicBankAccount (100));
        final BankAccount b4 = new WithFee(new Rigid(new BasicBankAccount (100)); //aggiungo tante decorazioni
                // WithFee, Rigid sono tutte implementazioni di BankAccountDecorator
                // BankAccountDecorator e' un implementazione ultra basic
        // Use/Test of the item 
        b.withdraw(20);
        System.out.println.() //70
    }
}
```

E' un potenziamento del template

### Observer 
Abbiamo un oggetto, siamo interessati di essere notificati quando in un oggetto succede qualcosa.
E' come aggiungere un EventListener in Swing (si usano proprio gli observer)

da approfondire da solo.

---

## Pattern Creazionali

### Factories

Static, Simple, Factory Method, Abstract.
Per fare costruttori qualsiasi di qualsiasi oggetto (Builder?).
Un oggetto che ha 3 campi opzionali possiamo avere 2^3 combinazioni di costruttori.
Addirittura ci sono dei problemi curiosi che non potrei implementare.

```java
// due costruttori
public Person(String name, String surname, int year){
    this.name = name;
    this.surname = surname;
    this.city = null;
    this.year = year;
}

public Person(String name, String city, int year){ //non compila!!! Ha gli stessi argomenti
    this.name = null;
    this.surname = surname;
    this.city = city;
    this.year = year;
}

// Vorrei poter avere costruttori con NOMI diversi
```

Come risolvo?
#### Static Factory

```java
// in Person.java
private Person(String name, String surname, String city, int year){ // lo metto privato altrimenti lo potrei
                                                    // usare 
    this.name = name;
    this.surname = surname;
    this.city = city;
    this.year = year;
}

public static Person create(String name, String surname, String city, int year){
    return new Person(name, surname, city, year);
}

public static Person createWithoutCity(String name, String surname, int year){
    return new Person(name, surname, null, year);
}

public static Person createWithoutName(String surname, String city, int year){
    return new Person(null, surname, city, year);
}
```

Ora nel main posso fare:
`Person.createWithoutCity("lucia", "bianchi", 1999)`
`Person.createWithoutCity("bianchi", "cesena", 1999)`

Ma adesso ancora violo il DIP perche' il mio main dipende dalla implementazione della classe Person.

#### Simple Factory

E' il passo successivo: Creo un interfaccia in cui ci metto anche i metodi per creare
==> e' ancora intermedio

