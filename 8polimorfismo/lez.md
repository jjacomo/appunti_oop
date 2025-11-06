# INTRO

Polimorfismo inclusivo con le classi, layout di oggetti in memoria, abstract

## Polimorfismo inclusivo con le classi

Ricorda il `principio di sostituibilita'`...

Con la definizione: 
```java
class B extends A{..}
```

Gli oggetti della classe B rispondo a tutti i messaggi previsti dalla classe A, ed eventualmente a qualcuno in piu'.
Quindi un oggetto della classe B potrebbe essere passato dove se ne aspetta uno della classe A, senza comportare problemi (di “typing”).
Hanno lo stesso `contratto` (come le interfacce).

### Il polimorfismo:
* Fornisce sopratipi che raccolgono classi uniformi tra loro.
* Usabili da funzionalita' ad alta riusabilita'
* Utile per costruire collezioni omogenee di oggetti (come l'oggetto Device)
### Con interfacce: 
* Solo relativo ad un contratto
* Facilita' nel far aderire al contratto classi esistenti
* Spesso vi `e la tendenza a creare un alto numero di interfacce
### Con classi: 
* Relativo a contratto `e comportamento`
* In genere ci si aderisce per costruzione dall’inizio
* Vincolato dall’ereditarieta' singola

--- 

## Eredita' multipla

```java
interface Counter{...}
interface MultiCounter extends Counter{...}
interface BiCounter extends Counter{...}
interface BiAndMultiCounter extends MultiCounter,BiCounter{...}
class CounterImpl implements Counter{...}
class MultiCounterImpl extends CounterImpl implements MultiCounter{...}
class BiCounterImpl extends CounterImpl implements BiCounter{...}
class BiAndMultiCounterImpl extends BiCounterImpl implements BiAndMultiCounter{...}
```


#### Object

Visto che tutti gli oggetti ereditano da Object posso fare container polimorfici, ad esempio via array di tipo Object[]:
```java
new Object[]{new SimpleLamp(),new Integer(10)}
```


#### instance of
```java
for(final Object o : array) {
    System.out.println("Object: " + o.toString());
    if(o instanceof Integer){ // test a runtime
        final Integer i = (Integer) o; // ( down ) cast
        sum = sum + i.intValue();
    }
}
```

--- 

## Classi Astratte

Le interfacce descrivono solo un contratto.
Le classi definiscono un comportamento completo.
Le abstract sono li `in mezzo`:
Sono usate per descrivere classi dal comportamento parziale, ossia in cui alcuni metodi sono dicharati ma non implementati.
Tali classi non sono istanziabili (l’operatore new non pu`o essere usato).
Possono essere estese e ivi completate, da cui la generazione di oggetti.

```java
abstract class C ... {
    abstract int m(int a, String s); //metodo astratto (come quelli delle interfacce: non implementati)

    // puo' definire campi, costruttori, metodi, concreti e non
}
```

Tipica applicazione: pattern `Template Method`
Serve a dichiare uno schema di strategia con un metodo che definisce uncomportamente comune (spesso final), ma che usa metodi da concretizzare in sottoclassi.


### Es
```java
public abstract class LimitedLamp extends SimpleLamp {
    public LimitedLamp(){
        super();
    }
    /* Questo metodo `e finale :
* - regola la coerenza con okSwitch () e isOver () */
    public final void switchOn(){
        if (!this.isSwitchedOn() && !this.isOver()){
            super.switchOn();
            this.okSwitch();
        }
    }

    // Cosa facciamo se abbiamo effettivamente acceso ? Dipende dalla strategia
    protected abstract void okSwitch();

    /* Strategia per riconoscere se la lamp `e esaurita */
    public abstract boolean isOver();

    public String toString(){
        return "Over: " + this.isOver() +
        ", switchedOn: " + this.isSwitchedOn();
    }
}

/* Non si esaurisce mai */
public class UnlimitedLamp extends LimitedLamp{

    /* Nessuna informazione extra da tenere */
    public UnlimitedLamp(){
        super();
    }

    /* Allo switchOn .. non faccio nulla */
    protected void okSwitch(){
    }

    /* Non `e mai esaurita */
    public boolean isOver(){
        return false;
    }
}
```

Poi puoi implementare anche CountdownLamp (si esaurisce quando un contatore arriva a 0),
ExpirationLamp (dopo un tot di tempo) etc . . . 


Sono tipo delle interfacce un po' piu' severe (puoi implementare dei metodi negli abstract)

---

### Wrapper dei tipi primitivi

Sono tipo `Integer` per gli int, `Double` per i double, ...

---

### Variable arguments

L'ultimo (o unico) argomento di un metodo puo' essere del tipo 
`Type... argname`

```java
void m(int a, float b, Object... args){...}
```

dove nel body del metodo argname e' trattato come un `Type[]`


