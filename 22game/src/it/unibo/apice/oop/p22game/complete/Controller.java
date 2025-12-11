package it.unibo.apice.oop.p22game.complete;

public interface Controller {

    enum UserInput {
        LEFT, RIGHT, FIRE;
    }

    void processInput(UserInput input);
}
