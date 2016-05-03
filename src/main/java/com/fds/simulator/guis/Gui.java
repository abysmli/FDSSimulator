package com.fds.simulator.guis;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.fds.simulator.guis.components.AirFlowMeter;
import com.fds.simulator.guis.components.AirPipe;
import com.fds.simulator.guis.components.AirPressuremeter;
import com.fds.simulator.guis.components.AirPump;
import com.fds.simulator.guis.components.AirValve;
import com.fds.simulator.guis.components.Ball;
import com.fds.simulator.guis.components.Flowmeter;
import com.fds.simulator.guis.components.Heater;
import com.fds.simulator.guis.components.LowerPipe;
import com.fds.simulator.guis.components.ManualValve;
import com.fds.simulator.guis.components.ManualValveLower;
import com.fds.simulator.guis.components.ManualValveLower2;
import com.fds.simulator.guis.components.Pressuremeter;
import com.fds.simulator.guis.components.ProportionalValve;
import com.fds.simulator.guis.components.Pump;
import com.fds.simulator.guis.components.SimulationComponent;
import com.fds.simulator.guis.components.Tank;
import com.fds.simulator.guis.components.TemperatureDisplay;
import com.fds.simulator.guis.components.UpperPipe;
import com.fds.simulator.guis.components.Valve;

public class Gui extends JPanel {

    private static final long serialVersionUID = 1L;
    private MenuGui menuGui;

    private final SimulationComponent heater;
    private final SimulationComponent temperatureDisplay;
    private final SimulationComponent tank1;
    private final SimulationComponent tank2;
    private final SimulationComponent pump;
    private final SimulationComponent valve;
    private final SimulationComponent flowmeter;
    private final SimulationComponent proportionalValve;
    private final SimulationComponent upperPipe;
    private final SimulationComponent lowerPipe;

    private final SimulationComponent manualvalve;
    private final SimulationComponent pressuremeter;
    private final SimulationComponent manualvalvelower;
    private final SimulationComponent manualvalvelower2;
    private final SimulationComponent airpump;
    private final SimulationComponent airPipe;
    private final SimulationComponent ball;
    private final SimulationComponent airvalve;
    private final SimulationComponent airflowmeter;
    private final SimulationComponent airpressuremeter;

    private final JLabel processLabel;

    public void init() {
        setBorder(new LineBorder(new Color(128, 128, 128)));
        setBounds(0, 0, 798, 800);
        setBackground(new Color(204, 229, 255));
        setLayout(null);
        add(this.pressuremeter);
        add(this.heater);
        add(this.temperatureDisplay);
        add(this.tank1);
        add(this.tank2);
        add(this.manualvalve);
        add(this.valve);
        add(this.ball);
        add(this.manualvalvelower);
        add(this.manualvalvelower2);
        add(this.airvalve);
        add(this.pump);
        add(this.flowmeter);
        add(this.airflowmeter);
        add(this.airpressuremeter);
        add(this.valve);
        add(this.proportionalValve);
        add(this.upperPipe);
        add(this.lowerPipe);
        add(this.processLabel);
        add(this.airpump);
        add(this.airPipe);
    }
    
    public Gui() {
        this.pressuremeter = new Pressuremeter(130, 550, 60, 100, 0.07, "Pressur");
        
        this.heater = new Heater(610, 490, 30, 30, 0.07);
        
        this.temperatureDisplay = new TemperatureDisplay(680, 490, 60, 30, 0.07);
        
        this.tank1 = new Tank(100, 50, 150, 250, 0.07, "Tank 102");
        
        this.tank2 = new Tank(600, 280, 150, 250, 0.07, "Tank 101");
        
        this.manualvalve = new ManualValve(300, 342, 60, 100, 0.07, ((Tank) tank1).getPipeWidth(), "Valve_M");
        
        this.valve = new Valve(448, 342, 60, 200, 0.07, ((Tank) tank1).getPipeWidth(), "Valve");
        
        this.ball = new Ball(463, 311, 30, 30, 0.07, "Ball");
        
        this.manualvalvelower = new ManualValveLower(60, 562, 60, 120, 0.07, ((Tank) tank1).getPipeWidth(), "Valve_M");

        this.manualvalvelower2 = new ManualValveLower2(600, 562, 60, 120, 0.07, ((Tank) tank1).getPipeWidth(), "Valve_M");
        
        this.airvalve = new AirValve(560, 110, 60, 120, 0.07, ((Valve) valve).getPipeWidth(), "AirValve");
        
        this.pump = new Pump(515, 550, 60, 100, 0.07, "Pump");
        
        this.flowmeter = new Flowmeter(220, 550, 60, 100, 0.07, "Flowmeter");
        
        this.airflowmeter = new AirFlowMeter(348, 150, 200, 100, 0.07, "AirFlowmeter");
        
        this.airpressuremeter = new AirPressuremeter(348, 225, 200, 100, 0.07, "Airepressure");
        
        this.proportionalValve = new ProportionalValve(400, 551, 60, 120, 0.07, ((Tank) tank1).getPipeWidth(),
                "PropValve");

        this.upperPipe = new UpperPipe((Tank) this.tank1, (Tank) this.tank2, (Valve) this.valve);
        
        this.lowerPipe = new LowerPipe((Tank) this.tank1, (Tank) this.tank2);
        
        this.processLabel = new JLabel("Function: stop");
        this.processLabel.setBounds(550, 50, 200, 20);
        
        // air road 
        this.airpump = new AirPump(650, 100, 60, 100, 0.07, "AirPump");
        
        this.airPipe = new AirPipe((Valve) this.valve, (AirPump) this.airpump);
    }

