package simulator.guis.components;

import javax.swing.JComponent;

public abstract class SimulationComponent extends JComponent
{
	private static final long serialVersionUID = 1L;
	private final int xCoordinate;
	private final int yCoordinate;
	private final int widthComponent;
	private final int heightComponent;
	private final double pipeRadiusPercentage; // Radius of the pipe in percentage based on the tank's width

	public SimulationComponent(int xCoordinate, int yCoordinate, int widthComponent, int heightComponent, double pipeWidthPorcentage) 
	{
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.widthComponent = widthComponent;
		this.heightComponent = heightComponent;
		this.pipeRadiusPercentage = pipeWidthPorcentage;
		this.setBounds(this.xCoordinate, this.yCoordinate, this.widthComponent+1, this.heightComponent+1);
	}
		
	public int getXCoordinate() 
	{
		return this.xCoordinate;
	}
	
	public int getYCoordinate() 
	{
		return this.yCoordinate;
	}
	
	public int getWidthComponent() 
	{
		return this.widthComponent;
	}
	
	public int getHeightComponent() 
	{
		return this.heightComponent;
	}
	
	public double getPipeRadiusPercentage() 
	{
		return this.pipeRadiusPercentage;
	}
}
