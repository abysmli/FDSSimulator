/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.controllers;

import simulator.controllers.SimulatorCenterController;
import simulator.utils.DataBuffer;
import simulator.utils.FDSHttpRequestHandler;
import fds.FDSGUI;
import fds.model.DatabaseHandler;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.utils.ErrorLogger;

/**
 *
 * @author abysmli
 */
public class FDSController {

    private final FDSHttpRequestHandler http;
    private final FDSGUI FDMGui;
    private final SimulatorCenterController simulatorCenterController;
    private final DatabaseHandler databaseHandler;

    public FDSController(FDSGUI FDMGui, FDSHttpRequestHandler http, SimulatorCenterController simulatorCenterController) {
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
                String mFaultLocation = this.findID(selectedseries);
                for (int i = 0; i < faultDatabase.length(); i++) {
                    JSONObject faultObject = faultDatabase.getJSONObject(i);
                    if (faultObject.getString("fault_location").equals(mFaultLocation)) {
                        localFaultFlag = true;
                        JSONArray mCommand = (new JSONObject(faultObject.getString("reconf_command"))).getJSONArray("command");
                        JSONObject reconfObjg = new JSONObject(faultObject.getString("reconf_command"));
                        JOptionPane.showMessageDialog(null,
                                "Knownfault found!\nReconfiguration Strategy: Deactive Functions and Reconfigure Tasklist"
                                + "\nMain Function Reconfiguration Command: 0x" + mCommand.getJSONObject(0).getString("main_function_command")
                                + "\nSub Function Reconfiguration Command: 0x" + mCommand.getJSONObject(1).getString("sub_function_command")
                                + "\nBasic Function Reconfiguration Command: 0x" + mCommand.getJSONObject(2).getString("basic_function_command")
                                + "\nTask Reoncfiguration Command: 0x" + mCommand.getJSONObject(0).getString("main_function_command") + mCommand.getJSONObject(1).getString("sub_function_command") + mCommand.getJSONObject(2).getString("basic_function_command")
                                + "\nRestart: " + reconfObjg.getString("special_code")
                                + "\nSpecific Code: " + reconfObjg.getString("special_code")
                                + "\nUser Instruction: Null"
                                + "\nContact Info: " + reconfObjg.getJSONObject("personal_data").getString("General_Techniker")
                                + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!", "Response from Local Fault Diagnose Modul", JOptionPane.INFORMATION_MESSAGE);
                        FDMGui.setReconfigurationStrategy(faultObject);
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

                    JSONObject faultObj = new JSONObject();
                    faultObj.put("fault_name", faultName);
                    faultObj.put("fault_effect", faultEffect);
                    faultObj.put("fault_location", mFaultLocation);
                    faultObj.put("fault_value", faultValue);
                    faultObj.put("fault_parameter", faultParam);
                    faultObj.put("fault_message", faultMessage);
                    faultObj.put("equipment_id", EquipmentID);
                    faultObj.put("fault_type", faultType);
                    faultObj.put("task_list", TaskList);
//                    simulatorCenterController.getWatchListGUI().setDefektComponent(componentID, true);
                    System.out.println("Sended Fault Object: ");
                    System.out.println(faultObj.toString());
                    JSONObject result = sendFault(faultObj);
                    JSONArray mCommand = result.getJSONObject("reconf_command").getJSONArray("command");
                    JSONObject reconfObjg = result.getJSONObject("reconf_command");
                    System.out.println("Response from DHFRS: ");
                    System.out.println(result.toString());
                    FDMGui.showAlert("Response from FRS(Server)", EquipmentID, Alert.AlertType.ERROR);
                    JOptionPane.showMessageDialog(null,
                            "Reconfiguration Strategy: Deactive Functions and Reconfigure Tasklist"
                            + "\nMain Function Reconfiguration Command: 0x" + mCommand.getJSONObject(0).getString("main_function_command")
                            + "\nSub Function Reconfiguration Command: 0x" + mCommand.getJSONObject(1).getString("sub_function_command")
                            + "\nBasic Function Reconfiguration Command: 0x" + mCommand.getJSONObject(2).getString("basic_function_command")
                            + "\nTask Reoncfiguration Command: 0x" + mCommand.getJSONObject(0).getString("main_function_command") + mCommand.getJSONObject(1).getString("sub_function_command") + mCommand.getJSONObject(2).getString("basic_function_command")
                            + "\nRestart: " + reconfObjg.getString("special_code")
                            + "\nSpecific Code: " + reconfObjg.getString("special_code")
                            + "\nUser Instruction: Null"
                            + "\nContact Info: " + reconfObjg.getJSONObject("personal_data").getString("General_Techniker")
                            + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!",
                            "Response from FRS(Server)", JOptionPane.INFORMATION_MESSAGE);
                    FDMGui.setReconfigurationStrategy(result);
                    DataBuffer.faultData.put(faultObj);
                    DataBuffer.strategy.put(result);

                    databaseHandler.saveFault(result);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Response from FRS(Server): Processing Failed!\n Internal Error!",
                    "Response from FRS(Server)", JOptionPane.ERROR_MESSAGE);
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
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

    public String findID(String selectedseries) {
        String[] ids = selectedseries.split(", ");
        List<String> idList = Arrays.asList(ids);
        String idString = "";
        for (int i = 0; i < idList.size(); i++) {
            String id = idList.get(i);
            if ((id.length() < 9 ? id : id.substring(0, 9)).equals("Subsystem")) {
                id = "S" + id.substring(10);
            } else {
                for (int j = 0; j < DataBuffer.data.length(); j++) {
                    if (DataBuffer.data.getJSONObject(j).getString("series").equals(id)) {
                        id = "C" + String.valueOf(DataBuffer.data.getJSONObject(i).getInt("component_id"));
                    }
                }
            }
            if (i == 0) {
                idString = id;
            } else {
                idString += "," + id;
            }
            idList.set(i, id);
        }

        return idString;
    }

    public void setFDSStatus(String status) {
        FDMGui.setFDSStatus("Status: " + status);
    }
}
