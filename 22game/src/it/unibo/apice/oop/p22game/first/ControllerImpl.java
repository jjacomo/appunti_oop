package it.unibo.apice.oop.p22game.first;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
        public void run(){
            while (true){
                long time = System.nanoTime();
                view.show(model.getShipPosition());
                try {
                    switch (queue.poll(1, TimeUnit.MILLISECONDS)) {
                        case LEFT -> model.move(Model.Movement.LEFT);
                        case RIGHT -> model.move(Model.Movement.RIGHT);
                        default -> {}
                    }
                } catch (Exception e){}
                model.update(System.nanoTime() - time);
            }
        }
    }
}
