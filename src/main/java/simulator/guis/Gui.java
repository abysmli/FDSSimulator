package simulator.guis;

import simulator.controllers.SimulatorCenterController;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import simulator.guis.components.AirFlowMeter;
import simulator.guis.components.AirPipe;
import simulator.guis.components.AirPressuremeter;
import simulator.guis.components.AirPump;
import simulator.guis.components.AirValve;
import simulator.guis.components.Ball;
import simulator.guis.components.Flowmeter;
import simulator.guis.components.Heater;
import simulator.guis.components.LowerPipe;
import simulator.guis.components.ManualValve;
import simulator.guis.components.ManualValveLower;
import simulator.guis.components.ManualValveLower2;
import simulator.guis.components.Pressuremeter;
import simulator.guis.components.ProportionalValve;
import simulator.guis.components.Pump;
import simulator.guis.components.SimulationComponent;
import simulator.guis.components.Tank101;
import simulator.guis.components.TemperatureDisplay;
import simulator.guis.components.UpperPipe;
import simulator.guis.components.Valve;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import simulator.guis.components.MC;
import simulator.guis.components.Switcher;
import simulator.guis.components.Tank102;
import simulator.guis.components.UltrasonicSensor;
import simulator.guis.components.UpperReplacePipe;
import simulator.guis.components.Valve113;

public class Gui extends JPanel {

    private static final long serialVersionUID = 1L;

    private final SimulationComponent heater;
    private final SimulationComponent temperatureDisplay;
    private final SimulationComponent temperatureDisplay2;
    private final SimulationComponent tank102;
    private final SimulationComponent tank101;
    private final SimulationComponent pump;
    private final SimulationComponent valve102;
    private final SimulationComponent flowmeter;
    private final SimulationComponent proportionalValve;
    private final SimulationComponent upperPipe;
    private final SimulationComponent lowerPipe;

    private final SimulationComponent valve112;
    private final SimulationComponent valve113;
    private final SimulationComponent pressuremeter;
    private final SimulationComponent valve104;
    private final SimulationComponent valve109;
    private final SimulationComponent airpump;
    private final SimulationComponent airPipe;
    private final SimulationComponent ball;
    private final SimulationComponent airvalve;
    private final SimulationComponent airflowmeter;
    private final SimulationComponent airpressuremeter;
    private final SimulationComponent upperReplacePipe;

    private final SimulationComponent switch111;
    private final SimulationComponent switch112;
    private final SimulationComponent switch113;
    private final SimulationComponent switch114;
    private final SimulationComponent switch117;
    private final SimulationComponent ultrasonic;
    private final SimulationComponent mc;

    private final JLabel processLabel;

    private final SimulatorCenterController simulatorCenterController;

