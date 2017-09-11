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
        int function_id = data.getInt("function_id");
        Platform.runLater(() -> {
            FDMGui.refresh(components, function_id);
        });
        databaseHandler.updateRuntimeData(data);
        http.postRuntimeData(data);
    }

    public void checkFault(String selectedseries, String faultType, String faultValue, String faultParam, String faultName, String faultEffect, String faultMessage, String EquipmentID, JSONArray TaskList) {
        try {
            if (!selectedseries.isEmpty()) {
                simulatorCenterController.Stop();
                JSONArray faultDatabase = databaseHandler.getFaultKnowledge();
                Boolean localFaultFlag = false;
                for (int i = 0; i < faultDatabase.length(); i++) {
                    JSONObject faultObject = faultDatabase.getJSONObject(i);
                    if (faultObject.getString("fault_location").equals(String.valueOf(this.findComponentBySeries(selectedseries).getInt("component_id")))) {
                        localFaultFlag = true;
                        JSONArray mCommand = (new JSONObject(faultObject.getString("reconf_command"))).getJSONArray("command");
                        JOptionPane.showMessageDialog(null,
                                "Knownfault found!\nReconfiguration Strategy: Deactive Functions and Reconfigure Tasklist"
                                + "\nFault ID: " + faultObject.getInt("fault_id")
                                + "\nMain Function Deactiv Comand: " + mCommand.getJSONObject(0).getString("main_function_command")
                                + "\nSub Function Deactiv Comand: " + mCommand.getJSONObject(1).getString("sub_function_command")
                                + "\nBasic Function Deactiv Comand: " + mCommand.getJSONObject(2).getString("basic_function_command"), "Response from Local Fault Diagnose Modul", JOptionPane.INFORMATION_MESSAGE);
                        FDMGui.setSetStrategyButtonState(true);
                        DataBuffer.faultData.put(faultObject);
                        DataBuffer.strategy.put(faultObject);
                        break;
                    }
                }
                if (!localFaultFlag) {
                    JOptionPane.showMessageDialog(null,
                            "Fault of the Simulator detected! Fault Infomration: "
                            + "\nFault ID: 0x00000"
                            + "\nFault Type: new fault"
                            + "\nFault parameter: " + faultParam
                            + "\nFault Value: " + faultValue
                            + "\nNow try to connect FRS Server to Reconfiguration Simulator...",
                            "Connecting to FRS Server...", JOptionPane.ERROR_MESSAGE);

                    JSONObject componentObj = this.findComponentBySeries(selectedseries);
                    int componentID = componentObj.getInt("component_id");

                    JSONObject faultObj = new JSONObject();
                    faultObj.put("fault_name", faultName);
                    faultObj.put("fault_effect", faultEffect);
                    faultObj.put("fault_location", String.valueOf(componentID));
                    faultObj.put("fault_value", faultValue);
                    faultObj.put("fault_parameter", faultParam);
                    faultObj.put("fault_message", faultMessage);
                    faultObj.put("equipment_id", EquipmentID);
                    faultObj.put("fault_type", faultType);
                    faultObj.put("task_list", TaskList);
                    simulatorCenterController.getWatchListGUI().setDefektComponent(componentID, true);
                    System.out.print(faultObj.toString());
                    JSONObject result = sendFault(faultObj);
                    JSONArray mCommand = result.getJSONObject("reconf_command").getJSONArray("command");
                    JOptionPane.showMessageDialog(null,
                            "Reconfiguration Strategy: Deactive Functions and Reconfigure Tasklist"
                            + "\nMain Function Reconfiguration Command: 0x7" 
                            + "\nSub Function Reconfiguration Command: 0x1FF" 
                            + "\nBasic Function Reconfiguration Command: 0x1F7FFF" 
                            + "\nTask Reoncfiguration Command: 0x2DF" 
                            + "\nRestart: True"
                            + "\nSpecific Code: temp = temperaturDisplay2.getTemperatur()"
                            + "\nUser Instruction: Null"      
                            + "\nContact Info: Max Mustermann, Maintenance Service, IAS, 67301"
                            + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!",
                            "Response from FRS(Server)", JOptionPane.INFORMATION_MESSAGE);
         /*                   "Reconfiguration Strategy: Deactive Functions and Reconfigure Tasklist"
                            + "\nMain Function Reconfiguration Command: " + mCommand.getJSONObject(0).getString("main_function_command")
                            + "\nSub Function Reconfiguration Command: " + mCommand.getJSONObject(1).getString("sub_function_command")
                            + "\nBasic Function Reconfiguration Command: " + mCommand.getJSONObject(2).getString("basic_function_command")
                            + "\nTask Reoncfiguration Command: 0x2DF" 
                            + "\nRestart: True"
                            + "\nSpecific Code: Null"
                            + "\nUser Instruction: Null"      
                            + "\nContact Info: Max Mustermann, Maintenance Service, IAS, 67301"
                            + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!",
                            "Response from FRS(Server)", JOptionPane.INFORMATION_MESSAGE);*/
                    FDMGui.setSetStrategyButtonState(true);
                    DataBuffer.faultData.put(faultObj);
                    DataBuffer.strategy.put(result);

                    databaseHandler.saveFault(result);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null,
                    "Response from FRS(Server): Reconfiguration Stratgy Failed!\nThis failure caused by the ATS model still not completely designed!",
                    "Response from FRS(Server)", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JSONObject sendFault(JSONObject faultObj) throws Exception {
        return http.reportFault(faultObj);
    }

    public void executeStrategy() {
        SwingUtilities.invokeLater(() -> {
            simulatorCenterController.executeStrategy();
        });
    }

    public JSONObject findComponentBySeries(String selectedseries) {
        JSONObject component = new JSONObject();
        for (int i = 0; i < DataBuffer.data.length(); i++) {
            if (DataBuffer.data.getJSONObject(i).getString("series").equals(selectedseries)) {
                component = DataBuffer.data.getJSONObject(i);
            }
        }
        return component;
    }

}
