31/10/2025
# ESECUZIONE PROGRAMMI JAVA

JUnit

quando compili i tuoi .java nella dir src i .class vanno a finire nella dir bin

`.java (/src)` -> `javac` -> `.class (/bin)`
                            ||   e' caricato in memoria

| Stack | Heap | classi |
|-------|------| -------|
| puntatori | classi istanziate | .class  |



Quando lanci il comando java (la JVM) gli puoi passare dei flag per impostare roba


```java
package aula.a14;

public class Play{
    class C {} //Play.C

    public static void main(String[] args){
        var c = new Object(){};
        var c2 = new Object(){};
        var c3 = new Object(){};
    }
}

```

Le tre classi anonime vengono tradotte in classi innestate che venogono tradotte in ...
in poche parole quando poi compili questi file non ti crea un unico .class veramente ma in questo caso te ne crea 5: Play, Play$C, Play$c, Play$c2, Play$c3.

Il .class contiene l'assembly (non quello vero) per l'hardware finto che e' la JVM.
Per ispezionare i .class esiste il comando `javap` che ti tira fuori l'assembly fasullo.
L'assembly di java lavora solo con lo stack, non coi registri.

La JVM e' scritta in C/C++ ed e' progettata per andare il piu' veloce possibile
Dispone di diversi componenti come il ClassLoader (carica i .class) e il GarbageCollector.
Per massimizare la velocita' le classi non vengono caricate in memoria appena avvii il programma ma vengono caricate `By Need`. Carica una classe solo se la devi usare (quando fai la new). 
Quindi c'e' sicuramente un piccolo gruppo di classi che vengono caricate sempre (tipo Object e String).

Esiste il fenomeno di `slow start` quando il tuo programma chiama subito un sacco di classi.
Ci sono anche dei rallentamenti quando ad esempio nel tuo gioco entri per la prima volta nello shop


---
## Initializer

Raro ma si puo' fare.

```java
class Counter{
    static{
        //codice che viene eseguito sempre appena CARICHI la classe
        System.out.println("classe Counter caricata!");
    }
    static int i; 

    private int countValue;
    // etc...
}
```

```java
public class Play{
    public static void main(String[] args){
        System.out.println("Inizio");
        Counter c = new Counter();
        System.out.println("Inizio");
    }
}
```


---
## Non Static Initializer

Anche questo non si usa praticamente mai.

```java
class Counter{
    {
        //codice che viene eseguito sempre appena crei un nuovo oggetto counter
        //Molto simile al costruttore (ma viene eseguito prima)
        System.out.println("classe Counter caricata!");
    }
    static int i; 

    private int countValue;
    // etc...
}
```


---
## Reflection API

Ogni volta che carichiamo le classi (i .class) in memoria java crea un oggetto `java.lang.Class` (cioe' quindi uno per classe caricata, non per oggetto creato).
Ogni oggetto istanziato di una classe terra' un riferimento anche all'oggetto Class della sua classe.
A questo oggetto Class gli puoi chiedere cose SULLE CLASSI (`oggetti riflessivi`) tipo quanti metodi ha, usa quel metodo che inizia con la i (wow) etc...
Una volta che hai caricato una classe in memoria non puo' essere 'scaricata'


```java
public class Play{
    public static void main(String[] args){
        Counter counter = new Counter();
        var c = counter.getClass(); //ecco, ora in c ho il riferimento al Class dei Counter
        //ora su c posso chiamare un sacco di metodi interessanti
        System.out.println(c.getName());
        System.out.println(c.getCanonicalName());
        System.out.println(c.getPackageName());
        System.out.println(c.isInterface());
        System.out.println(Arrays.deepToString(c.getMethods()));
    }
}
```

Come per le classi c'e' anche la stessa cosa per i metodi: `java.lang.reflect.Method`

Invece di `counter.getClass();` puoi fare anche `Class<?> c = new Class.counter;` oppure `c = Class.forName("aula.a14.Counter);` (attenzione se usi quest'ultimo devi anche handlare il checked error che puo' uscire se non esiste una classe con quel nome)

ESEMPIO CARINO

```java
public class DynamicExecution {

    private static final String Q_CLASS = "Insert fully-qualified class name: ";
    private static final String Q_METH = "Insert name of method to call: ";
    private static final String L_OK = "Everything was ok! The result is.. ";
    private static final String E_RET = "Wrong return type";

    public static void main (String[] s) throws Exception{
        while (true){
            System.out.println(Q_CLASS);
            final String className = System.console().readLine();
            System.out.println(Q_METH);
            final String methName = System.console().readLine();
            final Class <?> cl = Class.forName(className); // Ottiene la classe
            final Constructor <?> cns = cl.getConstructor(); // Ottiene il costruttore
            final Method met = cl.getMethod(methName); // Ottiene il metodo
            if (!met.getReturnType().isAssignableFrom(String.class)){
                throw new NoSuchMethodException(E_RET); // Il metodo deve tornare String
            }
            final Object o = cns.newInstance(); // Istanzia l ’ oggetto
            final String result = (String) met.invoke(o); // Chiama il metodo
            System.out.println(L_OK);
            System.out.println(result);
            System.out.println();
        }
    }
}
```
Prova a runnarlo con java.utils.Date


Con la reflection puoi spostare un .class nel folder del classpath e usarlo nella tua applicazione senza fermarla (cioe' mentre tu sposti il file nel folder intanto il programma sta gia' runnando) (ad esempio nel gioco carichi una skin mentre stai gia' giocando).
Spesso non si carica un unico file .class ma piu' insieme contemporaneamente -> file `.jar` (raccolta di .class)


---
## Annotazioni

Hanno evitato di aggiungere keyword per problemi di retrocmpatibilita' (se in passato avevi scritto un metodo che si chiamava Override non compila piu')

```java
class MyClass extends SuperClass {
    //...
    @Override // sta in java.lang e ti dice che sovrascrive un metodo di SuperClass
    public void myMethod (...) {...}
}
```

Il compilatore, in questo caso, guarda se override e' usato bene (se davvero si sovrascrive un metodo)
(Quindi aggiunge dei controlli aggiuntivi e rende i programmi piu' espressivi e previene errori)

Anche tu puoi dichiarare le tue annotazioni.


In generale i warning (giallo) vanno eliminati, se sei sicuro comunque di quello che hai fatto e non hai altro modo, esiste `@SuppressWarning("unchecked");`


---
## JUnit

```java 
package it.unibo.apice.oop.p14reflection.classes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // import static dice che quando chiami un metodo statico della classe Assertions basta scrivere il nome del metodo statico senza dover precedere con tutto il package

public class CounterTest{

    @Test
    public void test1 (){
        Counter c = new Counter();
        c.increment();
        c.increment();
        assertTrue(c.getValue() == 2, "Increment does not work wrt getValue");
    }

    @Test
    public void test2() {
        Counter c = new Counter();
        assertTrue(c.getValue() == 0, "getValue is not initially zero");
    }
}
```

Puoi creare classi (con lo stesso nome dalle classi da testare) per fare test
Ricorda che nei progetti gradle hai /src/main ma anche `/src/test`
*Meglio testing prima che debug dopo*

Queste classi hanno diversi test case (tipo i collaudi degli oggetti reali)

Ogni test dovrebbe essere corto e testare una cosa sola


---
### Altro 

```java
public String metodo(int a1, int a2, String... stringhe){
    //cose...
    //stringhe e' trattato come un array
}
```
Vuol dire che dopo i primi due int puoi passare quante stringhe ti pare

