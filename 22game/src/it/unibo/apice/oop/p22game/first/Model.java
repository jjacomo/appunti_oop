package it.unibo.apice.oop.p22game.first;

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

    void move(Movement movement);

}
