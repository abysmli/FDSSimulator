package simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class AirPressuremeter extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    // private JButton buttonPump;
    private String name;
    private double pressure;

    public AirPressuremeter(int x, int y, int width, int height, double pipeWidthPorcentage, String name) {
        super(x, y, width, height, pipeWidthPorcentage);
        this.pressure = 0.0;
        this.name = name;
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);

        g.setColor(Color.cyan);
        g.fillOval(100, 0, 60 + 1, 60 + 1);

        g.setColor(Color.black);
        g.drawOval(100, 0, 60, 60);

        g.drawLine(60 / 4 + 100, 60 / 4, 60 - 15 + 100, 60 - 15);
        g.drawLine(60 / 4 + 100, 60 / 4, 60 / 2 + 100, 60 / 3);
        g.drawLine(60 / 4 + 100, 60 / 4, 60 / 3 + 100, 60 / 2);
        // label
        g.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        g.setColor(new Color(51, 51, 51));
        g.drawString(this.name, (int) (18), 30);
        g.drawString(String.valueOf((double) Math.round(this.pressure * 10) / 10), (int) (18 + 30), 44);
    }

    /**
     *
     * @param pressure
     */
    public void setPressure(double pressure) {
        this.pressure = pressure;
        repaint();
    }

    public double getPressure() {
        return this.pressure;
    }

}
