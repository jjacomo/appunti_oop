package it.unibo.apice.oop.p22game.complete;

public interface View {

    void setController(Controller controller);
    
    void start();
    
    void show(Iterable<Model.Vector2D> projectiles, Iterable<Model.Vector2D> enemies, Model.Vector2D ship);
    
    void won();
    
    void lost();
}
