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
public class UltrasonicSensor extends SimulationComponent {

    private static final long serialVersionUID = 1L;

    public UltrasonicSensor(int x, int y, int width, int height, double pipeWidthPorcentage) {
        super(x, y, width, height, pipeWidthPorcentage);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.drawRoundRect(0, 0, this.getWidthComponent(), this.getHeightComponent(), 5, 5);

        g.setColor(Color.yellow);
        g.fillRoundRect(1, 1, this.getWidthComponent() - 1, this.getHeightComponent() - 1, 2, 2);
        g.setColor(Color.black);
        g.drawString("U", (int) (this.getWidthComponent() * 0.5 - g.getFontMetrics().stringWidth("U") * 0.5),
                (int) ((this.getHeightComponent() * 0.5) + 5.5));

    }
}
