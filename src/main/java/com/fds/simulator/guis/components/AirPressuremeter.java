package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class AirPressuremeter extends SimulationComponent {
	private static final long serialVersionUID = 1L;
	// private JButton buttonPump;
	private String name;
	private double pressure;

	public AirPressuremeter(int x, int y, int width, int height, double pipeWidthPorcentage, String name) {
		super(x, y, width, height, pipeWidthPorcentage);
		this.pressure = 0.0;

		this.name = name;

		this.setToolTipText("AirPressure");
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.cyan);
		g.fillOval(100, 0, 60 + 1, 60 + 1);

		g.setColor(Color.black);
		g.drawOval(100, 0, 60, 60);

		g.drawLine(60 / 4 + 100, 60 / 4, 60 - 15 + 100, 60 - 15);
		g.drawLine(60 / 4 + 100, 60 / 4, 60 / 2 + 100, 60 / 3);
		g.drawLine(60 / 4 + 100, 60 / 4, 60 / 3 + 100, 60 / 2);
		// label
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.setColor(new Color(51, 51, 51));
		g.drawString(this.name, (int) (18), 30);
		g.drawString(String.valueOf(this.pressure), (int) (18 + 30), 44);
	}

	/**
	 * 
	 * @param state:
	 *            true means pump on and false means pump off.
	 */
	public void setPressureRate(double rate) {
		this.pressure = rate;
		repaint();
	}

	public double getPressureRate() {
		return this.pressure;
	}

}
