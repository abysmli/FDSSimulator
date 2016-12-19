package simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ProportionalValve extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private final int widthPipe;
    private final String name;
    private double aperturePercentage;

    public ProportionalValve(int x, int y, int width, int height, double pipeWidthPorcentage, int widthPipe, String name) {
        super(x, y, width, height, pipeWidthPorcentage);
        this.widthPipe = widthPipe;
        this.name = name;
        this.aperturePercentage = 0.0;
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.setColor(new Color(204, 229, 255));
        g.fillRect(0, 0, this.getWidthComponent(), this.getHeightComponent());

        g.setColor(new Color(40, 68, 255));
        double m = (double) (this.widthPipe) / (double) (this.getWidthComponent());
        for (int i = 0; i < this.getWidthComponent(); i++) {
            g.drawLine(i, (int) (m * i + this.widthPipe), i, (int) (-m * i + 2 * this.widthPipe));
        }

        g.setColor(Color.black);
        g.drawLine(0, this.widthPipe, this.getWidthComponent(), 2 * this.widthPipe); // the first line from top-left to bottom-right
        g.drawLine(0, 2 * this.widthPipe, this.getWidthComponent(), this.widthPipe);  // the second line from bottom-left to top-right
        g.drawLine(this.getWidthComponent() / 2, (int) (1.5 * this.widthPipe), this.getWidthComponent() / 2, (int) (0.5 * this.widthPipe)); // the third line from center to top (below arc)
        g.drawLine((int) ((0.5 * this.getWidthComponent()) - (0.5 * this.widthPipe)), (int) (0.5 * this.widthPipe),
                (int) ((0.5 * this.getWidthComponent()) + (0.5 * this.widthPipe)), (int) (0.5 * this.widthPipe)); // horizontal line below the arc
        g.drawLine(0, this.widthPipe, 0, 2 * this.widthPipe); // line from top-left to bottom-left
        g.drawLine(this.getWidthComponent(), this.widthPipe, this.getWidthComponent(), 2 * this.widthPipe); // line from top-right to bottom-right
        g.drawArc((int) ((0.5 * this.getWidthComponent()) - (0.5 * this.widthPipe)), 0,
                this.widthPipe, this.widthPipe, 180, -180);

        // label
        g.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        g.setColor(new Color(51, 51, 51));
        g.drawString(name, (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2), this.getWidthComponent() - 12 + (int) (0.5 * this.widthPipe));
        g.drawString(String.valueOf(this.aperturePercentage), (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(String.valueOf(this.aperturePercentage)) / 2), this.getWidthComponent() + 3 + (int) (0.5 * this.widthPipe));
    }

    public void setAperturePercentage(double aperturePercentage) {
        this.aperturePercentage = aperturePercentage;
        repaint();
    }
}
