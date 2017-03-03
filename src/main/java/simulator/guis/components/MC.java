/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author abysmli
 */
public class MC extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private boolean state;

    public MC(int x, int y, int width, int height, double pipeWidthPorcentage) {
        super(x, y, width, height, pipeWidthPorcentage);
        this.state = false;
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.drawRoundRect(0, 0, this.getWidthComponent(), this.getHeightComponent(), 5, 5);

        if (state) {
            g.setColor(Color.green);
            g.fillRoundRect(1, 1, this.getWidthComponent() - 1, this.getHeightComponent() - 1, 2, 2);
            g.setColor(Color.black);
            g.drawString("MC: Running", (int) (this.getWidthComponent() * 0.5 - g.getFontMetrics().stringWidth("MC: Running") * 0.5),
                    (int) ((this.getHeightComponent() * 0.5) + 5.5));
        } else {
            g.setColor(Color.gray);
            g.fillRoundRect(1, 1, this.getWidthComponent() - 1, this.getHeightComponent() - 1, 2, 2);
            g.setColor(Color.black);
            g.drawString("MC: OFF", (int) (this.getWidthComponent() * 0.5 - g.getFontMetrics().stringWidth("MC: OFF") * 0.5),
                    (int) ((this.getHeightComponent() * 0.5) + 5.5));
        }
    }

    public void setMCSate(boolean state) {
        this.state = state;
        repaint();
    }
}
