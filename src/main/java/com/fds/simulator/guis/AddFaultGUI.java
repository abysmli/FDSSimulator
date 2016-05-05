package com.fds.simulator.guis;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import org.json.JSONObject;

import com.fds.simulator.controllers.SimulatorCenterController;
import com.fds.simulator.utils.DataBuffer;

public class AddFaultGUI {

    JFrame AddFaultGUI = new JFrame("Add Fault");
    String selectedseries = "";
    String faultType = "known";
    List<JButton> buttons = new ArrayList<>();
    SimulatorCenterController simulatorCenterController;

    public AddFaultGUI(SimulatorCenterController simulatorCenterController) {
        this.simulatorCenterController = simulatorCenterController;
        AddFaultGUI.setBounds(0, 0, 920, 500);
        AddFaultGUI.setResizable(false);
        AddFaultGUI.setLayout(null);
        JLabel ComponentsTitle = new JLabel("Select Component");
        ComponentsTitle.setFont(new Font("Ubuntu", 1, 18));
        ComponentsTitle.setBounds(10, 10, 200, 30);
        AddFaultGUI.getContentPane().add(ComponentsTitle);
        for (int i = 0; i < DataBuffer.data.length(); i++) {
            JSONObject obj = DataBuffer.data.getJSONObject(i);
            JButton button = new JButton(obj.getString("series"));
            button.setBounds(10 + i % 6 * 150, 50 + i / 6 * 50, 140, 40);
            button.setToolTipText("<html><b>Component ID:</b> " + obj.getInt("component_id")
                    + "<br><b>Component Name:</b> " + obj.getString("component_name") + "<br><b>Component Desc:</b> "
                    + obj.getString("component_desc") + "<br><b>Series:</b> " + obj.getString("series")
                    + "<br><b>Type:</b> " + obj.getString("type") + "<br><b>Activition:</b> "
                    + obj.getString("activition") + "<br><b>Status:</b> " + obj.getString("status") + "</html>");
            button.addActionListener((ActionEvent e) -> {
                selectedseries = ((JButton) e.getSource()).getText();
                enableButtons();
                ((JButton) e.getSource()).setEnabled(false);
            });
            buttons.add(button);
            AddFaultGUI.getContentPane().add(button);
        }

        JSeparator separator1 = new JSeparator();
        separator1.setBounds(10, 310, 900, 2);
        AddFaultGUI.getContentPane().add(separator1);

        JLabel OptionsTitle = new JLabel("Fault Type");
        OptionsTitle.setFont(new Font("Ubuntu", 1, 18));
        OptionsTitle.setBounds(10, 320, 200, 30);
        AddFaultGUI.getContentPane().add(OptionsTitle);

        JRadioButton knownRadio = new JRadioButton("Known Fault");
        JRadioButton unknownRadio = new JRadioButton("Unknown Fault");
        knownRadio.setBounds(10, 350, 150, 40);
        knownRadio.setSelected(true);
        knownRadio.addActionListener((ActionEvent e) -> {
            faultType = "known";
        });
        unknownRadio.setBounds(170, 350, 150, 40);
        unknownRadio.addActionListener((ActionEvent e) -> {
            faultType = "unknown";
        });
        ButtonGroup bG = new ButtonGroup();
        bG.add(knownRadio);
        bG.add(unknownRadio);
        AddFaultGUI.add(knownRadio);
        AddFaultGUI.add(unknownRadio);

        JSeparator separator2 = new JSeparator();
        separator2.setBounds(10, 390, 900, 2);
        AddFaultGUI.getContentPane().add(separator2);

        JButton addFaultButton = new JButton("Add Fault");
        addFaultButton.setBounds(760, 410, 140, 40);
        addFaultButton.addActionListener((ActionEvent e) -> {
            AddFaultGUI.setVisible(false);
            simulatorCenterController.sendFault(selectedseries, faultType);
        });

        AddFaultGUI.getContentPane().add(addFaultButton);
    }

    private void enableButtons() {
        buttons.forEach(button -> {
            button.setEnabled(true);
        });
    }

    public void setVisible(boolean b) {
        AddFaultGUI.setVisible(b);
    }

    public boolean isVisible() {
        return AddFaultGUI.isVisible();
    }
}
