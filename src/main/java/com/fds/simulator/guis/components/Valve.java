package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Valve extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private final JLabel labelValve;
    private final int widthPipe;
    private final String name;
    private boolean state;

    public Valve(int x, int y, int width, int height, double pipeWidthPorcentage, int widthPipe, String name) {
        super(x, y, width, height, pipeWidthPorcentage);
        labelValve = new JLabel("Closed");
        labelValve.setBounds(7, 55, 45, 15);
        labelValve.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        labelValve.setHorizontalAlignment(SwingConstants.CENTER);
        labelValve.setForeground(Color.white);
        labelValve.setBackground(Color.GRAY);
        labelValve.setOpaque(true);
        add(labelValve);
        this.widthPipe = widthPipe;
        this.name = name;
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
            g.drawLine(i, (int) (m * i + 0.5 * this.widthPipe), i, (int) (-m * i + 1.5 * this.widthPipe));
        }

        g.setColor(Color.black);
        g.drawLine(0, this.widthPipe / 2, this.getWidthComponent(), (int) (1.5 * this.widthPipe));
        g.drawLine(0, (int) (1.5 * this.widthPipe), this.getWidthComponent(), this.widthPipe / 2);

        g.drawLine(0, this.widthPipe / 2, 0, (int) (1.5 * this.widthPipe));
        g.drawLine(this.getWidthComponent() / 2, 0, this.getWidthComponent() / 2, (int) (1.5 * this.widthPipe));
        g.drawLine(this.getWidthComponent(), this.widthPipe / 2, this.getWidthComponent(),
                (int) (1.5 * this.widthPipe));
        g.drawOval((int) (this.getWidthComponent() / 3), (int) (this.getWidthComponent() / 5) - 2, this.widthPipe,
                this.widthPipe);
        g.setColor(new Color(40, 68, 255));

        g.fillOval((int) (this.getWidthComponent() / 3), (int) (this.getWidthComponent() / 5) - 2, this.widthPipe,
                this.widthPipe);

        // label
        g.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        g.setColor(new Color(51, 51, 51));
        g.drawString(name, (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2),
                this.getWidthComponent() - 12);
    }

    /**
     *
     * @param state: true means valve open and false means valve closed.
     */
    public void setValveState(boolean state) {
        this.state = state;
        if (state) {
            labelValve.setText("Open");
            labelValve.setForeground(Color.darkGray);
            labelValve.setBackground(Color.green);
        } else {
            labelValve.setText("Closed");
            labelValve.setForeground(Color.white);
            labelValve.setBackground(Color.gray);
        }
    }

    public boolean getValveState() {
        return this.state;
    }

    public int getInputX() {

        return (this.getWidthComponent() / 3) + this.getXCoordinate();
    }

    public int getInputY() {
        return this.getYCoordinate();
    }

    public int getPipeWidth() {
        return (int) (this.getWidthComponent() / 3);
    }

    public int getPipeRadius() {
        return (int) (this.getWidthComponent() * this.getPipeRadiusPercentage());
    }
}
