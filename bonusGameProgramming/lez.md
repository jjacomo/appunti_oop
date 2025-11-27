# Ciao 17:05 27/11/2025

File sorgenti:
https://bitbucket.org/aricci303/game-as-a-lab

Nystrom game programming patterns


Faremo gioco semplice (roll a ball) ma usando un architettura e patterns esemplare.

## patterns

* Game Loop
* Command
* Observer
* Event Queue
* Component


### Game Loop

```java
while (true) {
    processInput() //input da tastiera (serviranno code)
    update() // model
    render() // visualizza
}
```

Deve essere collegato al tempo fisico, non al tempo della macchina: lo stesso gioco deve andare alla stessa velocita' in una macchina dimmerd e una buonissima (in quella lenta ci saranno meno FPS)

```java
double lastTime = getCurrentTime()
while (true) {
    double current = getCurrentTime();
    double elapsed = current - lastTime;
    processInput();
    update(elapsed);
    render();
    lastTime = current;
}
```


### Start Modelling the Game

Pensa object oriented, come modelleresti questo gioco?
Beh mi serve una classe BallObj, pickUpObj che possono ereditare da GameObject;
Poi classe World che racchiude tutto.

Poi Pattern Model View Controller.


GameObj ha campi Point2D pos; e Vect2D vel; Setters e getters. Poi ha metodo updateState
queste due classi Point2D e Vect2D vanno nei package `rollball.common`.

BallObj e pickUpObj sono due estensioni di GameObj.

World e' un insieme di pickUpObjs e un BallObj.
Ha un metodo updateState che si occupa di aggiornare tutti i suoi elementi.
Ha un metodo getAllObjects che da una lista di GameObjs

La parte grafica prende questa lista e disegna tutto in Swing.


Nel package core ho la mia classe GameEngine che si occupa di setuppare creando il mondo con i suoi oggetti (piazza la palla e i pickupObjects) e poi di fare partire il main loop


### INPUT e il COMMAND pattern

25 volte al secondo dovrei calcolarli, quando arrivano li catturo li metto in un buffer e li processo quando faccio girare

Command pattern: rappresenta l'input a oggetti.
Voglio spostare in alto la palla. Allora Avro' un oggetto CommandMoveUp
Tutti questi comandi devono implementare l'interfaccia Command che implementa il metodo execute(world)

Chi processa l'input inizialmente? La view: E' in swing (nei framework grafici) che hai i metodi per catturare gli eventi.
Quindi la parte di View deve rilevare gli input.
A questo punto pero' deve essere associato un associazione tra tasto premuto e oggetto passato al controller.
Ad esempio premo freccia in su e la mia view passa al controller il command MoveUp


Nella view, nel pannello della scena aggiungo un KeyListener (metodo di swing)

Il controller (GameEngine) ha un metodo NotifyCommand che accetta un comando e lo mette in una coda
Sempre nel GameEngine il metodo processInput adesso pesca dalla coda dei comandi e li esegue.


### Collisioni

Si introducono i bounding box (hitbox) che ogni oggetto ha e rilevi le collisioni tra queste
Ogni GameObj adesso ha anche il campo bounding box che e' un oggetto boundingBox.
In UpdateState, dopo aver aggiornato la posizione della palla faccio anche CheckCollisions. 



### Game State + Events

Applichiamo l'observer pattern.
in CheckBoundaries voglio solo rilevare l'evento, dopo voglio fare un azione in base a che evento e' accaduto.

nell'update state del mondo, in CheckBoundaries ti dice anche che tipo di eventi che sono stati generati.
Infatti ci sono nuove classi che esentdono WorldEvent come HitBorderEvent, HitObjEvent.

Il GameEngine diventa anche il WorldEventListener (oltre che Controller).
QUando viene generato l'evento non viene eseguito subito (non e' sincrono) ma viene aggiunto a una lista di eventi (vengono processati in modo asincrono). 
Tutta sta roba serve a rendere il tutto multithread.


### Pattern Component

Io ho la mia palla che estende GameObj.
What if fossero tutti GameObj solo che ogni game object avra' la parte che dice come deve essere disegnato, come deve muoversi, come deve essere gestito l'input....
Qundi la palla diventa un GameObj che si Decora di ...

Qui entrano in gioco le Factory... CI sara' una fabbrica di Ball che parte da un GameObj e definisce tutte le sue parti.
Poi ci sara' una fabbrica di pickupObj.

Step 5 (non introduce niente di nuove feature ma applica questo pattern)


### Game over

Si basa su GameState.
Stabiliamo che il gioco si deve fermare quando i pickupObj di GameState sono 0.

### Aggiungere roba

Ora posso ad esempio creare un bot (semplice visto che e' tutto separato).
Basta cambiare l'InputComponent della palla

