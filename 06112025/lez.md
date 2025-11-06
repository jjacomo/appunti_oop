# GUI

Per fare desktop applications

Useremo `JavaSwing` che e' gia' nel JDK (in lab vedremo `javaFX`)

Write once run everywhere?

Con le librerie grafiche (una volta lo era) e' difficile (ogni window manager ha la sua api).
Hanno aspetti diversi per ogni sistema operativo (perche' ogni os ha il suo pulsante etc...).
Allora si e' deciso di disegnare tutto pixel per pixel (cosi' e' uguale ovunque).

In queste librerie si fa un uso massiccio di ereditarieta'.
Le classi di swing iniziano con la J.
In AWT (che viene prima di swing) ci sono le stesse classi senza J.

Nel package
`javax.swing` la x dovrebbe significare che e' un package opzionale (serve solo per la grafica) e quindi magari potresti non includerlo in una versione del JDK.

---

### JFrame
E' la finestra con la cornice su in alto (che e' decisa dal sistema operativo quella).

### JPanel
Sono dei riquadri dentro la finestra

### JComponent
Slider e altro...

### JDialog
E' una finestrella che ti da un messaggio di warning tipicamente o di confirmation


```java
import javax.swing.JFrame;

public class Components{
    public static void main(String[] args){
        JFrame frame = new JFrame();
        //adesso lo ho solo creato, non displaya niente
        
        //se la diplayassi sarebbe piccina piccina perche' non ho specificato la dimensione

        // ovviamente non cosi'
        frame.setSize(1000, 1000); // in pixel

        frame.setVisible(true);

    }
}
```

JFrame -> JPanel -> JButton

Devo creare JFrame poi dentro (al suo content pane) un JPanel e dentro al Jpanel il JButton.

```java

import javax.swing.*;

public class TrySwing {
    public static void main(String[] args){
        // Creo il frame e imposto titolo e altre proprietà
        final JFrame frame = new JFrame();	   
        frame.setTitle("Prova di JFrame"); 
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(320,240); // dimensioni assolute NO

        // Creo un pannello e gli imposto il bordino
        final JPanel panel = new JPanel();
        // Aggiungo il pannello ai 'contenuti' del frame 
        frame.getContentPane().add(panel);

        // Aggiungo un pulsante al pannello
        panel.add(new JButton("Prova di pulsante"));

        // Alla fine rendo visible il JFrame
        frame.setVisible(true);
    }
}
```

In generale quando crei un pane non vuoi dargli un layout assoluto ma dinamico e resizable a seconda della grandezza del tuo schermo. E poi la voglio responsive quando ingrandisco e rimpicciolisco la finestra.

=> 
## layout

### BorderLayout
```java

import java.awt.*;
import javax.swing.*;

public class UseBorderLayout3{
    public static void main(String[] args){

        // e' il layout manager (come il pattern strategy degli iterator)
        final BorderLayout b = new BorderLayout(); 
        // ce ne sono diversi. Questo qui al massimo gestisce 5 componenti puoi metterne anche meno
        // e sono: center, north, south, east, west.
        // north: larghezza=tutta la window; altezza=la sua preferita (sceglie lei)
        // south: uguale al north ma in basso
        // east: altezza quella che rimane tra north e south; larghezza: la sua(?)
        // west: come east
        // center: quello che rimane dopo tutti gli altri

        b.setHgap(10); // Parametri addizionali del lay-man
        b.setVgap(10);
        final MyFrame frame = new MyFrame("BorderLayout Example Bis",b);	   

        final JButton button = new JButton("North");
        final Dimension d = button.getPreferredSize(); // imposto le dim..
        button.setPreferredSize(new Dimension(d.width,d.height*2));
        frame.getMainPanel().add(button,BorderLayout.NORTH);
        frame.getMainPanel().add(new JButton("South"),BorderLayout.SOUTH);
        frame.getMainPanel().add(new JButton("Center"),BorderLayout.CENTER);
        frame.getMainPanel().add(new JButton("East"),BorderLayout.EAST);
        // nota l'effetto di una stringa più lunga qui
        frame.getMainPanel().add(new JButton("WestWest"),BorderLayout.WEST);

        frame.setVisible(true);
    }
}

```



Quando crei un tuo frame conviene quasi sempre fare una extend della classe JFrame nel costruttore ci metto tutte le conf del mio frame

```java
public class gameMenu extends JFrame{
    // ...
}

```


### FlowLayout

lascia a tutti le loro dimensioni preferite

### c'e pure il Grid Layout
Fa una scacchiera praticamente

FlowLayout e BorderLayout li useresti in modo combinato per ottenere robe belle


## Separazione UI - Logica dell'applicazione

E' fondamentale tenerle separate (sono concetti diversi).
Stanno in moduli diversi.

Inoltre si possono creare diverse interfacce grafiche per una stessa applicazione!!!
Pensa alla stessa applicazione che deve girare su un computer e su uno smartphone o su una tv!!!
Completamente diverse.
=> Ecco perche' si usano proprio le interfaces di java!
```java 
public interface UserInterface{
    void show();
    void SetDimension(int x, int y);
}

// e poi puoi fare tante implementazioni diverse 
// anche usando api diverse (swing, javaFX ...)
```

Capisci che hai separato bene i due moduli quando nel modulo della logica non importi javax.swing (cioe' dove fai la logica non c'e' niente di grafica)

---

## Organizzazione MVC

E' consigliato (non e' sempre usato comunque) separare l'applicazione in 3 moduli: 
* view: parla col controller
* controller: intermediario tra view e modem
* modem: parla col controller (logica)


--- 

## Event listners

DOMANI


