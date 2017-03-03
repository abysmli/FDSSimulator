package simulator.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;

import simulator.controllers.SimulatorCenterController;

public class MenuGui extends JPanel {

    private JSlider proportionalValveSlider;

    private final SimulatorCenterController simulatorCenterController;

    JLabel currentWaterLevel1, currentWaterLevel2, currentWaterTemp, abfStatus, connectionStatus;
    JButton Task1Button, Task2Button, Task3Button, Task4Button, StartButton, StopButton, FillingButton, FillingReplaceButton, HeatingButton, PumpingButton, AirPumpingButton, WatchListButton, AddFaultButton, ResetButton;

    /**
     * Create the panel.
     *
     * @param simulatorCenterController
     * @throws Exception
     */
    public MenuGui(SimulatorCenterController simulatorCenterController) throws Exception {
        this.simulatorCenterController = simulatorCenterController;
    }

    public void init() throws Exception {
        setBorder(new LineBorder(new Color(128, 128, 128)));
        setBounds(1000, 0, 302, 800);
        setLayout(null);

        JLabel userInterfaceLabel = new JLabel("Monitor");
        userInterfaceLabel.setFont(new Font("Ubuntu", 0, 20));
        userInterfaceLabel.setBounds(110, 10, 150, 20);
        add(userInterfaceLabel);

        JLabel waterLevelLabel1 = new JLabel("Water Level in Tank 101:");
        waterLevelLabel1.setBounds(40, 40, 200, 20);
        waterLevelLabel1.setFont(new Font("Ubuntu", 0, 14));
        add(waterLevelLabel1);

        currentWaterLevel1 = new JLabel("0.0 L");
        currentWaterLevel1.setBounds(230, 40, 100, 20);
        currentWaterLevel1.setFont(new Font("Ubuntu", 0, 14));
        add(currentWaterLevel1);

        JLabel waterLevelLabel2 = new JLabel("Water Level in Tank 102:");
        waterLevelLabel2.setBounds(40, 60, 200, 20);
        waterLevelLabel2.setFont(new Font("Ubuntu", 0, 14));
        add(waterLevelLabel2);

        currentWaterLevel2 = new JLabel("0.0 L");
        currentWaterLevel2.setBounds(230, 60, 100, 20);
        currentWaterLevel2.setFont(new Font("Ubuntu", 0, 14));
        add(currentWaterLevel2);

        JLabel waterTemperature = new JLabel("Water Temperature:");
        waterTemperature.setBounds(40, 80, 160, 20);
        waterTemperature.setFont(new Font("Ubuntu", 0, 14));
        add(waterTemperature);

        currentWaterTemp = new JLabel("0.0 °C");
        currentWaterTemp.setBounds(230, 80, 100, 20);
        currentWaterTemp.setFont(new Font("Ubuntu", 0, 14));
        add(currentWaterTemp);

        JLabel proportionalValveLabel = new JLabel("Proportional Valve:");
        proportionalValveLabel.setBounds(85, 110, 138, 20);
        proportionalValveLabel.setFont(new Font("Ubuntu", 0, 14));
        add(proportionalValveLabel);

        proportionalValveSlider = new JSlider();
        proportionalValveSlider.setMajorTickSpacing(1);
        proportionalValveSlider.setBounds(15, 130, 275, 50);
        proportionalValveSlider.setFocusable(false);
        add(proportionalValveSlider);

        JLabel tasklabel = new JLabel("Add Tasks");
        tasklabel.setFont(new Font("Ubuntu", 0, 20));
        tasklabel.setBounds(104, 180, 200, 25);
        add(tasklabel);

        Task1Button = new JButton("Auto. Cycl. 30°C");
        Task1Button.setBounds(16, 210, 128, 37);
        Task1Button.setToolTipText("Automatic Cycling in order heat 30°C Water");
        add(Task1Button);

        Task2Button = new JButton("3L 45°C Water");
        Task2Button.setBounds(162, 210, 128, 37);
        Task2Button.setToolTipText("Heat 3L, 45°C Water in Tank 101");
        add(Task2Button);

        Task3Button = new JButton("5L Water");
        Task3Button.setBounds(16, 260, 128, 37);
        Task3Button.setToolTipText("Pour 5L Water in Tank 101");
        add(Task3Button);

        Task4Button = new JButton("Clean Pipe");
        Task4Button.setBounds(162, 260, 128, 37);
        Task4Button.setToolTipText("Filling and Pumping periodic, in order to clean the Tanks.");
        add(Task4Button);

        JLabel processlabel = new JLabel("Functions");
        processlabel.setFont(new Font("Ubuntu", 0, 20));
        processlabel.setBounds(105, 310, 200, 25);
        add(processlabel);

        FillingButton = new JButton("Filling");
        FillingButton.setBounds(16, 340, 128, 37);
        FillingButton.setToolTipText("Execute Filling Function. Water will flowing from upper Tank into lower Tank.");
        add(FillingButton);
        
        FillingReplaceButton = new JButton("Filling rep.");
        FillingReplaceButton.setBounds(162, 340, 128, 37);
        FillingReplaceButton.setToolTipText("Execute Filling2 Function. Water will flowing from upper Tank into lower Tank.");
        add(FillingReplaceButton);

        HeatingButton = new JButton("Heating");
        HeatingButton.setBounds(16, 390, 128, 37);
        HeatingButton.setToolTipText("Execute Heating Function. Water will be heated in lower Tank.");
        add(HeatingButton);

        PumpingButton = new JButton("Pumping");
        PumpingButton.setBounds(162, 390, 128, 37);
        PumpingButton.setToolTipText("Execute Pumping Function. Water will be pumped from lower Tank into upper Tank.");
        add(PumpingButton);

        AirPumpingButton = new JButton("Air Pumping");
        AirPumpingButton.setBounds(16, 440, 128, 37);
        AirPumpingButton.setToolTipText("Execute Air Pumping Function. Air will be pumped into Tanks.");
        add(AirPumpingButton);

        JLabel controllerlabel = new JLabel("Controller");
        controllerlabel.setFont(new Font("Ubuntu", 0, 20));
        controllerlabel.setBounds(105, 490, 200, 25);
        add(controllerlabel);

        StartButton = new JButton("Start");
        StartButton.setBounds(16, 520, 128, 37);
        StartButton.setToolTipText("Start all Tasks.");
        add(StartButton);
        
        StopButton = new JButton("Stop");
        StopButton.setEnabled(false);
        StopButton.setBounds(162, 520, 128, 37);
        StopButton.setToolTipText("Stop all Functions and Tasks.");
        add(StopButton);

        ResetButton = new JButton("Reset");
        ResetButton.setBounds(16, 570, 128, 37);
        ResetButton.setToolTipText("Reset Simulator.");
        add(ResetButton);

        WatchListButton = new JButton("Watch List");
        WatchListButton.setBounds(162, 570, 128, 37);
        WatchListButton.setToolTipText("Open Watch List GUI to monitor all running states of Simulator.");
        WatchListButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.watchList();
        });
        add(WatchListButton);

        AddFaultButton = new JButton("Add Fault");
        AddFaultButton.setBounds(16, 620, 128, 37);
        AddFaultButton.setToolTipText("Open Add Fault GUI to add fault into Simulator.");
        AddFaultButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.addFaultGUI();
        });
        add(AddFaultButton);

        JLabel statuslabel = new JLabel("Status");
        statuslabel.setFont(new Font("Ubuntu", 0, 20));
        statuslabel.setBounds(123, 670, 180, 25);
        add(statuslabel);

        JLabel AbfStatusLabel = new JLabel("Abfuellanlage Status:");
        AbfStatusLabel.setBounds(40, 700, 200, 20);
        AbfStatusLabel.setFont(new Font("Ubuntu", 0, 14));
        add(AbfStatusLabel);

        abfStatus = new JLabel("stop");
        abfStatus.setBounds(200, 700, 100, 20);
        abfStatus.setFont(new Font("Ubuntu", 0, 14));
        add(abfStatus);

        JLabel connetcionLabel = new JLabel("Connection Status:");
        connetcionLabel.setBounds(40, 720, 200, 20);
        connetcionLabel.setFont(new Font("Ubuntu", 0, 14));
        add(connetcionLabel);

        connectionStatus = new JLabel("disconnected");
        connectionStatus.setBounds(200, 720, 100, 20);
        connectionStatus.setFont(new Font("Ubuntu", 0, 14));
        add(connectionStatus);
        
        this.StartButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.StartTasks();
        });
        this.ResetButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.Reset();
        });
        this.Task1Button.addActionListener((ActionEvent e) -> {
            simulatorCenterController.AutoCycl30();
        });
        this.Task2Button.addActionListener((ActionEvent e) -> {
            simulatorCenterController.PourWater5L();
        });
        this.Task3Button.addActionListener((ActionEvent e) -> {
            simulatorCenterController.HeatWater3L45();
        });
        this.Task4Button.addActionListener((ActionEvent e) -> {
            simulatorCenterController.Clean();
        });
        this.StopButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.Stop();
        });
        this.AirPumpingButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.AirPumpingProcess();
        });
        this.FillingButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.FillingProcess();
        });
        this.FillingReplaceButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.FillingReplaceProcess();
        });
        this.PumpingButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.PumpingProcess();
        });
        this.HeatingButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.HeatingProcess();
        });
        checkConnection();
    }

    private void checkConnection() throws Exception {
        if (simulatorCenterController.checkConnection()) {
            setConnectionStatus("connected");
        } else {
            setConnectionStatus("disconnected");
        }
    }

    public void setTask1ButtonEnable(boolean enable) {
        Task1Button.setEnabled(enable);
    }

    public void setTask2ButtonEnable(boolean enable) {
        Task2Button.setEnabled(enable);
    }

    public void setTask3ButtonEnable(boolean enable) {
        Task3Button.setEnabled(enable);
    }

    public void setTask4ButtonEnable(boolean enable) {
        Task4Button.setEnabled(enable);
    }

    public void setStartButtonEnable(boolean enable) {
        StartButton.setEnabled(enable);
    }
    
    public void setStopButtonEnable(boolean enable) {
        StopButton.setEnabled(enable);
    }

    public void setFillingButtonEnable(boolean enable) {
        FillingButton.setEnabled(enable);
    }
    
    public void setFillingReplaceButtonEnable(boolean enable) {
        FillingReplaceButton.setEnabled(enable);
    }

    public void setHeatingButtonEnable(boolean enable) {
        HeatingButton.setEnabled(enable);
    }

    public void setPumpingButtonEnable(boolean enable) {
        PumpingButton.setEnabled(enable);
    }

    public void setAirPumpingButtonEnable(boolean enable) {
        AirPumpingButton.setEnabled(enable);
    }

    public void setWaterLevel1(double level) {
        currentWaterLevel1.setText(level + " L");
        currentWaterLevel1.setFont(new Font("Ubuntu", 0, 14));
    }

    public void setWaterLevel2(double level) {
        currentWaterLevel2.setText(level + " L");
        currentWaterLevel2.setFont(new Font("Ubuntu", 0, 14));
    }

    public void setWaterTemp(double temp) {
        currentWaterTemp.setText(temp + "°C");
        currentWaterTemp.setFont(new Font("Ubuntu", 0, 14));
    }

    public void setSliderValue(double value) {
        this.proportionalValveSlider.setValue((int) (100 * value));
    }

    public double getSliderValue() {
        return this.proportionalValveSlider.getValue() / 100.0;
    }

    public void setAbfStatus(String status) {
        abfStatus.setText(status);
    }

    public void setConnectionStatus(String status) {
        connectionStatus.setText(status);
    }
}
