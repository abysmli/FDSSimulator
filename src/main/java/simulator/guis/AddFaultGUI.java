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

public class AddFaultGUI {

    JFrame AddFaultGUI = new JFrame("Add Fault");
    String selectedseries = "";
    String faultType = "known";
    JLabel ComponentInfo = new JLabel();
    JTextField shiftValueTextField = new JTextField();

    List<JButton> buttons = new ArrayList<>();
    SimulatorCenterController simulatorCenterController;

    public AddFaultGUI(SimulatorCenterController simulatorCenterController) {
        this.simulatorCenterController = simulatorCenterController;
        AddFaultGUI.setBounds(0, 0, 920, 600);
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
            });
            buttons.add(button);
            AddFaultGUI.getContentPane().add(button);
        }

        JLabel ComponentInfoTitle = new JLabel("Component Info");
        ComponentInfoTitle.setFont(new Font("Ubuntu", 0, 18));
        ComponentInfoTitle.setBounds(10, 320, 200, 30);
        AddFaultGUI.getContentPane().add(ComponentInfoTitle);

        ComponentInfo.setBounds(15, 340, 430, 200);
        AddFaultGUI.getContentPane().add(ComponentInfo);

        JLabel OptionsTitle = new JLabel("Fault Type (Send to FRS Server directly.)");
        OptionsTitle.setFont(new Font("Ubuntu", 0, 18));
        OptionsTitle.setBounds(440, 320, 400, 30);
        AddFaultGUI.getContentPane().add(OptionsTitle);

        JRadioButton knownRadio = new JRadioButton("Known Fault");
        JRadioButton unknownRadio = new JRadioButton("Unknown Fault");
        JRadioButton defectRadio = new JRadioButton("Defect Fault");
        JRadioButton shiftRadio = new JRadioButton("Shift Fault");
        knownRadio.setBounds(440, 350, 150, 40);
        knownRadio.setSelected(true);
        knownRadio.addActionListener((ActionEvent e) -> {
            faultType = "known";
            shiftValueTextField.setEnabled(false);
        });
        unknownRadio.setBounds(600, 350, 150, 40);
        unknownRadio.addActionListener((ActionEvent e) -> {
            faultType = "unknown";
            shiftValueTextField.setEnabled(false);
        });

        JLabel OptionsSymptomTitle = new JLabel("Fault Type (Detected by Simulator FDS)");
        OptionsSymptomTitle.setFont(new Font("Ubuntu", 0, 18));
        OptionsSymptomTitle.setBounds(440, 420, 400, 30);
        AddFaultGUI.getContentPane().add(OptionsSymptomTitle);

        defectRadio.setBounds(440, 450, 150, 40);
        defectRadio.addActionListener((ActionEvent e) -> {
            faultType = "defect";
            shiftValueTextField.setEnabled(false);
        });
        shiftRadio.setBounds(600, 450, 100, 40);
        shiftRadio.addActionListener((ActionEvent e) -> {
            faultType = "shift";
            shiftValueTextField.setEnabled(true);
        });
        ButtonGroup bG = new ButtonGroup();
        bG.add(knownRadio);
        bG.add(unknownRadio);
        bG.add(defectRadio);
        bG.add(shiftRadio);
        AddFaultGUI.add(knownRadio);
        AddFaultGUI.add(unknownRadio);
        AddFaultGUI.add(defectRadio);
        AddFaultGUI.add(shiftRadio);

        shiftValueTextField.setBounds(710, 450, 150, 40);
        shiftValueTextField.setEnabled(false);
        AddFaultGUI.add(shiftValueTextField);

        JButton addFaultButton = new JButton("Add Fault");
        addFaultButton.setBounds(760, 550, 140, 40);
        addFaultButton.addActionListener((ActionEvent e) -> {
            AddFaultGUI.setVisible(false);
            JSONObject fault = getFault();
            if (faultType.equals("known") || faultType.equals("unknown")) {
                simulatorCenterController.Stop();
                simulatorCenterController.getFDMController().sendFault(selectedseries, faultType, "Manual added Fault.");
            } else {
                DataBuffer.localFaultData.put(fault);
            }
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

    private JSONObject getFault() {
        JSONObject faultObj = new JSONObject();
        for (int i = 0; i < DataBuffer.data.length(); i++) {
            JSONObject component = DataBuffer.data.getJSONObject(i);
            if (component.getString("series").equals(selectedseries)) {
                int componentID = component.getInt("component_id");
                String shiftValue = "0.0";
                if (!shiftValueTextField.getText().isEmpty()) {
                    shiftValue = shiftValueTextField.getText();
                }
                faultObj.put("component_id", componentID);
                faultObj.put("series", selectedseries);
                faultObj.put("fault_type", faultType);
                faultObj.put("shift_value", shiftValue);
                faultObj.put("fault_desc", "Manual added Fault.");
                break;
            }
        }
        return faultObj;
    }
}
