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
        String timeStamp = data.getString("stamp_time");
        int process_id = data.getInt("process_id");
        Platform.runLater(() -> {
            FDMGui.refresh(components, process_id);
        });
        http.postComponentsValue(data);
        symptomAnalyse(components, process_id);
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

    private void symptomAnalyse(JSONArray data, int process_id) {
        boolean faultFlag = false;
        switch (process_id) {
            case 1:
                if (!DataBuffer.data.getJSONObject(22).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(22).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(19).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(19).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(5).getString("value").equals("off")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(5).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(6).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(6).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(20).getString("value")) > 9 || Double.valueOf(DataBuffer.data.getJSONObject(20).getString("value")) < 5) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(20).getString("series"), "shift", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(21).getString("value")) > 8 || Double.valueOf(DataBuffer.data.getJSONObject(21).getString("value")) < 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(21).getString("series"), "shift", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(14).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(14).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(13).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(13).getString("series"), "defect", "Auto detected Fault!");
                }
                break;
            case 2:
                if (!DataBuffer.data.getJSONObject(25).getString("value").equals("OK")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(25).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(24).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(24).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(4).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(4).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(3).getString("value").equals("OK")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(3).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(7).getString("value")) > 0.9 || Double.valueOf(DataBuffer.data.getJSONObject(7).getString("value")) < 0.1) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(7).getString("series"), "shift", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(20).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(20).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(14).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(14).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(13).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(13).getString("series"), "defect", "Auto detected Fault!");
                }
                break;
            case 3:
                if (!DataBuffer.data.getJSONObject(1).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(1).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(3).getString("value").equals("OK")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(3).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(20).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(20).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(14).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(14).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(13).getString("value")) != 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(13).getString("series"), "defect", "Auto detected Fault!");
                }
                break;
            case 4:
                if (!DataBuffer.data.getJSONObject(18).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(18).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(12).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(12).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(11).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(11).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(10).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(10).getString("series"), "defect", "Auto detected Fault!");
                }
                if (!DataBuffer.data.getJSONObject(23).getString("value").equals("on")) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(23).getString("series"), "defect", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(14).getString("value")) > 10 || Double.valueOf(DataBuffer.data.getJSONObject(14).getString("value")) < 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(14).getString("series"), "shift", "Auto detected Fault!");
                }
                if (Double.valueOf(DataBuffer.data.getJSONObject(13).getString("value")) > 60 || Double.valueOf(DataBuffer.data.getJSONObject(13).getString("value")) < 0) {
                    faultFlag = true;
                    sendFault(DataBuffer.data.getJSONObject(13).getString("series"), "shift", "Auto detected Fault!");
                }
                break;
            default:
                break;
        }
    }

    public void executeStrategy() {
        SwingUtilities.invokeLater(() -> {
            simulatorCenterController.executeStrategy();
        });
    }

}
