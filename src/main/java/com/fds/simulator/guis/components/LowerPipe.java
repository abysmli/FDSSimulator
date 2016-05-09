package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class LowerPipe extends SimulationComponent {

    private static final long serialVersionUID = 1L;
    private final Tank upperTank;
    private boolean lastPipeSectionBlue;
    private final Timer timer;
    private final int delay;
    private int barCurrentPosition;
    private final int barWidth;
    private int barDisplacement;

    public LowerPipe(Tank upperTank, Tank lowerTank) {
        super(upperTank.getInputX() - 3 * upperTank.getPipeWidth(), upperTank.getInputY() - upperTank.getPipeRadius(),
                lowerTank.getOutputX() + lowerTank.getPipeRadius() - (upperTank.getInputX() - 3 * upperTank.getPipeWidth()),
                lowerTank.getOutputY() + 3 * upperTank.getPipeWidth() - (upperTank.getInputY() - upperTank.getPipeRadius()),
                upperTank.getPipeRadiusPercentage());
        this.upperTank = upperTank;
        this.lastPipeSectionBlue = false;

        this.delay = 30;
        this.barCurrentPosition = 0;
        this.barWidth = 5;
        this.barDisplacement = 5;

        ActionListener timerListener = (ActionEvent e) -> {
            barCurrentPosition += barDisplacement;
            repaint();
        };
        this.timer = new Timer(this.delay, timerListener);
    }

    @Override
    protected void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.drawLine(0, 0, 3 * this.upperTank.getPipeWidth(), 0);
        g.drawLine(0, 0, 0, this.getHeightComponent());
        g.drawLine(0, this.getHeightComponent(), this.getWidthComponent(), this.getHeightComponent());
        g.drawLine(this.getWidthComponent(), this.getHeightComponent(), this.getWidthComponent(), this.getHeightComponent() - 3 * this.upperTank.getPipeWidth());

        g.drawLine(this.upperTank.getPipeWidth(), this.upperTank.getPipeWidth(), 3 * this.upperTank.getPipeWidth(), this.upperTank.getPipeWidth());
        g.drawLine(this.upperTank.getPipeWidth(), this.upperTank.getPipeWidth(), this.upperTank.getPipeWidth(), this.getHeightComponent() - this.upperTank.getPipeWidth());
        g.drawLine(this.upperTank.getPipeWidth(), this.getHeightComponent() - this.upperTank.getPipeWidth(),
                this.getWidthComponent() - this.upperTank.getPipeWidth(), this.getHeightComponent() - this.upperTank.getPipeWidth());
        g.drawLine(this.getWidthComponent() - this.upperTank.getPipeWidth(), this.getHeightComponent() - this.upperTank.getPipeWidth(),
                this.getWidthComponent() - this.upperTank.getPipeWidth(), this.getHeightComponent() - 3 * this.upperTank.getPipeWidth());
        g.setColor(Color.blue);
        g.fillRect(1, this.upperTank.getPipeWidth(), this.upperTank.getPipeWidth() - 1, this.getHeightComponent() - 1);
        g.fillRect(1, this.getHeightComponent() - this.upperTank.getPipeWidth() + 1, this.getWidthComponent() - 1, this.upperTank.getPipeWidth() - 1);
        g.fillRect(this.getWidthComponent() - this.upperTank.getPipeWidth() + 1,
                this.getHeightComponent() - 3 * this.upperTank.getPipeWidth(),
                this.upperTank.getPipeWidth() - 1, 3 * this.upperTank.getPipeWidth() - 1);
        if (this.lastPipeSectionBlue) {
            g.fillRect(1, 1, 3 * this.upperTank.getPipeWidth(), this.upperTank.getPipeWidth() - 1);

            g.setColor(Color.cyan);
            int a = 2 * this.upperTank.getPipeWidth();
            int b = this.getWidthComponent() - 2 * this.upperTank.getPipeWidth();
            int c = this.getHeightComponent() - 2 * this.upperTank.getPipeWidth();
            int d = a;
            int numberOfBars = 35;
            int distanceBetweenBars = 20;

            for (int i = 0; i < numberOfBars; i++) {
                if ((this.barCurrentPosition - (i * distanceBetweenBars) <= a) && (this.barCurrentPosition - (i * distanceBetweenBars) > 0)) {

                    g.fillRect(this.getWidthComponent() - this.upperTank.getPipeWidth() + 1,
                            this.getHeightComponent() - 3 * this.upperTank.getPipeWidth() + this.barCurrentPosition - (i * distanceBetweenBars),
                            this.upperTank.getPipeWidth() - 1, this.barWidth);
                } else if (this.barCurrentPosition - (i * distanceBetweenBars) <= (a + b)) {
                    g.fillRect(this.getWidthComponent() - this.upperTank.getPipeWidth() - (this.barCurrentPosition - (i * distanceBetweenBars) - a),
                            this.getHeightComponent() - this.upperTank.getPipeWidth() + 1,
                            this.barWidth, this.upperTank.getPipeWidth() - 1);
                } else if (this.barCurrentPosition - (i * distanceBetweenBars) <= (a + b + c)) {
                    g.fillRect(1, this.getHeightComponent() - this.upperTank.getPipeWidth() - 1 - (this.barCurrentPosition - (i * distanceBetweenBars) - a - b),
                            this.upperTank.getPipeWidth() - 1, this.barWidth);
                } else {
                    g.fillRect(this.upperTank.getPipeWidth() + (this.barCurrentPosition - (i * distanceBetweenBars) - a - b - c), 1, this.barWidth, this.upperTank.getPipeWidth() - 1);
                }
                if (this.barCurrentPosition > (a + b + c + d)) {
                    this.barCurrentPosition = 0;
                }

            }
        }
    }

    public void setLastPipeSectionBlue(boolean state) {
        this.lastPipeSectionBlue = state;

        if (state) {
            this.barCurrentPosition = 0;
            timer.start();
        } else {
            timer.stop();
        }

        repaint();
    }
}
