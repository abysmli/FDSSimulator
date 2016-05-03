package com.fds.simulator.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;

import com.fds.simulator.controllers.AbfuellanlageLogic;
import com.fds.simulator.utils.FDSHttpRequestHandler;
import com.fds.simulator.utils.SimulatorSetting;

public class MenuGui extends JPanel {

    private static final long serialVersionUID = 1L;
    private final JSlider proportionalValveSlider;
    Gui gui;
    AbfuellanlageLogic logic;
    WatchListGUI watchListGUI;
    AddFaultGUI addFaultGUI;
    private final FDSHttpRequestHandler http = new FDSHttpRequestHandler(SimulatorSetting.FDSAddress);

    JLabel currentWaterLevel1, currentWaterLevel2, currentWaterTemp, abfStatus, connectionStatus;
    JButton StartButton, StopButton, FillingButton, HeatingButton, PumpingButton, AirPumpingButton, WatchListButton, AddFaultButton, RepairButton, ExecuteButton;

    /**
     * Create the panel.
     *
     * @throws Exception
     */
    public MenuGui() throws Exception {
        setBorder(new LineBorder(new Color(128, 128, 128)));
        setBounds(798, 0, 302, 800);
        setLayout(null);

        JLabel userInterfaceLabel = new JLabel("User Interface");
        userInterfaceLabel.setFont(new Font("Ubuntu", 1, 20));
        userInterfaceLabel.setForeground(new Color(0, 0, 211));
        userInterfaceLabel.setBounds(85, 10, 150, 20);
        add(userInterfaceLabel);

        JLabel waterLevelLabel1 = new JLabel("Water Level in Tank 101:");
        waterLevelLabel1.setBounds(40, 50, 200, 20);
        waterLevelLabel1.setFont(new Font("Ubuntu", 0, 14));
        add(waterLevelLabel1);

        currentWaterLevel1 = new JLabel("0.0 L");
        currentWaterLevel1.setBounds(230, 50, 100, 20);
        currentWaterLevel1.setFont(new Font("Ubuntu", 0, 14));
        add(currentWaterLevel1);

        JLabel waterLevelLabel2 = new JLabel("Water Level in Tank 102:");
        waterLevelLabel2.setBounds(40, 70, 200, 20);
        waterLevelLabel2.setFont(new Font("Ubuntu", 0, 14));
        add(waterLevelLabel2);

        currentWaterLevel2 = new JLabel("0.0 L");
        currentWaterLevel2.setBounds(230, 70, 100, 20);
        currentWaterLevel2.setFont(new Font("Ubuntu", 0, 14));
        add(currentWaterLevel2);

        JLabel waterTemperature = new JLabel("Water Temperature:");
        waterTemperature.setBounds(40, 90, 160, 20);
        waterTemperature.setFont(new Font("Ubuntu", 0, 14));
        add(waterTemperature);

        currentWaterTemp = new JLabel("0.0 °C");
        currentWaterTemp.setBounds(230, 90, 100, 20);
        currentWaterTemp.setFont(new Font("Ubuntu", 0, 14));
        add(currentWaterTemp);

        JLabel proportionalValveLabel = new JLabel("Proportional Valve");
        proportionalValveLabel.setBounds(85, 130, 138, 20);
        proportionalValveLabel.setFont(new Font("Ubuntu", 0, 14));
        add(proportionalValveLabel);

        JSeparator separator1 = new JSeparator();
        separator1.setBounds(10, 40, 280, 2);
        add(separator1);

        JSeparator separator2 = new JSeparator();
        separator2.setBounds(10, 120, 280, 2);
        add(separator2);

        JSeparator separator3 = new JSeparator();
        separator3.setBounds(10, 180, 280, 2);
        add(separator3);

        JSeparator separator4 = new JSeparator();
        separator4.setBounds(10, 250, 280, 2);
        add(separator4);

        JSeparator separator5 = new JSeparator();
        separator5.setBounds(10, 400, 280, 2);
        add(separator5);

        JSeparator separator6 = new JSeparator();
        separator6.setBounds(10, 550, 280, 2);
        add(separator6);

        proportionalValveSlider = new JSlider();
        proportionalValveSlider.setMajorTickSpacing(1);
        proportionalValveSlider.setBounds(49, 150, 200, 26);
        add(proportionalValveSlider);

        StartButton = new JButton("Start");
        StartButton.setBounds(16, 200, 128, 37);
        add(StartButton);

        StopButton = new JButton("Stop");
        StopButton.setEnabled(false);
        StopButton.setBounds(162, 200, 128, 37);
        add(StopButton);

        JLabel tasklabel = new JLabel("Functions");
        tasklabel.setFont(new Font("Ubuntu", 1, 20));
        tasklabel.setForeground(new Color(0, 0, 211));
        tasklabel.setBounds(105, 260, 200, 25);
        add(tasklabel);

        FillingButton = new JButton("Filling");
        FillingButton.setBounds(16, 300, 128, 37);

        add(FillingButton);

        HeatingButton = new JButton("Heating");
        HeatingButton.setBounds(162, 300, 128, 37);

        add(HeatingButton);

        PumpingButton = new JButton("Pumping");
        PumpingButton.setBounds(16, 350, 128, 37);

        add(PumpingButton);

        AirPumpingButton = new JButton("Air Pumping");
        AirPumpingButton.setBounds(162, 350, 128, 37);

        add(AirPumpingButton);

        JLabel controllerlabel = new JLabel("Controller");
        controllerlabel.setFont(new Font("Ubuntu", 1, 20));
        controllerlabel.setForeground(new Color(0, 0, 211));
        controllerlabel.setBounds(105, 410, 200, 25);
        add(controllerlabel);

        WatchListButton = new JButton("Watch List");
        WatchListButton.setBounds(16, 450, 128, 37);
        WatchListButton.addActionListener((ActionEvent e) -> {
            if (watchListGUI.isVisible()) {
                watchListGUI.setVisible(false);
            } else {
                watchListGUI.setVisible(true);
            }
        });
        add(WatchListButton);

        AddFaultButton = new JButton("Add Fault");
        AddFaultButton.setBounds(162, 450, 128, 37);
        AddFaultButton.addActionListener((ActionEvent e) -> {
            if (addFaultGUI.isVisible()) {
                addFaultGUI.setVisible(false);
            } else {
                addFaultGUI.setVisible(true);
            }
        });
        add(AddFaultButton);

        RepairButton = new JButton("Repair");
        RepairButton.setBounds(16, 500, 128, 37);
        add(RepairButton);

        ExecuteButton = new JButton("Set Strategy");
        ExecuteButton.setBounds(162, 500, 128, 37);
        ExecuteButton.setEnabled(false);
        
        add(ExecuteButton);

        JLabel statuslabel = new JLabel("Status");
        statuslabel.setFont(new Font("Ubuntu", 1, 20));
        statuslabel.setForeground(new Color(0, 0, 211));
        statuslabel.setBounds(120, 560, 180, 25);
        add(statuslabel);

        JLabel AbfStatusLabel = new JLabel("Abfuellanlage Status:");
        AbfStatusLabel.setBounds(40, 590, 200, 20);
        AbfStatusLabel.setFont(new Font("Ubuntu", 0, 14));
        add(AbfStatusLabel);

        abfStatus = new JLabel("stop");
        abfStatus.setBounds(200, 590, 100, 20);
        abfStatus.setFont(new Font("Ubuntu", 0, 14));
        add(abfStatus);

        JLabel connetcionLabel = new JLabel("Connection Status:");
        connetcionLabel.setBounds(40, 610, 200, 20);
        connetcionLabel.setFont(new Font("Ubuntu", 0, 14));
        add(connetcionLabel);

        connectionStatus = new JLabel("disconnected");
        connectionStatus.setBounds(200, 610, 100, 20);
        connectionStatus.setFont(new Font("Ubuntu", 0, 14));
        add(connectionStatus);
        checkConnection();
    }

