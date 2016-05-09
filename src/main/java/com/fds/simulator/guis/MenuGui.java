package com.fds.simulator.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;

import com.fds.simulator.controllers.SimulatorCenterController;

public class MenuGui extends JPanel {

    private JSlider proportionalValveSlider;

    private final SimulatorCenterController simulatorCenterController;

    JLabel currentWaterLevel1, currentWaterLevel2, currentWaterTemp, abfStatus, connectionStatus;
    JButton SimulationAllTaskButton, SimulationHeatTaskButton, SimulationAirPumpTaskButton, SimulationCleanTaskButton, StopButton, FillingButton, HeatingButton, PumpingButton, AirPumpingButton, WatchListButton, AddFaultButton, ResetButton;

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
        setBounds(798, 0, 302, 800);
        setLayout(null);

        JLabel userInterfaceLabel = new JLabel("User Interface");
        userInterfaceLabel.setFont(new Font("Ubuntu", 0, 20));
        userInterfaceLabel.setBounds(85, 10, 150, 20);
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
        proportionalValveSlider.setBounds(49, 130, 200, 50);
        add(proportionalValveSlider);

        JLabel tasklabel = new JLabel("Tasks");
        tasklabel.setFont(new Font("Ubuntu", 0, 20));
        tasklabel.setBounds(127, 180, 200, 25);
        add(tasklabel);

        SimulationAllTaskButton = new JButton("Task No.1");
        SimulationAllTaskButton.setBounds(16, 210, 128, 37);
        SimulationAllTaskButton.setToolTipText("Simulate all processes periodic.");
        add(SimulationAllTaskButton);

        SimulationHeatTaskButton = new JButton("Task No.2");
        SimulationHeatTaskButton.setBounds(162, 210, 128, 37);
        SimulationHeatTaskButton.setToolTipText("Simulate Filling and Pumping with Heating periodic.");
        add(SimulationHeatTaskButton);

        SimulationAirPumpTaskButton = new JButton("Task No.3");
        SimulationAirPumpTaskButton.setBounds(16, 260, 128, 37);
        SimulationAirPumpTaskButton.setToolTipText("Simulate Filling and Pumping with Air Pumping periodic.");
        add(SimulationAirPumpTaskButton);

        SimulationCleanTaskButton = new JButton("Task No.4");
        SimulationCleanTaskButton.setBounds(162, 260, 128, 37);
        SimulationCleanTaskButton.setToolTipText("Simulate only Filling and Pumping periodic, in order to clean the Tanks.");
        add(SimulationCleanTaskButton);

        JLabel processlabel = new JLabel("Processes");
        processlabel.setFont(new Font("Ubuntu", 0, 20));
        processlabel.setBounds(105, 310, 200, 25);
        add(processlabel);

        FillingButton = new JButton("Filling");
        FillingButton.setBounds(16, 340, 128, 37);
        add(FillingButton);

        HeatingButton = new JButton("Heating");
        HeatingButton.setBounds(162, 340, 128, 37);
        add(HeatingButton);

        PumpingButton = new JButton("Pumping");
        PumpingButton.setBounds(16, 390, 128, 37);
        add(PumpingButton);

        AirPumpingButton = new JButton("Air Pumping");
        AirPumpingButton.setBounds(162, 390, 128, 37);
        add(AirPumpingButton);

        JLabel controllerlabel = new JLabel("Controller");
        controllerlabel.setFont(new Font("Ubuntu", 0, 20));
        controllerlabel.setBounds(105, 440, 200, 25);
        add(controllerlabel);
        
        StopButton = new JButton("Stop");
        StopButton.setEnabled(false);
        StopButton.setBounds(16, 470, 128, 37);
        add(StopButton);
        
        ResetButton = new JButton("Reset");
        ResetButton.setBounds(162, 470, 128, 37);
        add(ResetButton);

        WatchListButton = new JButton("Watch List");
        WatchListButton.setBounds(16, 520, 128, 37);
        WatchListButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.watchList();
        });
        add(WatchListButton);

        AddFaultButton = new JButton("Add Fault");
        AddFaultButton.setBounds(162, 520, 128, 37);
        AddFaultButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.addFaultGUI();
        });
        add(AddFaultButton);

        JLabel statuslabel = new JLabel("Status");
        statuslabel.setFont(new Font("Ubuntu", 0, 20));
        statuslabel.setBounds(123, 570, 180, 25);
        add(statuslabel);

        JLabel AbfStatusLabel = new JLabel("Abfuellanlage Status:");
        AbfStatusLabel.setBounds(40, 600, 200, 20);
        AbfStatusLabel.setFont(new Font("Ubuntu", 0, 14));
        add(AbfStatusLabel);

        abfStatus = new JLabel("stop");
        abfStatus.setBounds(200, 600, 100, 20);
        abfStatus.setFont(new Font("Ubuntu", 0, 14));
        add(abfStatus);

        JLabel connetcionLabel = new JLabel("Connection Status:");
        connetcionLabel.setBounds(40, 620, 200, 20);
        connetcionLabel.setFont(new Font("Ubuntu", 0, 14));
        add(connetcionLabel);

        connectionStatus = new JLabel("disconnected");
        connectionStatus.setBounds(200, 620, 100, 20);
        connectionStatus.setFont(new Font("Ubuntu", 0, 14));
        add(connectionStatus);

        this.ResetButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.Reset();
        });
        this.SimulationAllTaskButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.SimulationAllProcessTask();
        });
        this.SimulationHeatTaskButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.SimulationWithHeatTask();
        });
        this.SimulationAirPumpTaskButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.SimulationWithAirPumpTask();
        });
        this.SimulationCleanTaskButton.addActionListener((ActionEvent e) -> {
            simulatorCenterController.CleanTask();
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

    public void setSimulationAllTaskButtonEnable(boolean enable) {
        if (enable) {
            SimulationAllTaskButton.setEnabled(true);
        } else {
            SimulationAllTaskButton.setEnabled(false);
        }
    }

    public void setSimulationHeatTaskButtonEnable(boolean enable) {
        if (enable) {
            SimulationHeatTaskButton.setEnabled(true);
        } else {
            SimulationHeatTaskButton.setEnabled(false);
        }
    }

    public void setSimulationAirPumpTaskButtonEnable(boolean enable) {
        if (enable) {
            SimulationAirPumpTaskButton.setEnabled(true);
        } else {
            SimulationAirPumpTaskButton.setEnabled(false);
        }
    }

    public void setSimulationCleanTaskButtonEnable(boolean enable) {
        if (enable) {
            SimulationCleanTaskButton.setEnabled(true);
        } else {
            SimulationCleanTaskButton.setEnabled(false);
        }
    }

    public void setStopButtonEnable(boolean enable) {
        if (enable) {
            StopButton.setEnabled(true);
        } else {
            StopButton.setEnabled(false);
        }
    }

    public void setFillingButtonEnable(boolean enable) {
        if (enable) {
            FillingButton.setEnabled(true);
        } else {
            FillingButton.setEnabled(false);
        }
    }

    public void setHeatingButtonEnable(boolean enable) {
        if (enable) {
            HeatingButton.setEnabled(true);
        } else {
            HeatingButton.setEnabled(false);
        }
    }

    public void setPumpingButtonEnable(boolean enable) {
        if (enable) {
            PumpingButton.setEnabled(true);
        } else {
            PumpingButton.setEnabled(false);
        }
    }

    public void setAirPumpingButtonEnable(boolean enable) {
        if (enable) {
            AirPumpingButton.setEnabled(true);
        } else {
            AirPumpingButton.setEnabled(false);
        }
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
