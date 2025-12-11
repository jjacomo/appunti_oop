package it.unibo.apice.oop.p22game.first;

public class ModelImpl implements Model {

    private static record V2D(double x, double y) implements Vector2D {}
    private final static double ENV_X = 1000.0;
    private final static double ENV_Y = 1000.0;
    private final static double ITEM_SIZE = 30.0;
    private final static double SHIP_VELOCITY = 5e-7;

    private Vector2D ship;
    private Movement movement = Movement.LEFT;
    
    public ModelImpl() {
        this.ship = new V2D(ENV_X / 2, ENV_Y - ITEM_SIZE * 2);
    }

    @Override
    public void update(long time) {
        this.ship = new V2D(fitIntoRange(
                    ship.x() + SHIP_VELOCITY * time * (this.movement == Movement.LEFT ? -1 : +1),
                    ITEM_SIZE,
                    ENV_X - ITEM_SIZE),
                ship.y());
    }

    @Override
    public void move(Movement movement) {
        this.movement = movement;
    }

    @Override
    public Vector2D getShipPosition() {
        return this.ship;
    }

    private static double fitIntoRange(double val, double min, double max){
        return Math.min(Math.max(val, min), max);
    }
}
