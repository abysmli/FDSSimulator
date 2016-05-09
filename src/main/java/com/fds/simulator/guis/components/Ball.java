package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class Ball extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private boolean state;

    public Ball(int x, int y, int width, int height, double pipeWidthPorcentage, String name) {
        super(x, y, width, height, pipeWidthPorcentage);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);

        if (state) {
            g.setColor(Color.black);
            g.drawRect(0, 0, this.getWidthComponent(), this.getWidthComponent());
            g.setColor(Color.green);
            g.fillRect(1, 1, this.getWidthComponent() - 1, this.getWidthComponent() - 1);
        } else {
            g.setColor(Color.black);
            g.drawRect(0, 0, this.getWidthComponent(), this.getWidthComponent());
            g.setColor(Color.gray);
            g.fillRect(1, 1, this.getWidthComponent() - 1, this.getWidthComponent() - 1);
        }

    }

    /**
     *
     * @param state: true means pump on and false means pump off.
     */
    public void setBallState(boolean state) {
        this.state = state;
        repaint();
    }
}
