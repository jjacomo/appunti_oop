package it.unibo.apice.oop.p22game.complete;

import java.util.HashSet;
import java.util.Map;
import javax.swing.*;

import it.unibo.apice.oop.p22game.complete.Model.Vector2D;

import static it.unibo.apice.oop.p22game.complete.Controller.UserInput.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SwingView implements View {
    private static final Map<Integer, Controller.UserInput> KEY_MAPPER =
        Map.of(KeyEvent.VK_SPACE, FIRE, KeyEvent.VK_LEFT, LEFT, KeyEvent.VK_RIGHT, RIGHT);

    private Controller controller;
    private DrawingPanel drawingPanel = new DrawingPanel();
    private final JFrame frame = new JFrame();
    private boolean started = false;

    @Override
    public void start() {
        if (this.started){
            throw new IllegalStateException();
        }
        this.started = true;
        frame.setResizable(false);
        frame.setSize(DrawingPanel.DIMENSION);
        frame.getContentPane().add(drawingPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
    public void show(Iterable<Vector2D> projectiles, Iterable<Vector2D> enemies, Vector2D ship) {
        SwingUtilities.invokeLater(() -> {
            drawingPanel.projectiles = projectiles;
            drawingPanel.enemies = enemies;
            drawingPanel.ship = ship;
            drawingPanel.repaint();
        });
    }

    @Override
    public void won() {
        JOptionPane.showMessageDialog(frame, "You won!");
        System.exit(0);
    }

    @Override
    public void lost() {
        JOptionPane.showMessageDialog(frame, "You lost!");
        System.exit(0);
    }

    private class DrawingPanel extends JPanel {
        static final Dimension DIMENSION = new Dimension(1000, 1000);
        Iterable<Vector2D> projectiles = new HashSet<>();
        Iterable<Vector2D> enemies = new HashSet<>();
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
            projectiles.forEach(p -> g.drawLine((int) p.x(), (int) p.y() + 10, (int) p.x(), (int) p.y()));
            enemies.forEach(p -> g.drawOval((int) p.x() - 15, (int) p.y() - 15, 30, 30));
        }
    }
}
