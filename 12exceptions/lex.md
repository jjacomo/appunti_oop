# INTRO

1. Errori a run-time e necessita' di una loro gestione;
2. Tipi di eccezioni/errori in Java;
3. Istruzione throw;
4. Costrutto try-catch-finally;
5. Dichiarazioni throws;

### Errori compile time

* sono quelli piu' grossolani, sono intercettati dal compilatore;
* quindi rientrano nella fase dell’implementazione, sono innocui;
* un linguaggio con strong typing consente di identificarne molti a compile-time;
* editor moderni spesso li rendono “edit-time”;
* non sono considerabili “bug”, ma situazioni temporanee nello sviluppo.

### Errori a tempo di esecuzione (run-time)

* sono condizioni anomale dovute alla dinamica del sistema;
    ▶ parametri anomali a funzioni, errori nell’uso delle risorse di sistema,..
* in genere `e possibile:
    (i) identificare/descrivere dove potrebbero accadere, 
    (ii) intercettarli e 
    (iii) gestirli prevedendo procedure di compensazione (rimedio al problema che le ha causate);
* alcuni linguaggi (come Java/C#, non il C) forniscono costrutti di alto livello per agevolarne la gestione
* nella programmazione moderna li si cerca di associare a bug

---

* Gli errori a run-time in Java sono rappresentati da oggetti della classe `java.lang.Throwable`, vengono “lanciati”:
    1. da esplicite istruzioni del tipo: `throw <exception-object>`;
    2. o, direttamente dalla JVM per cause legate al “sistema operativo”
* Tali oggetti portano informazioni utili a capire la causa dell’errore
* Si puo' dichiarare se un metodo potra' lanciare una eccezione:
```java
metodo() throws classeDiUnEccezione{..}
```

* Si pu`o intercettare una eccezione e porvi rimedio:
```java
try{ <instructions> } catch(classeDiUnEccezione <var>){...}
```


---


## Tipi di errori

















