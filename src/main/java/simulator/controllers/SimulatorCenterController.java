package simulator.controllers;

import simulator.Simulator;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.guis.AddFaultGUI;
import simulator.guis.Gui;
import simulator.guis.MenuGui;
import simulator.guis.WatchListGUI;
import simulator.utils.DataBuffer;
import simulator.utils.ErrorLogger;
import simulator.utils.FDSHttpRequestHandler;
import simulator.utils.SimulatorSetting;
import fds.FDMGUI;
import fds.controllers.FDMController;
import fds.model.DatabaseHandler;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import simulator.guis.TasksList;

public class SimulatorCenterController {

    private final FDMGUI FDMGui;
    private final Simulator application;
    private final Gui gui;
    private final MenuGui menugui;
    public final TasksList tasksList;
    private final WatchListGUI watchListGUI;
    private final AddFaultGUI addFaultGUI;
    private FDSHttpRequestHandler http;
    private final TaskController taskController;
    private final FDMController FDMController;
    private final DatabaseHandler databaseHandler;

    public SimulatorCenterController(Simulator application, FDMGUI FDMGui) throws Exception {
        this.databaseHandler = new DatabaseHandler();
        DataBuffer.SubSystems = databaseHandler.getSubsystems();
        DataBuffer.initData = databaseHandler.getComponents();
        DataBuffer.data = databaseHandler.getComponents();
        DataBuffer.task = databaseHandler.getTasks();
        this.application = application;
        this.FDMGui = FDMGui;

        http = new FDSHttpRequestHandler(SimulatorSetting.FDSAddress);

        gui = new Gui(this);
        gui.init();
        this.application.add(gui);

        tasksList = new TasksList(this);
        tasksList.init();
        this.application.add(tasksList);

        menugui = new MenuGui(this);
        menugui.init();
        this.application.add(menugui);

        addFaultGUI = new AddFaultGUI(this);
        watchListGUI = new WatchListGUI();
        taskController = new TaskController(this, gui, menugui);
        FDMController = new FDMController(this.FDMGui, this.http, this);
        FDMGui.setFDMController(FDMController);
    }

    public void AutoCycl30() {
        tasksList.addTasks(getTask(1));
        taskController.taskList.add(1);
    }

    public void HeatWater3L45() {
        tasksList.addTasks(getTask(2));
        taskController.taskList.add(2);
    }

    public void PourWater5L() {
        tasksList.addTasks(getTask(3));
        taskController.taskList.add(3);
    }

    public void Clean() {
        tasksList.addTasks(getTask(4));
        taskController.taskList.add(4);
    }

    public void FillingProcess() {
        taskController.startFillLowerTankProcess();
        buttonStatusChange(true);
    }

    public void FillingReplaceProcess() {
        taskController.startFillReplaceLowerTankProcess();
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
        sendData.put("task_id", new Integer(0));
        sendData.put("function_id", new Integer(0));
        try {
            http.postRuntimeData(sendData);
        } catch (Exception e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
        }
    }

    public void Reset() throws SQLException, NamingException {
        taskController.reset();
        buttonStatusChange(false);
        watchListGUI.resetDefektComponent();
        DataBuffer.deactivedFunction.clear();
        DataBuffer.faultData = new JSONArray();
        DataBuffer.strategy = new JSONArray();
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("task_id", new Integer(0));
        sendData.put("function_id", new Integer(0));
        databaseHandler.resetDatabase();
        try {
            http.postRuntimeData(sendData);
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
            updateMeta(DataBuffer.strategy.getJSONObject(i).getInt("component_id"), DataBuffer.strategy.getJSONObject(i).getJSONObject("execute_command"));
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
        System.out.println(id);
        switch (id) {
            case 1:
                menugui.setHeatingButtonEnable(false);
                menugui.setTask1ButtonEnable(false);
                menugui.setTask2ButtonEnable(false);
                break;
            case 2:
                menugui.setFillingButtonEnable(false);
                menugui.setAirPumpingButtonEnable(false);
                menugui.setTask1ButtonEnable(false);
                menugui.setTask3ButtonEnable(false);
                menugui.setTask4ButtonEnable(false);
                menugui.setTask2ButtonEnable(false);
                break;
            case 3:
                menugui.setPumpingButtonEnable(false);
                menugui.setTask1ButtonEnable(false);
                menugui.setTask3ButtonEnable(false);
                menugui.setTask4ButtonEnable(false);
                menugui.setTask2ButtonEnable(false);
                break;
            default:
                break;
        }
        tasksList.disableTask(2);
        tasksList.disableTask(2);
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

    public FDMController getFDMController() {
        return this.FDMController;
    }

    public void watchList() {
        if (watchListGUI.isVisible()) {
            watchListGUI.setVisible(false);
        } else {
            watchListGUI.setVisible(true);
        }
    }

    public void addFaultGUI() {
        addFaultGUI.setVisible(!addFaultGUI.isVisible());
    }

    public void openAddFaultGUIBySeries(String series) {
        addFaultGUI.setVisible(series);
    }

    public void checkConnection() {
        http = new FDSHttpRequestHandler(SimulatorSetting.FDSAddress);
        menugui.checkConnection(false);
        try {
            boolean state = http.connectionStatus().getString("status").equals("running");
            menugui.checkConnection(state);
            JOptionPane.showMessageDialog(null,
                    "Connected to DHFRS Server successfully",
                    "Connecting to DHFRS Server", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Connect to DHFRS Server failed",
                    "Connecting to DHFRS Server", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(SimulatorCenterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buttonStatusChange(boolean flag) {
        if (flag) {
            menugui.setAbfStatus("running");
        } else {
            menugui.setAbfStatus("stop");
        }
        menugui.setStopButtonEnable(flag);
        menugui.setStartButtonEnable(!flag);
        menugui.setTask1ButtonEnable(!flag);
        menugui.setTask2ButtonEnable(!flag);
        menugui.setTask3ButtonEnable(!flag);
        menugui.setTask4ButtonEnable(!flag);
        menugui.setFillingButtonEnable(!flag);
        menugui.setFillingReplaceButtonEnable(!flag);
        menugui.setHeatingButtonEnable(!flag);
        menugui.setPumpingButtonEnable(!flag);
        menugui.setAirPumpingButtonEnable(!flag);
    }

    public void StartTasks() {
        buttonStatusChange(true);
        taskController.startTasks();
    }

    public void clearTasks() {
        taskController.taskList.clear();
    }

    private JSONObject getTask(int index) {
        JSONObject taskObj = new JSONObject();
        for (int i = 0; i < DataBuffer.task.length(); i++) {
            if (DataBuffer.task.getJSONObject(i).getInt("task_id") == index) {
                taskObj = DataBuffer.task.getJSONObject(i);
                taskObj.put("task_status", "normal");
            }
        }
        return taskObj;
    }
}
