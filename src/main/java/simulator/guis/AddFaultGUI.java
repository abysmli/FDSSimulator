package simulator.guis;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.json.JSONObject;

import simulator.controllers.SimulatorCenterController;
import simulator.utils.DataBuffer;
import javax.swing.JTextField;
import org.json.JSONArray;
import simulator.utils.SimulatorSetting;

public class AddFaultGUI {

    JFrame AddFaultGUI = new JFrame("Add Fault");
    String selectedseries = "";
    String faultType = "value";
    String faultParam = "temperatur";
    JLabel ComponentInfo = new JLabel();
//    JLabel FaultName = new JLabel("Fault Name:");
//    JLabel FaultEffect = new JLabel("Fault Effect:");
    JLabel FaultLocation = new JLabel("Fault Location:");
    JLabel EquipmentID = new JLabel("Equipment ID: " + SimulatorSetting.EquipmentID);
    JLabel FaultValue = new JLabel("Fault Value:");
//    JTextField FaultNameField = new JTextField();
//    JTextField FaultEffectField = new JTextField();
    JTextField FaultValueField = new JTextField();

    List<JButton> buttons = new ArrayList<>();
    SimulatorCenterController simulatorCenterController;

    public AddFaultGUI(SimulatorCenterController simulatorCenterController) {
        this.simulatorCenterController = simulatorCenterController;
        AddFaultGUI.setBounds(0, 0, 920, 780);
        AddFaultGUI.setResizable(false);
        AddFaultGUI.setLayout(null);
        JLabel ComponentsTitle = new JLabel("Select Component");
        ComponentsTitle.setFont(new Font("Ubuntu", 0, 18));
        ComponentsTitle.setBounds(10, 10, 200, 30);
        AddFaultGUI.getContentPane().add(ComponentsTitle);
        for (int i = 0; i < DataBuffer.initData.length(); i++) {
            JSONObject obj = DataBuffer.initData.getJSONObject(i);
            JButton button = new JButton(obj.getString("series"));
            button.setBounds(10 + i % 6 * 150, 50 + i / 6 * 50, 140, 40);
            button.setFocusable(false);
            button.setToolTipText("<html><b>Component ID:</b> " + obj.getInt("component_id")
                    + "<br><b>Component Name:</b> " + obj.getString("component_name") + "<br><b>Component Desc:</b> "
                    + obj.getString("component_desc") + "<br><b>Series:</b> " + obj.getString("series")
                    + "<br><b>Type:</b> " + obj.getString("type") + "<br><b>Activition:</b> "
                    + obj.getString("activition") + "<br><b>Status:</b> " + obj.getString("status") + "</html>");
            button.addActionListener((ActionEvent e) -> {
                if (selectedseries.isEmpty()) {
                    selectedseries = ((JButton) e.getSource()).getText();
                } else {
                    selectedseries += ", " + ((JButton) e.getSource()).getText();
                }
                ((JButton) e.getSource()).setEnabled(!((JButton) e.getSource()).isEnabled());
                ComponentInfo.setText(((JButton) e.getSource()).getToolTipText());
                FaultLocation.setText("Fault Location: " + selectedseries);
            });
            buttons.add(button);
            AddFaultGUI.getContentPane().add(button);
        }
        JLabel SubsystemsTitle = new JLabel("Select Subsystem");
        SubsystemsTitle.setFont(new Font("Ubuntu", 0, 18));
        SubsystemsTitle.setBounds(10, 350, 300, 30);
        AddFaultGUI.getContentPane().add(SubsystemsTitle);
        for (int i = 0; i < DataBuffer.SubSystems.length(); i++) {
            JSONObject obj = DataBuffer.SubSystems.getJSONObject(i);
            JButton button = new JButton("Subsystem " + obj.getInt("subsystem_id"));
            button.setBounds(10 + i % 6 * 150, 390 + i / 6 * 50, 140, 40);
            button.setFocusable(false);
            button.setToolTipText("<html><b>Subsystem ID:</b> " + obj.getInt("subsystem_id")
                    + "<br><b>Subsystem Name:</b> " + obj.getString("subsystem_desc") + "<br><b>Status:</b> " + obj.getString("status") + "</html>");
            button.addActionListener((ActionEvent e) -> {
                if (selectedseries.isEmpty()) {
                    selectedseries = ((JButton) e.getSource()).getText();
                } else {
                    selectedseries += ", " + ((JButton) e.getSource()).getText();
                }
                ((JButton) e.getSource()).setEnabled(!((JButton) e.getSource()).isEnabled());
                ComponentInfo.setText(((JButton) e.getSource()).getToolTipText());
                FaultLocation.setText("Fault Location: " + selectedseries);
            });
            buttons.add(button);
            AddFaultGUI.getContentPane().add(button);
        }

        JLabel ComponentInfoTitle = new JLabel("Component Info");
        ComponentInfoTitle.setFont(new Font("Ubuntu", 0, 18));
        ComponentInfoTitle.setBounds(10, 450, 200, 30);
        AddFaultGUI.getContentPane().add(ComponentInfoTitle);

        ComponentInfo.setBounds(15, 470, 430, 200);
        AddFaultGUI.getContentPane().add(ComponentInfo);

        JLabel FaultBlockTitle = new JLabel("Fault Setting:");
        FaultBlockTitle.setFont(new Font("Ubuntu", 0, 18));
        FaultBlockTitle.setBounds(440, 450, 400, 30);
        AddFaultGUI.getContentPane().add(FaultBlockTitle);

        FaultLocation.setFont(new Font("Ubuntu", 0, 15));
        FaultLocation.setBounds(440, 480, 400, 30);
        AddFaultGUI.getContentPane().add(FaultLocation);

        EquipmentID.setFont(new Font("Ubuntu", 0, 15));
        EquipmentID.setBounds(440, 510, 400, 30);
        AddFaultGUI.getContentPane().add(EquipmentID);

        ButtonGroup valueGroup = new ButtonGroup();

        JRadioButton valueRadio = new JRadioButton("Value Shift Fault");
        JRadioButton shiftRadio = new JRadioButton("Changerate Fault");
        JRadioButton defektRadio = new JRadioButton("Defekt Fault");
        valueRadio.setBounds(440, 540, 150, 40);
        valueRadio.setSelected(true);
        valueRadio.addActionListener((ActionEvent e) -> {
            faultType = "value";
            FaultValueField.setEnabled(true);
        });
        shiftRadio.setBounds(600, 540, 150, 40);
        shiftRadio.addActionListener((ActionEvent e) -> {
            faultType = "changerate";
            FaultValueField.setEnabled(true);
        });

        defektRadio.setBounds(760, 540, 150, 40);
        defektRadio.addActionListener((ActionEvent e) -> {
            faultType = "defect";
            FaultValueField.setEnabled(false);
        });

        valueGroup.add(valueRadio);
        valueGroup.add(shiftRadio);
        valueGroup.add(defektRadio);
        AddFaultGUI.getContentPane().add(valueRadio);
        AddFaultGUI.getContentPane().add(shiftRadio);
        AddFaultGUI.getContentPane().add(defektRadio);

        FaultValue.setFont(new Font("Ubuntu", 0, 15));
        FaultValue.setBounds(440, 580, 400, 30);
        AddFaultGUI.getContentPane().add(FaultValue);
        FaultValueField.setBounds(570, 580, 150, 27);
        AddFaultGUI.add(FaultValueField);

        ButtonGroup paramGroup = new ButtonGroup();
        JRadioButton temperaturRadio = new JRadioButton("Temperatur");
        JRadioButton airpressureRadio = new JRadioButton("Air Pressure");
        JRadioButton airflowrateRadio = new JRadioButton("Air Flowrate");
        JRadioButton waterlevelRadio = new JRadioButton("Water Level");
        JRadioButton waterpressureRadio = new JRadioButton("Water Pressure");
        JRadioButton waterflowrateRadio = new JRadioButton("Water Flowrate");

        temperaturRadio.setBounds(440, 610, 150, 40);
        temperaturRadio.setSelected(true);
        temperaturRadio.addActionListener((ActionEvent e) -> {
            faultParam = "temperatur";
        });
        airpressureRadio.setBounds(600, 610, 150, 40);
        airpressureRadio.addActionListener((ActionEvent e) -> {
            faultParam = "airpressure";
        });

        airflowrateRadio.setBounds(760, 610, 150, 40);
        airflowrateRadio.addActionListener((ActionEvent e) -> {
            faultParam = "airflowrate";
        });

        waterlevelRadio.setBounds(440, 640, 150, 40);
        waterlevelRadio.addActionListener((ActionEvent e) -> {
            faultParam = "waterlevel";
        });

        waterpressureRadio.setBounds(600, 640, 150, 40);
        waterpressureRadio.addActionListener((ActionEvent e) -> {
            faultParam = "waterpressure";
        });

        waterflowrateRadio.setBounds(760, 640, 150, 40);
        waterflowrateRadio.addActionListener((ActionEvent e) -> {
            faultParam = "waterflowrate";
        });

        paramGroup.add(temperaturRadio);
        paramGroup.add(airpressureRadio);
        paramGroup.add(airflowrateRadio);
        paramGroup.add(waterlevelRadio);
        paramGroup.add(waterpressureRadio);
        paramGroup.add(waterflowrateRadio);
        AddFaultGUI.getContentPane().add(temperaturRadio);
        AddFaultGUI.getContentPane().add(airpressureRadio);
        AddFaultGUI.getContentPane().add(airflowrateRadio);
        AddFaultGUI.getContentPane().add(waterlevelRadio);
        AddFaultGUI.getContentPane().add(waterpressureRadio);
        AddFaultGUI.getContentPane().add(waterflowrateRadio);

        JButton addFaultButton = new JButton("Add & Send Fault");
        addFaultButton.setBounds(740, 690, 140, 40);
        addFaultButton.addActionListener((ActionEvent e) -> {
            AddFaultGUI.setVisible(false);
            simulatorCenterController.Stop();
            JSONArray mTaskList = simulatorCenterController.tasksList.getList();
            simulatorCenterController.getFDMController().checkFault(selectedseries, faultType, FaultValueField.getText(), faultParam, "", "", "", SimulatorSetting.EquipmentID, mTaskList);
        });
        AddFaultGUI.getContentPane().add(addFaultButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(580, 690, 140, 40);
        resetButton.addActionListener((ActionEvent e) -> {
            enableButtons();
            selectedseries = "";
            faultType = "value";
            FaultValueField.setText("");
            ComponentInfo.setText("");
            FaultLocation.setText("Fault Location: ");
        });
        AddFaultGUI.getContentPane().add(resetButton);
    }

    private void enableButtons() {
        buttons.forEach(button -> {
            button.setEnabled(true);
        });
    }

    public void setVisible(String series) {
        buttons.forEach(button -> {
            if (button.getText().equals(series)) {
                selectedseries = button.getText();
                enableButtons();
                button.setEnabled(false);
                ComponentInfo.setText(button.getToolTipText());
            }
        });
        AddFaultGUI.setVisible(true);
    }

    public void setVisible(boolean b) {
        AddFaultGUI.setVisible(b);
    }

    public boolean isVisible() {
        return AddFaultGUI.isVisible();
    }
}
