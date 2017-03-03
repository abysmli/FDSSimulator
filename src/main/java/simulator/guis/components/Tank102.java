package simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Tank102 extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private double level;
    private String name;
    private boolean stripe;

    public Tank102(int x, int y, int width, int height, double pipeWidthPorcentage, String name) {
        super(x, y, width, height, pipeWidthPorcentage);
        this.name = name;
        this.level = 0.5;
        this.stripe = false;
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.drawLine(0, (int) (this.getHeightComponent() / 5.0), 0, (int) (this.getHeightComponent() / 3 - this.getWidthComponent() * this.getPipeRadiusPercentage()));
        g.drawLine(0, (int) (this.getHeightComponent() / 3.0 + this.getWidthComponent() * this.getPipeRadiusPercentage()), 0, getHeightComponent());
   
        g.drawLine(0, this.getHeightComponent(), (int) (this.getWidthComponent() / 2.0 - this.getWidthComponent() * this.getPipeRadiusPercentage()) - 30, this.getHeightComponent());
        g.drawLine((int) (this.getWidthComponent() / 2.0 + this.getWidthComponent() * this.getPipeRadiusPercentage()) - 30, this.getHeightComponent(), (int) (this.getWidthComponent() / 2.0 - this.getWidthComponent() * this.getPipeRadiusPercentage()), this.getHeightComponent());
        g.drawLine((int) (this.getWidthComponent() / 2.0 + this.getWidthComponent() * this.getPipeRadiusPercentage()), this.getHeightComponent(), this.getWidthComponent(), this.getHeightComponent());
        
        g.drawLine(this.getWidthComponent(), this.getHeightComponent(), this.getWidthComponent(), (int) (this.getHeightComponent() / 5.0));

        // cap
        g.drawArc(0, 25, this.getWidthComponent(), (int) (((this.getHeightComponent() / 5.0) - 25) * 2), 180, -180);

        g.setColor(Color.blue);
        // fill the tank to current level
        int waterLevel = (int) ((((2.0 / 3.0) * this.getHeightComponent()) - this.getPipeRadius() - 1) * this.level);
        g.fillRect(1, this.getHeightComponent() - waterLevel, this.getWidthComponent() - 1, waterLevel);

        if (stripe) {
            g.fillRect(1, this.getInputY() - this.getPipeRadius() - this.getYCoordinate(), 5, this.getHeightComponent() - this.getInputY() + this.getPipeRadius() + this.getYCoordinate());
        }

        // label
        g.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        g.setColor(new Color(51, 51, 51));
        g.drawString(name, (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2), 11);

        // water level
        String waterLevelDisplay = String.valueOf((double) Math.round(this.level * 100.0) / 10.0) + " L";
        g.drawString(waterLevelDisplay, (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(waterLevelDisplay) / 2), 50);

    }

    public int getInputY() {
        return (this.getHeightComponent() / 3) + this.getYCoordinate();
    }

    public int getInputX() {
        return this.getXCoordinate();
    }

    public int getOutputY() {
        return this.getHeightComponent() + this.getYCoordinate();
    }

    public int getOutputX() {
        return (this.getWidthComponent() / 2) + this.getXCoordinate();
    }

    public int getPipeWidth() {
        return (int) (this.getWidthComponent() * 2.0 * this.getPipeRadiusPercentage());
    }

    public int getPipeRadius() {
        return (int) (this.getWidthComponent() * this.getPipeRadiusPercentage());
    }

    public void setTankLevel(double level) {
        this.level = level;
        this.repaint();
        this.revalidate();
    }

    public double getTankLevel() {
        return this.level;
    }

    public void setBlueStripe(boolean stripe) {
        this.stripe = stripe;
        repaint();
    }
}
