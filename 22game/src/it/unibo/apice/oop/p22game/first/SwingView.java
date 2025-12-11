package it.unibo.apice.oop.p22game.first;

import java.util.Map;
import javax.swing.*;

import it.unibo.apice.oop.p22game.first.Model.Vector2D;

import static it.unibo.apice.oop.p22game.first.Controller.UserInput.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SwingView implements View {
    private static final Map<Integer, Controller.UserInput> KEY_MAPPER =
        Map.of(KeyEvent.VK_LEFT, LEFT, KeyEvent.VK_RIGHT, RIGHT);

    private Controller controller;
    private DrawingPanel drawingPanel = new DrawingPanel();
    private final JFrame frame = new JFrame();

    @Override
    public void start() {
        frame.setResizable(false);
        frame.setSize(DrawingPanel.DIMENSION);
        frame.getContentPane().add(drawingPanel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (KEY_MAPPER.containsKey(e.getKeyCode())){
                    controller.processInput(KEY_MAPPER.get(e.getKeyCode()));
                }
            }
        });
        frame.setVisible(true);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void show(Vector2D ship) {
        SwingUtilities.invokeLater(() -> {
            drawingPanel.ship = ship;
            drawingPanel.repaint();
        });
    }

    private class DrawingPanel extends JPanel {
        static final Dimension DIMENSION = new Dimension(1000, 1000);
        Vector2D ship = null;
        
        DrawingPanel(){
            this.setSize(DIMENSION);
        }
        
        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            if (ship != null) {
                g.drawPolyline(
                    new int[]{(int)ship.x() - 20, (int)ship.x() + 20, (int)ship.x(), (int)ship.x() - 20}, 
                    new int[]{(int)ship.y(), (int)ship.y(), (int)ship.y() - 30, (int)ship.y()}, 4);
            }
        }
    }
}
