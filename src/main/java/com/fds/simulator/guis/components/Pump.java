package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;

public class Pump extends SimulationComponent
{
	private static final long serialVersionUID = 1L;
	private JButton buttonPump;
	private String name;
	private boolean state;
	
	public Pump(int x, int y, int width, int height, double pipeWidthPorcentage, String name) 
	{
		super(x, y, width, height, pipeWidthPorcentage);
		
		buttonPump = new JButton("Off");
		buttonPump.setBounds(0, 80, 60, 20);
		buttonPump.setSelected(true);
		buttonPump.setBackground(Color.gray);
		add(buttonPump);
		
		this.name = name;
		
		this.setToolTipText("Pump");
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		g.setColor(new Color(40, 68, 255));
		g.fillOval(0, 0, this.getWidthComponent() + 1, this.getWidthComponent() + 1);
		
		g.setColor(Color.black);
		g.drawOval(0, 0, this.getWidthComponent(), this.getWidthComponent());

		g.drawLine(0, this.getWidthComponent()/2, this.getWidthComponent()/2, 0);	
		g.drawLine(0, this.getWidthComponent()/2, this.getWidthComponent()/2, this.getWidthComponent());
		
		// label
		g.setFont(new Font("Tahoma", Font.BOLD, 11));
		g.setColor(new Color(51, 51, 51));
		g.drawString(name, (int)(this.getWidthComponent() / 2 - g.getFontMetrics().stringWidth(this.name) / 2), 15 + this.getWidthComponent());
	}
	
	/**
	 * 
	 * @param state: true means pump on and false means pump off.
	 */
	public void setPumpState(boolean state)
	{
		this.state = state;
		if (state) 
		{
			buttonPump.setText("On");
			buttonPump.setSelected(false);
			buttonPump.setBackground(Color.green);			
		}
		else 
		{
			buttonPump.setText("Off");
			buttonPump.setSelected(true);
			buttonPump.setBackground(Color.gray);
		}
	}
	public boolean getPumpState(){
		return this.state;
	}
	
}
