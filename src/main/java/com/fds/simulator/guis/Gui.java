package com.fds.simulator.guis;

import com.fds.simulator.controllers.SimulatorCenterController;
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

    private final SimulatorCenterController simulatorCenterController;

    public Gui(SimulatorCenterController simulatorCenterController) {
        this.simulatorCenterController = simulatorCenterController;

        this.processLabel = new JLabel("Function: stop");
        this.processLabel.setBounds(550, 50, 200, 20);

        this.heater = new Heater(610, 490, 30, 30, 0.07);
        this.heater.setToolTipText("Heizung, Actor, E104, ID: 2");

        this.tank1 = new Tank(100, 50, 150, 250, 0.07, "Tank 102");
        this.tank1.setToolTipText("Tank 102, Tank, ID: 26");

        this.tank2 = new Tank(600, 280, 150, 250, 0.07, "Tank 101");
        this.tank2.setToolTipText("Tank 101, Tank, ID: 4");

        this.valve = new Valve(448, 342, 60, 200, 0.07, ((Tank) tank1).getPipeWidth(), "Valve 102");
        this.valve.setToolTipText("Kugelhahn, Actor, V102, ID: 5");

        this.ball = new Ball(463, 311, 30, 30, 0.07, "Ball");
        this.ball.setToolTipText("Kugelhahn, Schalter, S115/S116, ID: 6/7");

        this.proportionalValve = new ProportionalValve(270, 551, 60, 120, 0.07, ((Tank) tank1).getPipeWidth(),
                "Prop. Valve");
        this.proportionalValve.setToolTipText("Proportionalwegeventil, Actor, M106, ID: 11");

        this.pressuremeter = new Pressuremeter(160, 550, 60, 100, 0.07, "Pressure");
        this.pressuremeter.setToolTipText("Wasserdrucksensor, Sensor, B103, ID: 14");

        this.pump = new Pump(515, 550, 60, 100, 0.07, "Pump");
        this.pump.setToolTipText("Durchflusspumpe, Actor, P101, ID: 13");

        this.flowmeter = new Flowmeter(380, 550, 60, 100, 0.07, "Flowmeter");
        this.flowmeter.setToolTipText("Wasserdurchflusssensor, Sensor, B102, ID:15");

        this.manualvalvelower2 = new ManualValveLower2(600, 562, 60, 120, 0.07, ((Tank) tank1).getPipeWidth(), "Valve 109");
        this.manualvalvelower2.setToolTipText("Ventil 109, Ventil, V109, ID: 19");

        this.airvalve = new AirValve(560, 110, 60, 120, 0.07, ((Valve) valve).getPipeWidth(), "Air Valve");
        this.airvalve.setToolTipText("Magnet Ventil, Actor, VAir, ID: 20");

        this.airflowmeter = new AirFlowMeter(348, 150, 200, 100, 0.07, "Air Flowmeter");
        this.airflowmeter.setToolTipText("Luftdurchflusssensor, Sensor, Air101, ID: 21");

        this.airpressuremeter = new AirPressuremeter(348, 225, 200, 100, 0.07, "Air Pressure");
        this.airpressuremeter.setToolTipText("Luftdrucksensor, Sensor, Air102, ID: 22");

        this.airpump = new AirPump(650, 100, 60, 100, 0.07, "Air Pump");
        this.airpump.setToolTipText("Kompressor,Actor, KOMP, ID: 23");

        this.manualvalvelower = new ManualValveLower(60, 562, 60, 120, 0.07, ((Tank) tank1).getPipeWidth(), "Valve 104");
        this.manualvalvelower.setToolTipText("Ventil, V104, ID: 24");

        this.manualvalve = new ManualValve(300, 342, 60, 100, 0.07, ((Tank) tank1).getPipeWidth(), "Valve 112");
        this.manualvalve.setToolTipText("Manual Valve 112, Ventil, V112, ID: 25");

        this.temperatureDisplay = new TemperatureDisplay(680, 490, 60, 30, 0.07);
        this.temperatureDisplay.setToolTipText("Temperature Display");

        this.upperPipe = new UpperPipe((Tank) this.tank1, (Tank) this.tank2, (Valve) this.valve);
        this.lowerPipe = new LowerPipe((Tank) this.tank1, (Tank) this.tank2);
        this.airPipe = new AirPipe((Valve) this.valve, (AirPump) this.airpump);
    }

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

    public void setSliderValue(double value) {
        this.simulatorCenterController.getMenuGUI().setSliderValue(value);
    }

    public double getSliderValue() {
        return this.simulatorCenterController.getMenuGUI().getSliderValue();
    }

    public void setShowWaterLevel1(double level) {
        this.simulatorCenterController.getMenuGUI().setWaterLevel1(level);
    }

    public void setShowWaterLevel2(double level) {
        this.simulatorCenterController.getMenuGUI().setWaterLevel2(level);
    }

    public void setShowWaterTemp(double temp) {
        this.simulatorCenterController.getMenuGUI().setWaterTemp(temp);
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
    }

    public void setManualValveState(boolean state) {
        ((ManualValve) this.manualvalve).setValveState(state);
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
        ((Ball) this.ball).setBallState(airstate);
    }

    public void setAirFlowRate(double airflowrate) {
        ((AirFlowMeter) this.airflowmeter).setFlowRate(airflowrate);
    }

    public void setAirPressure(double airpressure) {
        ((AirPressuremeter) this.airpressuremeter).setPressure(airpressure);
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
        return ((AirPressuremeter) this.airpressuremeter).getPressure();
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
