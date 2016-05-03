package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class AirFlowMeter extends SimulationComponent {
	private static final long serialVersionUID = 1L;
	private double rate;
	private String name;

	public AirFlowMeter(int x, int y, int width, int height, double pipeWidthPorcentage, String name) {
		super(x, y, width, height, pipeWidthPorcentage);
		this.rate = 0.0;
		this.name = name;

		this.setToolTipText("AirFlowMeter");
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.cyan);
		g.fillOval(100, 0, 60 + 1, 60 + 1);

		g.setColor(Color.black);
		g.drawOval(100, 0, 60, 60);

		g.drawArc((int) (0.25 * 60) + 100, (int) (-0.25 * 60), (int) (0.5 * 60), (int) (0.5 * 60), 200, 145);
		g.drawArc((int) (0.25 * 60) + 100, (int) (0.75 * 60), (int) (0.5 * 60), (int) (0.5 * 60), 160, -145);
		// label
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.setColor(new Color(51, 51, 51));
		g.drawString(this.name, 18, 30);
		g.drawString(String.valueOf(this.rate), (int) (18) + 30, 40);
	}

	public void setFlowRate(double rate) {
		this.rate = rate;
		repaint();
	}

	public double getFlowRate() {
		return this.rate;
	}
}
