package it.unibo.apice.oop.p22game.complete;

import static it.unibo.apice.oop.p22game.complete.ModelImpl.Constants.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelImpl implements Model {

    private record Enemy(Vector2D position, Vector2D velocity){}

    private final List<Vector2D> projectiles;
    private final List<Enemy> enemies;
    private Vector2D ship;
    private final Physics physics = new Physics();
    
    public ModelImpl() {
        this.enemies = Stream
                .generate(() -> new Enemy(physics.newEnemyPosition(), physics.newEnemyVelocity()))
                .limit(Constants.N_ENEMIES)
                .collect(Collectors.toCollection(LinkedList::new));
        this.ship = physics.initialShip();
        this.projectiles = new LinkedList<>();
    }

    @Override
    public void update(long time) {
        this.physics.updateShip(time);
        this.physics.updateProjectiles(time);
        this.physics.updateEnemies(time);
        this.physics.handleCollisions();
    }

    @Override
    public Iterable<Vector2D> getProjectilesPosition() {
        return Collections.unmodifiableCollection(this.projectiles);
    }

    @Override
    public Iterable<Vector2D> getEnemiesPosition() {
        return this.enemies.stream().map(Enemy::position).toList();
    }

    @Override
    public void move(Movement movement) {
        this.physics.movement = movement;
    }

    @Override
    public void fire() {
        this.projectiles.add(physics.newProjectile());
    }

    @Override
    public Vector2D getShipPosition() {
        return this.ship;
    }

    @Override
    public boolean hasWon() {
        return this.enemies.isEmpty();
    }

    @Override
    public boolean hasLost() {
        return this.enemies.stream().map(Enemy::position).anyMatch(p -> p.y() > ENV_Y - ITEM_SIZE);
    }

    private class Physics {

        private static record V2D(double x, double y) implements Vector2D {
            public V2D relative(Vector2D p) {
                return new V2D(this.x + p.x(), this.y + p.y());
            }

            public V2D inRange(Vector2D dim) {
                return new V2D(Utils.fitIntoRange(this.x, 0, dim.x()), Utils.fitIntoRange(this.y, 0, dim.y()));
            }
        }

        private Movement movement = Movement.LEFT;
        private final Random random = new Random();

        public Vector2D newEnemyPosition() {
            return new V2D(Math.random() * ENV_X, ITEM_SIZE);
        }

        public Vector2D newEnemyVelocity(){
            return new V2D(
                ENEMY_X_VELOCITY * (this.random.nextBoolean() ? +1 : -1),
                ENEMY_Y_VELOCITY * Math.random());
        }

        public Vector2D initialShip() {
            return new V2D(ENV_X / 2, ENV_Y - ITEM_SIZE * 2);
        }

        public Vector2D newProjectile() {
            return new V2D(0, -2 * ITEM_SIZE).relative(ship);
        }

        public void updateProjectiles(long time) {
            Utils.updateList(projectiles, p -> new V2D(0, -time * PROJECTILE_VELOCITY * 0.5).relative(p));
            Utils.removeFromList(projectiles, p -> p.y() <= 0);
        }

        public void updateEnemies(long time) {
            Utils.updateList(enemies, e -> new Enemy(
                    new V2D(time * e.velocity().x(), time * e.velocity().y()).relative(e.position()).inRange(new V2D(ENV_X,ENV_Y)),
                    Math.random() > ENEMY_CHANGE_DIRECTION_PROBABILITY ? e.velocity() : new V2D(-e.velocity().x(), e.velocity().y())));
        }

        public void handleCollisions() {
            record PE(Vector2D projectile, Enemy enemy) {
            }
            enemies.stream()
                    .map(e -> projectiles.stream()
                            .filter(p -> hit(p, e.position()))
                            .map(p -> new PE(p, e))
                            .findAny())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList()
                    .forEach(pe -> {
                        enemies.remove(pe.enemy());
                        projectiles.remove(pe.projectile());
                    });
        }

        public boolean hit(Vector2D projectiles, Vector2D enemy) {
            return Math.abs(projectiles.x() - enemy.x()) <= ITEM_SIZE / 2
                    && Math.abs(projectiles.y() - enemy.y()) <= ITEM_SIZE / 2;
        }

        public void updateShip(long time) {
            ship = new V2D(Utils.fitIntoRange(
                    ship.x() + SHIP_VELOCITY * time * (this.movement == Movement.LEFT ? -1 : +1),
                    ITEM_SIZE,
                    ENV_X - ITEM_SIZE),
                    ship.y());
        }
    }

    public static class Constants {
        final static double ENV_X = 1000.0;
        final static double ENV_Y = 1000.0;
        final static double ITEM_SIZE = 30.0;
        final static int N_ENEMIES = 30;
        final static double PROJECTILE_VELOCITY = 1e-6;
        final static double SHIP_VELOCITY = 5e-7;
        final static double ENEMY_X_VELOCITY = 5e-7;
        final static double ENEMY_Y_VELOCITY = 5e-8;
        final static double ENEMY_CHANGE_DIRECTION_PROBABILITY = 0.02;
    }
}
