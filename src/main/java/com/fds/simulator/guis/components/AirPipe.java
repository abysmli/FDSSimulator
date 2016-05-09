package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AirPipe extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private final AirPump airPump;
    private final Valve valve;
    private boolean lastPipeSectionBlue;
    private final Timer timer;
    private final int delay;
    private final int barWidth;
    private final int numberOfBars;
    private int timeindex = 0;

    public AirPipe(Valve valve, AirPump airPump) {
        super(valve.getInputX(), airPump.getInputY(), 200, 200, 0);
        this.valve = valve;
        this.airPump = airPump;
        this.lastPipeSectionBlue = false;
        this.delay = 30;
        this.barWidth = 5;
        this.numberOfBars = 100;
        ActionListener timerListener = (ActionEvent e) -> {
            timeindex = timeindex + 5;
            repaint();
        };
        this.timer = new Timer(this.delay, timerListener);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);

        g.drawLine(0, 0, 0, this.valve.getInputY());
        g.drawLine(this.valve.getPipeWidth(), this.valve.getPipeWidth(), this.valve.getPipeWidth(),
                this.valve.getInputY());
        g.drawLine(0, 0, this.airPump.getX(), 0);
        g.drawLine(this.valve.getPipeWidth(), this.valve.getPipeWidth(), this.airPump.getX(),
                this.valve.getPipeWidth());

        g.setColor(Color.cyan);
        g.fillRect(1, 1, this.valve.getPipeWidth() - 1, this.valve.getInputY() - 1);
        g.fillRect(1, 1, this.airPump.getX(), this.valve.getPipeWidth() - 1);

        if (this.lastPipeSectionBlue) {
            g.setColor(Color.orange);
            int distanceBetweenBars = 20;
            for (int i = 0; i < this.numberOfBars; i++) {
                if (timeindex - (i * distanceBetweenBars) <= this.getWidthComponent()-this.valve.getPipeWidth()) {
                    g.fillRect(this.getWidthComponent() + (i * distanceBetweenBars) - this.timeindex, 1, this.barWidth, this.valve.getPipeWidth() - 1);
                } else {
                    if (this.timeindex - (i * distanceBetweenBars)- this.getWidthComponent() > this.valve.getPipeWidth() / 2) {
                        g.fillRect(1, this.timeindex - (i * distanceBetweenBars) - this.getWidthComponent(), this.valve.getPipeWidth() - 1, this.barWidth); 
                    }
                }
            }
        }
    }

    public void setLastPipeSectionBlue(boolean state) {
        this.lastPipeSectionBlue = state;
        if (state) {
            timer.start();
        } else {
            this.timeindex = 0;
            timer.stop();
        }
        repaint();
    }
}
