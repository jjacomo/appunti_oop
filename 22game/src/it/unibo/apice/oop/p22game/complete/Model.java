package it.unibo.apice.oop.p22game.complete;

public interface Model {

    interface Vector2D {
        double x();
        double y();
    }

    enum Movement {
        LEFT, RIGHT;
    }

    void update(long time);

    Vector2D getShipPosition();

    Iterable<Vector2D> getProjectilesPosition();

    Iterable<Vector2D> getEnemiesPosition();

    void move(Movement movement);

    void fire();
    
    boolean hasWon();

    boolean hasLost();
}
