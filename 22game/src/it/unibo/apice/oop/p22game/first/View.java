package it.unibo.apice.oop.p22game.first;

public interface View {

    void setController(Controller controller);
    
    void start();
    
    void show(Model.Vector2D ship);
}
