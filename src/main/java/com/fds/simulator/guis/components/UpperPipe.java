package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class UpperPipe extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private final Tank upperTank;
    private boolean lastPipeSectionBlue;
    private final Valve valve;
    private final Timer timer;
    private final int delay;
    private int barCurrentPosition;
    private final int barWidth;
    private int barDisplacement;
    private final int numberOfBars;

    private int timeindex = 0;

    public UpperPipe(Tank upperTank, Tank lowerTank, Valve valve) {
        super(upperTank.getOutputX() - upperTank.getPipeRadius(), upperTank.getOutputY(),
                lowerTank.getInputX() - (upperTank.getOutputX() - upperTank.getPipeRadius()),
                (lowerTank.getInputY() + lowerTank.getPipeRadius()) - upperTank.getOutputY(),
                upperTank.getPipeRadiusPercentage());
        this.upperTank = upperTank;
        this.lastPipeSectionBlue = false;
        this.valve = valve;
        this.delay = 30;

        this.barWidth = 5;
        this.barDisplacement = 5;
        this.numberOfBars = 35;

        ActionListener timerListener = (ActionEvent e) -> {
            // TODO Auto-generated method stub
            barCurrentPosition += barDisplacement;
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
        g.drawLine(0, 0, 0, this.getHeightComponent());
        g.drawLine(0, this.getHeightComponent(), this.getWidthComponent(), this.getHeightComponent());
        g.drawLine(this.upperTank.getPipeWidth(), 0, this.upperTank.getPipeWidth(),
                this.getHeightComponent() - this.upperTank.getPipeWidth());
        g.drawLine(this.upperTank.getPipeWidth(), this.getHeightComponent() - this.upperTank.getPipeWidth(),
                this.getWidthComponent(), this.getHeightComponent() - this.upperTank.getPipeWidth());
        int xJoint = this.valve.getXCoordinate() - this.getXCoordinate();
        g.setColor(Color.blue);
        g.fillRect(1, 0, this.upperTank.getPipeWidth() - 1, this.getHeightComponent());
        g.fillRect(1, this.getHeightComponent() - this.upperTank.getPipeWidth() + 1, xJoint,
                this.upperTank.getPipeWidth() - 1);
        if (lastPipeSectionBlue) {

            g.fillRect(xJoint, this.getHeightComponent() - this.upperTank.getPipeWidth() + 1,
                    this.getWidthComponent() - xJoint + 1, this.upperTank.getPipeWidth() - 1);

            g.setColor(Color.cyan);
            int d = this.getHeightComponent() - this.upperTank.getPipeWidth();
            int distanceBetweenBars = 20;
            for (int i = 0; i < this.numberOfBars; i++) {
                if (timeindex - (i * distanceBetweenBars) <= d) {

                    g.fillRect(1, this.timeindex - (i * distanceBetweenBars), this.upperTank.getPipeWidth() - 1,
                            this.barWidth);
                    // barCurrentPosition += barDisplacement;

                } else {
                    g.fillRect(timeindex - (i * distanceBetweenBars) - d + this.upperTank.getPipeWidth(),
                            this.getHeightComponent() - this.upperTank.getPipeWidth() + 1, this.barWidth,
                            this.upperTank.getPipeWidth() - 1);
                    if (timeindex >= this.getWidthComponent() + d) {
                        this.timeindex = 0;

                    }
                }
            }

        }
    }

    public void setLastPipeSectionBlue(boolean state) {
        this.lastPipeSectionBlue = state;

        if (state) {
            this.barCurrentPosition = 0;
            this.timer.start();
        } else {
            this.timer.stop();
        }

        repaint();
    }
}
