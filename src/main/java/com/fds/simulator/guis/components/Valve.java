package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;

public class Valve extends SimulationComponent {
	private static final long serialVersionUID = 1L;
	private JButton buttonValve;
	private int widthPipe;
	private String name;
	private boolean state;

	public Valve(int x, int y, int width, int height, double pipeWidthPorcentage, int widthPipe, String name) {
		super(x, y, width, height, pipeWidthPorcentage);

		buttonValve = new JButton("Closed");
		buttonValve.setMargin(new Insets(0, 0, 0, 0));
		buttonValve.setBounds(0, 55, 60, 20);
		buttonValve.setSelected(true);
		buttonValve.setBackground(Color.gray);
		add(buttonValve);

		this.widthPipe = widthPipe;
		this.name = name;

		this.setToolTipText("Valve");
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(204, 229, 255));
		g.fillRect(0, 0, this.getWidthComponent(), this.getHeightComponent());

		g.setColor(new Color(40, 68, 255));
		double m = (double) (this.widthPipe) / (double) (this.getWidthComponent());
		for (int i = 0; i < this.getWidthComponent(); i++) {
			if (i <= (int) (0.5 * this.getWidthComponent())) {
				g.drawLine(i, (int) (m * i + 0.5 * this.widthPipe), i, (int) (-m * i + 1.5 * this.widthPipe));
			}
			if ((i > (int) (0.5 * this.getWidthComponent()))) {
				g.setColor(Color.gray);
				g.drawLine(i, (int) (m * i + 0.5 * this.widthPipe), i, (int) (-m * i + 1.5 * this.widthPipe));

			}

		}

		g.setColor(Color.black);
		g.drawLine(0, this.widthPipe / 2, this.getWidthComponent(), (int) (1.5 * this.widthPipe));
		g.drawLine(0, (int) (1.5 * this.widthPipe), this.getWidthComponent(), this.widthPipe / 2);
		// g.drawLine(this.getWidthComponent()/2, this.widthPipe,
		// this.getWidthComponent()/2, 0);
		// g.drawLine((int)((this.getWidthComponent()/2.0)-(this.widthPipe/2.0)),
		// 0,
		// (int)((this.getWidthComponent()/2.0)+(this.widthPipe/2.0)), 0);
		// g.drawRect(this.getWidthComponent()/4,(int)(this.getWidthComponent()-3.8*(this.widthPipe)),
		// (int)((this.getWidthComponent()/2.0)),
		// (int)((this.getWidthComponent()/2.0)-(this.widthPipe/2.0)));

		g.drawLine(0, this.widthPipe / 2, 0, (int) (1.5 * this.widthPipe));
		g.drawLine(this.getWidthComponent() / 2, 0, this.getWidthComponent() / 2, (int) (1.5 * this.widthPipe));
		g.drawLine(this.getWidthComponent(), this.widthPipe / 2, this.getWidthComponent(),
				(int) (1.5 * this.widthPipe));
		// g.drawRect(this.getWidthComponent()/4, 0, this.getWidthComponent()/2,
		// this.getWidthComponent()/2);
		g.drawOval((int) (this.getWidthComponent() / 3), (int) (this.getWidthComponent() / 5), this.widthPipe,
				this.widthPipe);
		g.setColor(new Color(40, 68, 255));
		g.fillOval((int) (this.getWidthComponent() / 3), (int) (this.getWidthComponent() / 5), this.widthPipe + 1,
				this.widthPipe + 1);

		// label
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.setColor(new Color(51, 51, 51));
		g.drawString(name, (int) (this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2),
				this.getWidthComponent() - 12);
	}

	/**
	 * 
	 * @param state:
	 *            true means valve open and false means valve closed.
	 */
	public void setValveState(boolean state) {
		this.state = state;
		if (state) {
			buttonValve.setText("Open");
			buttonValve.setSelected(!this.state);
			buttonValve.setBackground(Color.green);
		} else {
			buttonValve.setText("Closed");
			buttonValve.setSelected(!this.state);
			buttonValve.setBackground(Color.gray);
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
