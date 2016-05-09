package com.fds.simulator.controllers;

import com.fds.simulator.Simulator;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fds.simulator.guis.AddFaultGUI;
import com.fds.simulator.guis.Gui;
import com.fds.simulator.guis.MenuGui;
import com.fds.simulator.guis.WatchListGUI;
import com.fds.simulator.utils.DataBuffer;
import com.fds.simulator.utils.ErrorLogger;
import com.fds.simulator.utils.FDSHttpRequestHandler;
import com.fds.simulator.utils.SimulatorSetting;

public class SimulatorCenterController {

    private final Simulator application;
    private final Gui gui;
    private final MenuGui menugui;
    private final WatchListGUI watchListGUI;
    private final AddFaultGUI addFaultGUI;
    private final FDSHttpRequestHandler http;
    private final TaskController taskController;

    public SimulatorCenterController(Simulator application) throws Exception {
        this.application = application;
        http = new FDSHttpRequestHandler(SimulatorSetting.FDSAddress);

        gui = new Gui(this);
        gui.init();
        this.application.add(gui);

        menugui = new MenuGui(this);
        menugui.init();
        this.application.add(menugui);

        addFaultGUI = new AddFaultGUI(this);
        watchListGUI = new WatchListGUI();
        taskController = new TaskController(this, gui, menugui);
    }

    public void SimulationAllProcessTask() {
        taskController.startSimulationAllProcess();
        buttonStatusChange(true);
    }

    public void SimulationWithAirPumpTask() {
        taskController.startSimulationWithAirPump();
        buttonStatusChange(true);
    }

    public void SimulationWithHeatTask() {
        taskController.startSimulationWithHeat();
        buttonStatusChange(true);
    }

    public void CleanTask() {
        taskController.startClean();
        buttonStatusChange(true);
    }

    public void FillingProcess() {
        taskController.startFillLowerTankProcess();
        buttonStatusChange(true);
    }

    public void HeatingProcess() {
        taskController.startHeatProcess();
        buttonStatusChange(true);
    }

    public void PumpingProcess() {
        taskController.startFillUpperTankProcess();
        buttonStatusChange(true);
    }

    public void AirPumpingProcess() {
        taskController.startAirPumpingProcess();
        buttonStatusChange(true);
    }
    
    public void Stop() {
        taskController.stop();
        buttonStatusChange(false);
        DataBuffer.deactivedFunction.forEach(function_id -> {
            deactiveFunction(function_id);
        });
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("process_id", new Integer(0));
        try {
            http.postComponentsValue(sendData);
        } catch (Exception e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
        }
    }

    public void Reset() {
        taskController.reset();
        buttonStatusChange(false);
        watchListGUI.resetDefektComponent();
        DataBuffer.deactivedFunction.clear();
        DataBuffer.faultData = new JSONArray();
        DataBuffer.strategy = new JSONArray();
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("process_id", new Integer(0));
        try {
            http.postComponentsValue(sendData);
        } catch (Exception e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
        }
    }
    
