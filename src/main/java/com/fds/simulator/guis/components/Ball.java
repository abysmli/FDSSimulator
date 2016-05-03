package com.fds.simulator.guis.components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class Ball extends SimulationComponent
{
	private static final long serialVersionUID = 1L;
	private JButton buttonPump;
//	private String name;
	private boolean state;
	public Ball(int x, int y, int width, int height, double pipeWidthPorcentage, String name) 
	{
		super(x, y, width, height, pipeWidthPorcentage);
		
		buttonPump = new JButton("Off");
		buttonPump.setBounds(0, 80, 60, 20);
		buttonPump.setSelected(true);
		buttonPump.setBackground(Color.gray);
		add(buttonPump);
		
//		this.name = name;
		
		this.setToolTipText("Ball");
	}

	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
			
		if(state){
			g.setColor(Color.black);
			g.drawRect(0, 0, this.getWidthComponent(), this.getWidthComponent());
			g.setColor(Color.green);
			g.fillRect(1, 1, this.getWidthComponent()-1, this.getWidthComponent()-1);
		}else{
			g.setColor(Color.black);
			g.drawRect(0, 0, this.getWidthComponent(), this.getWidthComponent());
			g.setColor(Color.red);
			g.fillRect(1, 1, this.getWidthComponent()-1, this.getWidthComponent()-1);
		}

	}
	
	/**
	 * 
	 * @param state: true means pump on and false means pump off.
	 */
	public void setBallState(boolean state)
	{
		this.state = state;
		repaint();
	}
}
