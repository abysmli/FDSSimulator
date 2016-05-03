package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Flowmeter extends SimulationComponent
{
	private static final long serialVersionUID = 1L;
	private double rate;
	private String name;
	
	public Flowmeter(int x, int y, int width, int height, double pipeWidthPorcentage, String name) 
	{
		super(x, y, width, height, pipeWidthPorcentage);
		this.rate = 0.0;
		this.name = name;
		
		this.setToolTipText("Flow Meter");
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(new Color(40, 68, 255));
		g.fillOval(0, 0, this.getWidthComponent() + 1, this.getWidthComponent() + 1);
		
		g.setColor(Color.black);
		g.drawOval(0, 0, this.getWidthComponent(), this.getWidthComponent());
		
		g.drawArc((int)(0.25 * this.getWidthComponent()), (int)(-0.25 * this.getWidthComponent()),
			   	  (int)(0.5 * this.getWidthComponent()), (int)(0.5 * this.getWidthComponent()),
				  200, 145);
		g.drawArc((int)(0.25 * this.getWidthComponent()), (int)(0.75 * this.getWidthComponent()),
				  (int)(0.5 * this.getWidthComponent()), (int)(0.5 * this.getWidthComponent()),
				  160, -145);
		// label
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.setColor(new Color(51, 51, 51));
		g.drawString(this.name, (int)(this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2), 15 + this.getWidthComponent());
		g.drawString(String.valueOf(this.rate), (int)(this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(String.valueOf(this.rate)) / 2), 30 + this.getWidthComponent());
	}
	
	public void setFlowRate(double rate)
	{
		this.rate = rate;
		repaint();
	}
	public double getFlowRate()
	{
		return this.rate;
	}
}
