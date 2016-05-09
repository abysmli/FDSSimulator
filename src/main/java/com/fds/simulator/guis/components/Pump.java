package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Pump extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private final JLabel labelPump;
    private final String name;
    private boolean state;

    public Pump(int x, int y, int width, int height, double pipeWidthPorcentage, String name) {
        super(x, y, width, height, pipeWidthPorcentage);
        labelPump = new JLabel("Off");
        labelPump.setBounds(7, 80, 45, 15);
        labelPump.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        labelPump.setHorizontalAlignment(SwingConstants.CENTER);
        labelPump.setForeground(Color.white);
        labelPump.setBackground(Color.GRAY);
        labelPump.setOpaque(true);
        add(labelPump);
        this.name = name;
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);

        g.setColor(new Color(40, 68, 255));
        g.fillOval(0, 0, this.getWidthComponent() + 1, this.getWidthComponent() + 1);

        g.setColor(Color.black);
        g.drawOval(0, 0, this.getWidthComponent(), this.getWidthComponent());

        g.drawLine(0, this.getWidthComponent() / 2, this.getWidthComponent() / 2, 0);
        g.drawLine(0, this.getWidthComponent() / 2, this.getWidthComponent() / 2, this.getWidthComponent());

        // label
        g.setFont(new Font("Ubuntu", Font.PLAIN, 12));
        g.setColor(new Color(51, 51, 51));
        g.drawString(name, (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2), 15 + this.getWidthComponent());
    }

    /**
     *
     * @param state: true means pump on and false means pump off.
     */
    public void setPumpState(boolean state) {
        this.state = state;
        if (state) {
            labelPump.setText("On");
            labelPump.setForeground(Color.darkGray);
            labelPump.setBackground(Color.green);
        } else {
            labelPump.setText("Off");
            labelPump.setForeground(Color.white);
            labelPump.setBackground(Color.gray);
        }
    }

    public boolean getPumpState() {
        return this.state;
    }

}
