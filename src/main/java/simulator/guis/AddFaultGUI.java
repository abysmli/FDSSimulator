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

public class AddFaultGUI {

    JFrame AddFaultGUI = new JFrame("Add Fault");
    String selectedseries = "";
    String faultType = "value";
    String faultParam = "temperatur";
    JLabel ComponentInfo = new JLabel();
//    JLabel FaultName = new JLabel("Fault Name:");
//    JLabel FaultEffect = new JLabel("Fault Effect:");
    JLabel FaultLocation = new JLabel("Fault Location:");
    JLabel EquipmentID = new JLabel("Equipment ID:            a100111");
    JLabel FaultValue = new JLabel("Fault Value:");
//    JTextField FaultNameField = new JTextField();
//    JTextField FaultEffectField = new JTextField();
    JTextField FaultValueField = new JTextField();

    List<JButton> buttons = new ArrayList<>();
    SimulatorCenterController simulatorCenterController;

    public AddFaultGUI(SimulatorCenterController simulatorCenterController) {
        this.simulatorCenterController = simulatorCenterController;
        AddFaultGUI.setBounds(0, 0, 920, 650);
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
                selectedseries = ((JButton) e.getSource()).getText();
                enableButtons();
                ((JButton) e.getSource()).setEnabled(false);
                ComponentInfo.setText(((JButton) e.getSource()).getToolTipText());
                FaultLocation.setText("Fault Location:          " + selectedseries);
            });
            buttons.add(button);
            AddFaultGUI.getContentPane().add(button);
        }

        JLabel ComponentInfoTitle = new JLabel("Component Info");
        ComponentInfoTitle.setFont(new Font("Ubuntu", 0, 18));
        ComponentInfoTitle.setBounds(10, 350, 200, 30);
        AddFaultGUI.getContentPane().add(ComponentInfoTitle);

        ComponentInfo.setBounds(15, 370, 430, 200);
        AddFaultGUI.getContentPane().add(ComponentInfo);

        JLabel FaultBlockTitle = new JLabel("Fault Setting:");
        FaultBlockTitle.setFont(new Font("Ubuntu", 0, 18));
        FaultBlockTitle.setBounds(440, 320, 400, 30);
        AddFaultGUI.getContentPane().add(FaultBlockTitle);

//        FaultName.setFont(new Font("Ubuntu", 0, 15));
//        FaultName.setBounds(440, 350, 150, 30);
//        AddFaultGUI.getContentPane().add(FaultName);
//        FaultNameField.setBounds(570, 350, 150, 27);
//        AddFaultGUI.add(FaultNameField);
//
//        FaultEffect.setFont(new Font("Ubuntu", 0, 15));
//        FaultEffect.setBounds(440, 380, 400, 30);
//        AddFaultGUI.getContentPane().add(FaultEffect);
//        FaultEffectField.setBounds(570, 380, 150, 27);
//        AddFaultGUI.add(FaultEffectField);
        FaultLocation.setFont(new Font("Ubuntu", 0, 15));
        FaultLocation.setBounds(440, 350, 400, 30);
        AddFaultGUI.getContentPane().add(FaultLocation);

        EquipmentID.setFont(new Font("Ubuntu", 0, 15));
        EquipmentID.setBounds(440, 380, 400, 30);
        AddFaultGUI.getContentPane().add(EquipmentID);

        ButtonGroup valueGroup = new ButtonGroup();

        JRadioButton valueRadio = new JRadioButton("Value Shift Fault");
        JRadioButton shiftRadio = new JRadioButton("Changerate Fault");
        JRadioButton defektRadio = new JRadioButton("Defekt Fault");
        valueRadio.setBounds(440, 410, 150, 40);
        valueRadio.setSelected(true);
        valueRadio.addActionListener((ActionEvent e) -> {
            faultType = "value";
            FaultValueField.setEnabled(true);
        });
        shiftRadio.setBounds(600, 410, 150, 40);
        shiftRadio.addActionListener((ActionEvent e) -> {
            faultType = "changerate";
            FaultValueField.setEnabled(true);
        });

        defektRadio.setBounds(760, 410, 150, 40);
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
        FaultValue.setBounds(440, 450, 400, 30);
        AddFaultGUI.getContentPane().add(FaultValue);
        FaultValueField.setBounds(570, 450, 150, 27);
        AddFaultGUI.add(FaultValueField);
        
        ButtonGroup paramGroup = new ButtonGroup();
        JRadioButton temperaturRadio = new JRadioButton("Temperatur");
        JRadioButton airpressureRadio = new JRadioButton("Air Pressure");
        JRadioButton airflowrateRadio = new JRadioButton("Air Flowrate");
        JRadioButton waterlevelRadio = new JRadioButton("Water Level");
        JRadioButton waterpressureRadio = new JRadioButton("Water Pressure");
        JRadioButton waterflowrateRadio = new JRadioButton("Water Flowrate");
        
        temperaturRadio.setBounds(440, 480, 150, 40);
        temperaturRadio.setSelected(true);
        temperaturRadio.addActionListener((ActionEvent e) -> {
            faultParam = "temperatur";
        });
        airpressureRadio.setBounds(600, 480, 150, 40);
        airpressureRadio.addActionListener((ActionEvent e) -> {
            faultParam = "airpressure";
        });

        airflowrateRadio.setBounds(760, 480, 150, 40);
        airflowrateRadio.addActionListener((ActionEvent e) -> {
            faultParam = "airflowrate";
        });
        
        waterlevelRadio.setBounds(440, 510, 150, 40);
        waterlevelRadio.addActionListener((ActionEvent e) -> {
            faultParam = "waterlevel";
        });
        
        waterpressureRadio.setBounds(600, 510, 150, 40);
        waterpressureRadio.addActionListener((ActionEvent e) -> {
            faultParam = "waterpressure";
        });

        waterflowrateRadio.setBounds(760, 510, 150, 40);
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
        
        JButton addFaultButton = new JButton("Add Fault");
        addFaultButton.setBounds(760, 550, 140, 40);
        addFaultButton.addActionListener((ActionEvent e) -> {
            AddFaultGUI.setVisible(false);
            simulatorCenterController.Stop();
            JSONArray mTaskList = simulatorCenterController.tasksList.getList();
            simulatorCenterController.getFDMController().checkFault(selectedseries, faultType, FaultValueField.getText(), faultParam, "", "", "", "a100111", mTaskList);
        });
        AddFaultGUI.getContentPane().add(addFaultButton);
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