    // setter	
    public void setMenuGUI(MenuGui menuGui) {
        this.menuGui = menuGui;
    }

    public void setSliderValue(double value) {
        this.menuGui.setSliderValue(value);
    }

    public double getSliderValue() {
        return this.menuGui.getSliderValue();
    }

    public void setShowWaterLevel1(double level) {
        this.menuGui.setWaterLevel1(level);
    }

    public void setShowWaterLevel2(double level) {
        this.menuGui.setWaterLevel2(level);
    }

    public void setShowWaterTemp(double temp) {
        this.menuGui.setWaterTemp(temp);
    }

    public void setProcessLabelText(String text) {
        this.processLabel.setText(text);
    }

    public void setPumpState(boolean state) {
        ((Pump) this.pump).setPumpState(state);
        ((LowerPipe) this.lowerPipe).setLastPipeSectionBlue(state);
        ((Tank) this.tank1).setBlueStripe(state);
        // menuGui.setPumpButtonState(state);
    }

    public void setPumpMotorState(boolean state) {
        ((Pump) this.pump).setPumpState(state);
    }

    public void setValveState(boolean state) {
        ((Valve) this.valve).setValveState(state);
        ((UpperPipe) this.upperPipe).setLastPipeSectionBlue(state);
        ((Tank) this.tank2).setBlueStripe(state);
        // menuGui.setValveButtonState(state);
    }

    public void setManualValveState(boolean state) {
        ((ManualValve) this.manualvalve).setValveState(state);
        // menuGui.setValveButtonState(state);
    }

    public void setUpperTankLevel(double upperTankLevel) {
        ((Tank) this.tank1).setTankLevel(upperTankLevel);
    }

    public void setLowerTankLevel(double lowerTankLevel) {
        ((Tank) this.tank2).setTankLevel(lowerTankLevel);
    }

    public void setWaterLevel(double waterLevel) {
        setLowerTankLevel(waterLevel);
        setUpperTankLevel(1 - waterLevel);
    }

    public void setFlowRate(double rate) {
        ((Flowmeter) this.flowmeter).setFlowRate(rate);
    }

    public void setAperturePercentage(double aperturePercentage) {
        ((ProportionalValve) this.proportionalValve).setAperturePercentage(aperturePercentage);
    }

    public void setHeaterState(boolean heaterState) {
        ((Heater) this.heater).setHeaterSate(heaterState);
    }

    public void setTemperatureDisplay(double temperature) {
        ((TemperatureDisplay) this.temperatureDisplay).setTemperature(temperature);

    }

    public void setPressureRate(double pressurerate) {
        ((Pressuremeter) this.pressuremeter).setPressureRate(pressurerate);
    }

    public void setAirState(boolean airstate) {
        ((AirPump) this.airpump).setPumpState(airstate);
        ((AirValve) this.airvalve).setValveState(airstate);
        ((AirPipe) this.airPipe).setLastPipeSectionBlue(airstate);
    }

    public void setAirFlowRate(double airflowrate) {
        ((AirFlowMeter) this.airflowmeter).setFlowRate(airflowrate);
    }

    public void setAirPressureRate(double airpressurerate) {
        ((AirPressuremeter) this.airpressuremeter).setPressureRate(airpressurerate);
    }

    public void setBallState(boolean ballstate) {
        ((Ball) this.ball).setBallState(ballstate);
    }

    //getter
    public double getWaterPressure() {
        return ((Pressuremeter) this.pressuremeter).getPressureRate();
    }

    public double getWaterFlow() {
        return ((Flowmeter) this.flowmeter).getFlowRate();
    }

    public double getAirFlow() {
        return ((AirFlowMeter) this.airflowmeter).getFlowRate();
    }

    public double getAirPressure() {
        return ((AirPressuremeter) this.airpressuremeter).getPressureRate();
    }

    public double getWaterLevel() {
        return ((Tank) this.tank2).getTankLevel();
    }

    public double getTankLevel() {
        return ((Tank) this.tank1).getTankLevel();
    }

    public boolean getValveState() {
        return ((Valve) this.valve).getValveState();
    }

    public boolean getManualValveState() {
        return ((ManualValve) this.manualvalve).getValveState();
    }

    public double getTemperature() {
        return ((TemperatureDisplay) this.temperatureDisplay).getTemperature();
    }

    public boolean getPumpState() {
        return ((Pump) this.pump).getPumpState();
    }
}