    public Gui(SimulatorCenterController simulatorCenterController) {
        this.simulatorCenterController = simulatorCenterController;

        this.processLabel = new JLabel("Function: stop");
        this.processLabel.setBounds(550, 50, 200, 20);

        this.heater = new Heater(610, 490, 30, 30, 0.07);
        this.heater.setToolTipText("Heizung, Actor, E104, ID: 2");

        this.tank102 = new Tank102(100, 50, 150, 250, 0.07, "Tank 102");
        this.tank102.setToolTipText("Tank 102, Tank, ID: 26");

        this.tank101 = new Tank101(600, 280, 150, 250, 0.07, "Tank 101");
        this.tank101.setToolTipText("Tank 101, Tank, ID: 4");

        this.valve102 = new Valve(448, 342, 60, 80, 0.07, ((Tank102) tank102).getPipeWidth(), "Valve 102");
        this.valve102.setToolTipText("Kugelhahn, Actor, V102, ID: 5");

        this.ball = new Ball(463, 311, 30, 30, 0.07, "Ball");
        this.ball.setToolTipText("Kugelhahn, Schalter, S115/S116, ID: 6/7");

        this.proportionalValve = new ProportionalValve(270, 551, 60, 120, 0.07, ((Tank102) tank102).getPipeWidth(),
                "Prop. Valve");
        this.proportionalValve.setToolTipText("Proportionalwegeventil, Actor, M106, ID: 11");

        this.pressuremeter = new Pressuremeter(160, 550, 60, 100, 0.07, "Pressure");
        this.pressuremeter.setToolTipText("Wasserdrucksensor, Sensor, B103, ID: 14");

        this.pump = new Pump(515, 550, 60, 100, 0.07, "Pump");
        this.pump.setToolTipText("Durchflusspumpe, Actor, P101, ID: 13");

        this.flowmeter = new Flowmeter(380, 550, 60, 100, 0.07, "Flowmeter");
        this.flowmeter.setToolTipText("Wasserdurchflusssensor, Sensor, B102, ID:15");

        this.valve109 = new ManualValveLower2(600, 562, 60, 120, 0.07, ((Tank102) tank102).getPipeWidth(), "Valve 109");
        this.valve109.setToolTipText("Ventil 109, Ventil, V109, ID: 19");

        this.airvalve = new AirValve(560, 110, 60, 120, 0.07, ((Valve) valve102).getPipeWidth(), "Air Valve");
        this.airvalve.setToolTipText("Magnet Ventil, Actor, VAir, ID: 20");

        this.airflowmeter = new AirFlowMeter(348, 150, 200, 100, 0.07, "Air Flowmeter");
        this.airflowmeter.setToolTipText("Luftdurchflusssensor, Sensor, Air101, ID: 21");

        this.airpressuremeter = new AirPressuremeter(348, 225, 200, 100, 0.07, "Air Pressure");
        this.airpressuremeter.setToolTipText("Luftdrucksensor, Sensor, Air102, ID: 22");

        this.airpump = new AirPump(650, 100, 60, 100, 0.07, "Air Pump");
        this.airpump.setToolTipText("Kompressor,Actor, KOMP, ID: 23");

        this.valve104 = new ManualValveLower(60, 562, 60, 120, 0.07, ((Tank102) tank102).getPipeWidth(), "Valve 104");
        this.valve104.setToolTipText("Ventil, V104, ID: 24");

        this.valve112 = new ManualValve(300, 342, 60, 100, 0.07, ((Tank102) tank102).getPipeWidth(), "Valve 112");
        this.valve112.setToolTipText("Manual Valve 112, Ventil, V112, ID: 25");

        this.valve113 = new Valve113(448, 442, 60, 100, 0.07, ((Tank102) tank102).getPipeWidth(), "Valve 113");
        this.valve113.setToolTipText("Digital Valve 113, Ventil, V113, ID: 28");

        this.temperatureDisplay = new TemperatureDisplay(610, 420, 60, 30, 0.07);
        this.temperatureDisplay.setToolTipText("Temperature Display, B104, ID: 3");

        this.temperatureDisplay2 = new TemperatureDisplay(610, 380, 60, 30, 0.07);
        this.temperatureDisplay2.setToolTipText("Temperature Display 2, B105, ID: 27");

        this.upperPipe = new UpperPipe((Tank102) this.tank102, (Tank101) this.tank101, (Valve) this.valve102);
        this.upperReplacePipe = new UpperReplacePipe((Tank102) this.tank102, (Tank101) this.tank101, (Valve) this.valve102);
        this.lowerPipe = new LowerPipe((Tank102) this.tank102, (Tank101) this.tank101);
        this.airPipe = new AirPipe((Valve) this.valve102, (AirPump) this.airpump);

        this.switch114 = new Switcher(700, 370, 50, 15, 0.07);
        this.switch114.setToolTipText("Maximum Fill Level sensor of Tank 101, B114, ID: 10");

        this.switch117 = new Switcher(700, 435, 50, 15, 0.07);
        this.switch117.setToolTipText("Medium Fill Level sensor of Tank 101, S117, ID: 9");

        this.switch113 = new Switcher(700, 500, 50, 15, 0.07);
        this.switch113.setToolTipText("Minimum Fill Level sensor of Tank 101, B113, ID: 18");

        this.switch111 = new Switcher(200, 140, 50, 15, 0.07);
        this.switch111.setToolTipText("Maximum Fill Level sensor of Tank 102, S111, ID: 16");

        this.switch112 = new Switcher(200, 205, 50, 15, 0.07);
        this.switch112.setToolTipText("Medium Fill Level sensor of Tank 102, S112, ID: 17");

        this.ultrasonic = new UltrasonicSensor(600, 332, 15, 20, 0.07);
        this.ultrasonic.setToolTipText("Ultrasonic Sensor of Tank 101, B101, ID: 8");

        this.mc = new MC(600, 720, 150, 40, 0.07);
        this.mc.setToolTipText("Microcontroller, MC, ID: 1");
    }

