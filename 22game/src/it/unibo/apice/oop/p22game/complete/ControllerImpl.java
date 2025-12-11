package it.unibo.apice.oop.p22game.complete;

import java.util.concurrent.LinkedBlockingQueue;

public class ControllerImpl implements Controller {

    private final Model model = new ModelImpl();
    private final View view = new SwingView();
    private final LinkedBlockingQueue<UserInput> queue = new LinkedBlockingQueue<>();
    private final Loop loop = new Loop();
    
    public ControllerImpl(){
        this.view.setController(this);
        this.view.start();
    }

    public void loop(){
        this.loop.start();
    }

    @Override
    public void processInput(UserInput input) {
        this.queue.add(input);
    }

    private class Loop extends Thread {
        private static final int SLEEPING_PERIOD_IN_MILLISECONDS = 10;

        public void run(){
            while (true){
                long time = System.nanoTime();
                view.show(model.getProjectilesPosition(), model.getEnemiesPosition(), model.getShipPosition());
                if (model.hasWon()){
                    view.won();
                }
                if (model.hasLost()){
                    view.lost();
                }
                try {
                    Thread.sleep(SLEEPING_PERIOD_IN_MILLISECONDS);
                    switch (queue.poll()) {
                        case LEFT -> model.move(Model.Movement.LEFT);
                        case RIGHT -> model.move(Model.Movement.RIGHT);
                        case FIRE -> model.fire();
                        default -> {}
                    }
                } catch (Exception e){}
                model.update(System.nanoTime() - time);
            }
        }
    }
}