    private void checkConnection() throws Exception {
        if (http.connectionStatus().getString("status").equals("running")) {
            setConnectionStatus("connected");
        } else {
            setConnectionStatus("disconnected");
        }
    }

    public void setStartButtonEnable(boolean enable) {
        if (enable) {
            StartButton.setEnabled(true);
        } else {
            StartButton.setEnabled(false);
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

    public void setExecuteButtonEnable(boolean enable) {
        if (enable) {
            ExecuteButton.setEnabled(true);
        } else {
            ExecuteButton.setEnabled(false);
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

    public void init(AbfuellanlageLogic logic, Gui gui) {
        this.gui = gui;
        this.logic = logic;
        this.watchListGUI = logic.getWatchListGUI();
        this.addFaultGUI = logic.getAddFaultGUI();
        this.RepairButton.addActionListener((ActionEvent e) -> {
            logic.Repair();
        });
        this.StartButton.addActionListener((ActionEvent e) -> {
            logic.Start();
        });
        this.StopButton.addActionListener((ActionEvent e) -> {
            logic.Stop();
        });
        this.AirPumpingButton.addActionListener((ActionEvent e) -> {
            logic.AirPumpingFunction();
        });
        this.FillingButton.addActionListener((ActionEvent e) -> {
            logic.FillingFunction();
        });
        this.PumpingButton.addActionListener((ActionEvent e) -> {
            logic.PumpingFunction();
        });
        this.HeatingButton.addActionListener((ActionEvent e) -> {
            logic.HeatingFunction();
        });
        this.ExecuteButton.addActionListener((ActionEvent e) -> {
            logic.executeStrategy();
        });
    }
}