    public void init() {
        setBorder(new LineBorder(new Color(128, 128, 128)));
        setBounds(0, 0, 798, 800);
        setBackground(new Color(204, 229, 255));
        setLayout(null);
        this.pressuremeter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B103");
            }
        });
        add(this.pressuremeter);

        this.heater.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("E104");
            }
        });
        add(this.heater);

        this.temperatureDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B104");
            }
        });
        add(this.temperatureDisplay);

        this.temperatureDisplay2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B105");
            }
        });
        add(this.temperatureDisplay2);

        this.switch111.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("S111");
            }
        });
        add(this.switch111);

        this.switch112.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("S112");
            }
        });
        add(this.switch112);

        this.switch113.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B113");
            }
        });
        add(this.switch113);

        this.switch114.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B114");
            }
        });
        add(this.switch114);

        this.switch117.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("S117");
            }
        });
        add(this.switch117);

        this.ultrasonic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B101");
            }
        });
        add(this.ultrasonic);

        this.tank102.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("Tank102");
            }
        });
        add(this.tank102);

        this.tank101.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("Tank101");
            }
        });
        add(this.tank101);

        // V112
        this.valve112.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("V112");
            }
        });
        add(this.valve112);

        // V113
        this.valve113.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("V113");
            }
        });
        add(this.valve113);
        setValveStateV113(false);

        // Valve 102
        this.valve102.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("V102");
            }
        });
        add(this.valve102);

        this.ball.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("S117");
            }
        });
        add(this.ball);

        // V104
        this.valve104.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("V104");
            }
        });
        add(this.valve104);

        // V109
        this.valve109.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("V109");
            }
        });
        add(this.valve109);

        this.airvalve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("VAir");
            }
        });
        add(this.airvalve);

        this.pump.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("P101");
            }
        });
        add(this.pump);

        this.flowmeter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("B102");
            }
        });
        add(this.flowmeter);

        this.airflowmeter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("Air101");
            }
        });
        add(this.airflowmeter);

        this.airpressuremeter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("Air102");
            }
        });
        add(this.airpressuremeter);

        this.proportionalValve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("M106");
            }
        });
        add(this.proportionalValve);

        this.airpump.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("KOMP");
            }
        });
        add(this.airpump);

        this.mc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                simulatorCenterController.openAddFaultGUIBySeries("MC");
            }
        });
        add(this.mc);
        
        add(this.upperPipe);
        add(this.upperReplacePipe);
        add(this.lowerPipe);
        add(this.processLabel);
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
        ((Tank102) this.tank102).setBlueStripe(state);
        // menuGui.setPumpButtonState(state);
    }

    public void setPumpMotorState(boolean state) {
        ((Pump) this.pump).setPumpState(state);
    }

    public void setValveStateID5(boolean state) {
        ((Valve) this.valve102).setValveState(state);
    }

    public void setValveStateV113(boolean state) {
        ((Valve113) this.valve113).setValveState(state);
        ((UpperReplacePipe) this.upperReplacePipe).setLastPipeSectionBlue(state);
        ((Tank101) this.tank101).setBlueRepalceStripe(state);
    }

    public void setValveState(boolean state) {
        ((Valve) this.valve102).setValveState(state);
        ((UpperPipe) this.upperPipe).setLastPipeSectionBlue(state);
        ((Tank101) this.tank101).setBlueStripe(state);
    }

    public void setManualValveState(boolean state) {
        ((ManualValve) this.valve112).setValveState(state);
    }

    public void setManualValveLowerState(boolean state) {
        ((ManualValveLower) this.valve104).setValveState(state);
    }

    public void setManualValveLower2State(boolean state) {
        ((ManualValveLower2) this.valve109).setValveState(state);
    }

    public void setUpperTankLevel(double upperTankLevel) {
        ((Tank102) this.tank102).setTankLevel(upperTankLevel);
    }

    public void setLowerTankLevel(double lowerTankLevel) {
        ((Tank101) this.tank101).setTankLevel(lowerTankLevel);
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

    public void setTemperatureDisplay2(double temperature) {
        ((TemperatureDisplay) this.temperatureDisplay2).setTemperature(temperature);
    }

    public void setPressureRate(double pressurerate) {
        ((Pressuremeter) this.pressuremeter).setPressureRate(pressurerate);
    }

    public void setAirValveState(boolean state) {
        ((AirValve) this.airvalve).setValveState(state);
    }

    public void setAirPumpState(boolean state) {
        ((AirValve) this.airpump).setValveState(state);
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
        return ((Tank101) this.tank101).getTankLevel();
    }

    public double getTankLevel() {
        return ((Tank102) this.tank102).getTankLevel();
    }

    public boolean getValveState() {
        return ((Valve) this.valve102).getValveState();
    }

    public boolean getManualValveState() {
        return ((ManualValve) this.valve112).getValveState();
    }

    public boolean getValveStateV113() {
        return ((Valve113) this.valve113).getValveState();
    }

    public double getTemperature() {
        return ((TemperatureDisplay) this.temperatureDisplay).getTemperature();
    }

    public double getTemperature2() {
        return ((TemperatureDisplay) this.temperatureDisplay2).getTemperature();
    }

    public boolean getPumpState() {
        return ((Pump) this.pump).getPumpState();
    }

    public void setUltraSensor117(int checkUltrasonicSensorState) {
        ((Switcher) this.switch117).setSwitcherState(checkUltrasonicSensorState);
    }

    public void setUltraSensor114(int checkUltrasonicSensorState) {
        ((Switcher) this.switch114).setSwitcherState(checkUltrasonicSensorState);
    }

    public void setUltraSensor111(int checkUltrasonicSensorState) {
        ((Switcher) this.switch111).setSwitcherState(checkUltrasonicSensorState);
    }

    public void setUltraSensor112(int checkUltrasonicSensorState) {
        ((Switcher) this.switch112).setSwitcherState(checkUltrasonicSensorState);
    }

    public void setUltraSensor113(int checkUltrasonicSensorState) {
        ((Switcher) this.switch113).setSwitcherState(checkUltrasonicSensorState);
    }
}
