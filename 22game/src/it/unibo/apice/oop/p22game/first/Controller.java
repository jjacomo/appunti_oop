package it.unibo.apice.oop.p22game.first;

public interface Controller {

    enum UserInput {
        LEFT, RIGHT;
    }

    void processInput(UserInput input);
}
