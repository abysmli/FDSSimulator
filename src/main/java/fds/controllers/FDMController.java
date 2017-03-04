/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.controllers;

import simulator.controllers.SimulatorCenterController;
import simulator.utils.DataBuffer;
import simulator.utils.FDSHttpRequestHandler;
import fds.FDMGUI;
import fds.model.DatabaseHandler;
import javafx.application.Platform;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class FDMController {

    private final FDSHttpRequestHandler http;
    private final FDMGUI FDMGui;
    private final SimulatorCenterController simulatorCenterController;
    private final DatabaseHandler databaseHandler;

    public FDMController(FDMGUI FDMGui, FDSHttpRequestHandler http, SimulatorCenterController simulatorCenterController) {
        this.http = http;
        this.FDMGui = FDMGui;
        this.simulatorCenterController = simulatorCenterController;
        this.databaseHandler = new DatabaseHandler();
    }

    public void checkData(JSONObject data) throws Exception {
        JSONArray components = data.getJSONArray("components");
        int process_id = data.getInt("process_id");
        Platform.runLater(() -> {
            FDMGui.refresh(components, process_id);
        });
        http.postComponentsValue(data);
    }

    public void sendFault(String selectedseries, String faultType, String faultDesc) {
        try {
            if (!selectedseries.isEmpty()) {
                simulatorCenterController.Stop();
                JSONArray faultDatabase = databaseHandler.getFaults();
                Boolean localFaultFlag = false;
                for (int i = 0; i < faultDatabase.length(); i++) {
                    JSONObject faultObject = faultDatabase.getJSONObject(i);
                    if (faultObject.getString("series") == selectedseries && faultObject.getString("fault_type") == faultType) {
                        localFaultFlag = true;
                        JOptionPane.showMessageDialog(null,
                                "Reconfiguration Strategy: Deactive Mainfunction and Reconfigure Tasklist "
                                + faultObject.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                .toString()
                                + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!",
                                "Response from Local Fault Diagnose Modul", JOptionPane.INFORMATION_MESSAGE);
                        FDMGui.setSetStrategyButtonState(true);
                        DataBuffer.faultData.put(faultObject);
                        DataBuffer.strategy.put(faultObject);
                    }
                }
                if (!localFaultFlag) {
                    if (faultType.equals("defect") || faultType.equals("shift")) {
                        JOptionPane.showMessageDialog(null,
                                "Fault of the Simulator detected by FDS\nComponent: " + selectedseries + "\nNow try to connect FDS Server to Reconfiguration Simulator...",
                                "Auto Detected Fault", JOptionPane.ERROR_MESSAGE);
                    }
                    for (int i = 0; i < DataBuffer.data.length(); i++) {
                        JSONObject component = DataBuffer.data.getJSONObject(i);
                        if (component.getString("series").equals(selectedseries)) {
                            JSONObject faultObj = new JSONObject();
                            int componentID = component.getInt("component_id");
                            faultObj.put("component_id", componentID);
                            faultObj.put("series", selectedseries);
                            faultObj.put("fault_type", faultType);
                            faultObj.put("fault_desc", faultDesc);
                            simulatorCenterController.getWatchListGUI().setDefektComponent(componentID, true);
                            JSONObject result = http.reportFault(faultObj);
                            JOptionPane.showMessageDialog(null,
                                    "Reconfiguration Strategy: Deactive Mainfunction "
                                    + result.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                    .toString()
                                    + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!",
                                    "Response from FRS(Server)", JOptionPane.INFORMATION_MESSAGE);
                            FDMGui.setSetStrategyButtonState(true);
                            DataBuffer.faultData.put(faultObj);
                            DataBuffer.strategy.put(result);
                            result.put("series", selectedseries);
                            databaseHandler.saveFault(result);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,
                    "Response from FRS(Server): Reconfiguration Stratgy Failed!\nThis failure caused by the ATS model still not completely designed!",
                    "Response from FRS(Server)", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void executeStrategy() {
        SwingUtilities.invokeLater(() -> {
            simulatorCenterController.executeStrategy();
        });
    }

}