    public void sendFault(String selectedseries, String faultType) {
        try {
            if (!selectedseries.isEmpty()) {
                for (int i = 0; i < DataBuffer.data.length(); i++) {
                    JSONObject component = DataBuffer.data.getJSONObject(i);
                    if (component.getString("series").equals(selectedseries)) {
                        JSONObject faultObj = new JSONObject();
                        int componentID = component.getInt("component_id");
                        faultObj.put("component_id", componentID);
                        faultObj.put("series", selectedseries);
                        faultObj.put("fault_type", faultType);
                        faultObj.put("fault_desc", "manual added fault");
                        watchListGUI.setDefektComponent(componentID, true);
                        JSONObject result = http.reportFault(faultObj);
                        JOptionPane.showMessageDialog(null,
                                "Reconfiguration Strategy: Deactive Mainfunction "
                                + result.getJSONObject("execute_command").getJSONArray("mainfunction_ids")
                                .toString()
                                + "\nClick [Set Strategy] Button to apply the reconfiguration strategy!",
                                "Response from FRS(Server)", JOptionPane.WARNING_MESSAGE);
                        DataBuffer.faultData.put(faultObj);
                        DataBuffer.strategy.put(result);
//                        menugui.setExecuteButtonEnable(true);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Response from FRS(Server): Reconfiguration Stratgy Failed!\nThis failure caused by the ATS model still not completely designed!",
                    "Response from FRS(Server)", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void executeStrategy() {
        for (int i = 0; i < DataBuffer.strategy.length(); i++) {
            for (int j = 0; j < DataBuffer.strategy.getJSONObject(i).getJSONObject("execute_command")
                    .getJSONArray("mainfunction_ids").length(); j++) {
                int function_id = DataBuffer.strategy.getJSONObject(i).getJSONObject("execute_command")
                        .getJSONArray("mainfunction_ids").getInt(j);
                DataBuffer.deactivedFunction.add(function_id);
                deactiveFunction(function_id);
            }
            updateMeta(DataBuffer.strategy.getJSONObject(i).getInt("component_id"),
                    DataBuffer.strategy.getJSONObject(i).getJSONObject("execute_command"));
        }

    }

    private void updateMeta(int component_id, JSONObject obj) {
        JSONObject updateMeta = new JSONObject();
        updateMeta.put("component", component_id);
        updateMeta.put("functions", obj.getJSONArray("function_ids"));
        updateMeta.put("subsystems", obj.getJSONArray("subsystem_ids"));
        updateMeta.put("subfunctions", obj.getJSONArray("subfunction_ids"));
        updateMeta.put("mainfunctions", obj.getJSONArray("mainfunction_ids"));
        try {
            JSONObject result = http.updateStatus(updateMeta);
            if (result.getString("result").equals("success")) {
                JOptionPane.showMessageDialog(null,
                        "Response from FRS(Server): Reconfiguration Stratgy has been already setted successfully!",
                        "Response from FRS(Server)", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
        }
    }

    private void deactiveFunction(int id) {
        switch (id) {
            case 1:
                menugui.setHeatingButtonEnable(false);
                break;
            case 2:
                menugui.setFillingButtonEnable(false);
                menugui.setAirPumpingButtonEnable(false);
                break;
            case 3:
                menugui.setPumpingButtonEnable(false);
                break;
            default:
                break;
        }
    }

    public MenuGui getMenuGUI() {
        return this.menugui;
    }

    public Gui getGui() {
        return this.gui;
    }

    public WatchListGUI getWatchListGUI() {
        return this.watchListGUI;
    }

    public AddFaultGUI getAddFaultGUI() {
        return this.addFaultGUI;
    }

    public void watchList() {
        if (watchListGUI.isVisible()) {
            watchListGUI.setVisible(false);
        } else {
            watchListGUI.setVisible(true);
        }
    }

    public void addFaultGUI() {
        if (addFaultGUI.isVisible()) {
            addFaultGUI.setVisible(false);
        } else {
            addFaultGUI.setVisible(true);
        }
    }

    public boolean checkConnection() throws Exception {
        return http.connectionStatus().getString("status").equals("running");
    }

    public void buttonStatusChange(boolean flag) {
        if (flag) {
            menugui.setAbfStatus("running");
        } else {
            menugui.setAbfStatus("stop");
        }
        menugui.setStopButtonEnable(flag);
        menugui.setSimulationAllTaskButtonEnable(!flag);
        menugui.setSimulationHeatTaskButtonEnable(!flag);
        menugui.setSimulationAirPumpTaskButtonEnable(!flag);
        menugui.setSimulationCleanTaskButtonEnable(!flag);
        menugui.setFillingButtonEnable(!flag);
        menugui.setHeatingButtonEnable(!flag);
        menugui.setPumpingButtonEnable(!flag);
        menugui.setAirPumpingButtonEnable(!flag);
    }
}
