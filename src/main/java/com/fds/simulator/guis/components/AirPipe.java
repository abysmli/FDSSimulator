package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class AirPipe extends SimulationComponent {
	private static final long serialVersionUID = 1L;
	// private Tank upperTank;
	private AirPump airPump;
	private Valve valve;
	private boolean lastPipeSectionBlue;
	private Timer timer;
	private int delay;
	private int barCurrentPosition;
	private int barWidth;
	private int barDisplacement;
	private int numberOfBars;
	private int timeindex = 0;

	public AirPipe(Valve valve, AirPump airPump) {
		super(valve.getInputX(), airPump.getInputY(), 200, 200, 0);
		this.valve = valve;
		this.airPump = airPump;
		this.lastPipeSectionBlue = false;
		this.delay = 30;
		this.barCurrentPosition = 0;
		this.barWidth = 5;
		this.barDisplacement = 5;
		this.numberOfBars = 35;
		ActionListener timerListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				barCurrentPosition += barDisplacement;
				timeindex = timeindex + 5;
				repaint();
			}
		};
		this.timer = new Timer(this.delay, timerListener);
	}

	@Override
	protected void paintComponent(Graphics g) {
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
			// g.fillRect(1, 1, 3 * this.upperTank.getPipeWidth(),
			// this.upperTank.getPipeWidth()-1);

			g.setColor(Color.orange);
			int d = this.getWidthComponent();
			int distanceBetweenBars = 20;
			for (int i = 0; i < this.numberOfBars; i++) {
				if (timeindex - (i * distanceBetweenBars) <= d) {
					g.fillRect(1, this.timeindex - (i * distanceBetweenBars), this.valve.getPipeWidth() - 1,
							this.barWidth);
				} else {
					// g.fillRect(timeindex - (i * distanceBetweenBars) - d +
					// this.valve.getPipeWidth(), this.getHeightComponent() -
					// this.valve.getPipeWidth() + 1,
					// this.barWidth, this.upperTank.getPipeWidth() - 1);
					if (timeindex >= this.getHeightComponent() - d) {
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
			timer.start();
		} else {
			timer.stop();
		}

		repaint();
	}
}
