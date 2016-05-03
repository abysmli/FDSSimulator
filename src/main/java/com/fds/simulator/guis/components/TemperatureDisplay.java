package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;

public class TemperatureDisplay extends SimulationComponent
{
	private static final long serialVersionUID = 1L;
	private double temperature;

	public TemperatureDisplay(int x, int y, int width, int height, double pipeWidthPorcentage) 
	{
		super(x, y, width, height, pipeWidthPorcentage);
		
		this.setToolTipText("Temperature Display");
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		g.drawRoundRect(0, 0, this.getWidthComponent(), this.getHeightComponent(), 5, 5);
		g.setColor(Color.white);
		g.fillRoundRect(1, 1, this.getWidthComponent() - 1, this.getHeightComponent() - 1, 2, 2);
		g.setColor(Color.black);
		g.drawString(String.format("%.1f", temperature) + "°C", 
				(int)(this.getWidthComponent() * 0.5 - g.getFontMetrics().stringWidth(String.valueOf(String.format("%.1f", temperature) + "°C")) *0.5), 
				(int)((this.getHeightComponent() * 0.5) + 5.5));	
	}
	
	public void setTemperature(double temperature) 
	{
		this.temperature = temperature;
		repaint();
	}

	public double getTemperature() 
	{
		return this.temperature;
	}
}
