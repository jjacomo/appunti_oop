# INTRO

### Forme di dipendenza e riuso fra classi nell'OO
* Associazione -> Un oggetto ne usa un altro: `uses`
* Composizione -> Un oggetto ne aggrega altri: `has-a`
* Specializzazione -> Una classe ne specializza un altra: `is-a`

Qui faremo la 
## composizione

Un oggetto della classe A si compone di altri oggetti delle classi B1, B2, ... 
L'unica differenza con la composizione e' che con l'aggregazione l'oggetto che aggrega rende disponibile all'esterno il riferimento all'oggetto aggregato.

Ad esempio un oggetto Ateneo si compone di oggetti di tipo Faculty, Student, Teacher, . . . 


In UML si fa col simbolo del diamante pieno

---

## Interfacce

E' un meccanismo per separare esplicitamente, in dichiarazioni diverse, l’interfaccia della classe e la sua realizzazione.
E' un `contratto` a cui tutti devono attenersi per poter essere utilizzati da una stessa API


Dentro le interfacce puoi solo mettere dichiarazioni di metodi.
Nella sua implementazione bisogna dichiararli esplicitamente.

Esempio: dispositivi DomusController
Lamp e' una lamp ma anche un dispositivo, stessa cosa per TV, Radio, ...

```java
public interface Device {
    void switchOn();
    void switchOff();
    boolean isSwitchedOn();
}
```


In UML si denota con <<Interface>> nome e le classi che la implementano vi sono collegate con linea tratteggiata che finisce con triangolo

### ES Puoi assegnare dei tipi alle interfacce
```java
public interface Device {
    /* Su Lamp effettuo le usuali operazioni */
    Lamp lamp = new Lamp();
    lamp.switchOn();
    boolean b = lamp.isSwitchedOn();

    /* Una variabile di tipo Device pu `o contenere
un Lamp , e su essa posso eseguire le
operazioni definite da Device */
    Device dev; // creo la variabile
    dev = new Lamp(); // assegnamento ok
    dev.switchOn(); // operazione di Device
    boolean b2 = dev.isSwitchedOn(); // operazioni di Device

    Device dev2 = new Lamp(); // Altro assegnamento

    /* Attenzione , le interfacce non sono istanziabili */
    // Device dev3 = new Device () ; NO !!!!
}
```


Utilissime per riutilizzo di codice, invece di fare un metodo per ogni singolo device ne basta uno
```java
class DeviceUtilities {

    /* Grazie alle interfacce , fattorizzo in un solo metodo */

    public static void switchOnIfCurrentlyOff(Device device) {
        if (!device.isSwitchedOn()) {
            device.switchOn();
        }
    }
}
```

---

## Principio di sostituibilita'
Se la classe C implementa l’interfaccia I, allora ogni istanza di C puo` essere passata dove il programma si attende un elemento del tipo I.

"Se A e` un sottotipo di B allora ogni oggetto (o valore) di A deve essere utilizzabile dove un programma si attende un oggetto (o valore) di B"


### Polimorfismo (inclusivo)
E' precisamente l'applicazione del principio di sostituibilita'

---

### Altro

```java 
class C implements I1, I2, I3{
    //...
    // devi implementare tutti i metodi di I1, I2, I3
}
```

