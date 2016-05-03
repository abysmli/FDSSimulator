package com.fds.simulator.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

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

public class AbfuellanlageLogic {

    private final static int delay = 30;
    private final static double flowrateValve = 0.05;
    private final static double sollWaterLevel = 6;
    private final static double initWaterLevel = 2;
    private final static double sollWaterTemp = 28;
    private final static double initWaterTemp = 25;
    private final static double SumWaterLevel = 12;
    private final static double heatPower = 0.5;

    private final Gui gui;
    private MenuGui menugui;
    private WatchListGUI watchListGUI = new WatchListGUI();
    private AddFaultGUI addFaultGUI = new AddFaultGUI(this);
    private final FDSHttpRequestHandler http = new FDSHttpRequestHandler(SimulatorSetting.FDSAddress);

    private double WaterTempIn102;
    private int FunctionNo;
    private final Timer runningTimer, stopTimer, fillingTimer, heatingTimer, pumpingTimer, airpumpingTimer;
    private long starttime;
    private boolean faultflag;
    private double oldTemperature, oldWaterLevel, oldWaterPressure, oldWaterFlow, oldAirPressure, oldAirFlow,
            changeRateTemperature, changeRateWaterLevel, changeRateWaterPressure, changeRateWaterFlow,
            changeRateAirPressure, changeRateAirFlow;

    public AbfuellanlageLogic(Gui gui, MenuGui menugui) throws Exception {
        this.WaterTempIn102 = initWaterTemp;
        this.gui = gui;
        this.gui.setMenuGUI(menugui);
        this.menugui = menugui;
        this.menugui.init(this, gui);
        gui.setWaterLevel(initWaterLevel / SumWaterLevel);
        gui.setTemperatureDisplay(initWaterTemp);
        ActionListener runningTimerListener = (ActionEvent e) -> {
            try {
                switch (FunctionNo) {
                    case 1:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 2) {
                                Stop();
                            }
                        });
                        FillLowerTankProcess();
                        AirPumpingProcess();
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            FunctionNo = 2;
                        }
                        break;
                    case 2:
                        CheckSollWaterLevelProcess();
                        FunctionNo = 3;
                        break;
                    case 3:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 1) {
                                Stop();
                            }
                        });
                        if (gui.getTemperature() > sollWaterTemp) {
                            FunctionNo = 4;
                        } else {
                            HeatProcess();
                        }
                        break;
                    case 4:
                        CheckTemperaturProcess();
                        FunctionNo = 5;
                        break;
                    case 5:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 3) {
                                Stop();
                            }
                        });
                        FillUpperTankProcess();
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            FunctionNo = 6;
                        }
                        break;
                    case 6:
                        CheckInitWaterLevelProcess();
                        FunctionNo = 1;
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        runningTimer = new Timer(delay, runningTimerListener);

        ActionListener stopTimerListener = (ActionEvent e) -> {
            gui.setTemperatureDisplay(gui.getTemperature() - 0.1 * delay / 1000);
            if (gui.getTemperature() < 0.0) {
                gui.setTemperatureDisplay(0.0);
            }
        };
        stopTimer = new Timer(delay, stopTimerListener);

        ActionListener fillingTimerListener = (ActionEvent e) -> {
            try {
                FillLowerTankProcess();
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
            if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                Stop();
            }
        };
        fillingTimer = new Timer(delay, fillingTimerListener);

        ActionListener heatingTimerListener = (ActionEvent e) -> {
            if (gui.getTemperature() > sollWaterTemp) {
                Stop();
            } else {
                try {
                    HeatProcess();
                } catch (Exception e1) {
                    ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
                }
            }
        };
        heatingTimer = new Timer(delay, heatingTimerListener);

        ActionListener pumpingTimerListener = (ActionEvent e) -> {
            try {
                FillUpperTankProcess();
                if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                    Stop();
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        pumpingTimer = new Timer(delay, pumpingTimerListener);

        ActionListener airpumpingTimerListener = (ActionEvent e) -> {
            AirPumpingProcess();
        };
        airpumpingTimer = new Timer(delay, airpumpingTimerListener);
    }

    public void setMenuGUI(MenuGui menugui) {
        this.menugui = menugui;
    }
    
    public MenuGui getMenuGUI() {
        return this.menugui;
    }

    public void Start() {
        stopTimer.stop();
        FunctionNo = 1;
        starttime = System.currentTimeMillis();
        runningTimer.start();
        menugui.setAbfStatus("running");
        menugui.setStopButtonEnable(true);
        menugui.setStartButtonEnable(false);
        menugui.setFillingButtonEnable(false);
        menugui.setHeatingButtonEnable(false);
        menugui.setPumpingButtonEnable(false);
        menugui.setAirPumpingButtonEnable(false);
    }

    public void Stop() {
        gui.setProcessLabelText("Stop");
        runningTimer.stop();
        fillingTimer.stop();
        heatingTimer.stop();
        pumpingTimer.stop();
        airpumpingTimer.stop();
        gui.setPumpState(false);
        gui.setValveState(false);
        gui.setAirState(false);
        gui.setHeaterState(false);
        gui.setBallState(false);
        gui.setAirFlowRate(0.0);
        gui.setAirPressureRate(0.0);
        gui.setFlowRate(0.0);
        gui.setPressureRate(0.0);
        stopTimer.start();
        menugui.setAbfStatus("stop");
        menugui.setStopButtonEnable(false);
        menugui.setStartButtonEnable(true);
        menugui.setFillingButtonEnable(true);
        menugui.setHeatingButtonEnable(true);
        menugui.setPumpingButtonEnable(true);
        menugui.setAirPumpingButtonEnable(true);
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

    public void Repair() {
        gui.setProcessLabelText("Stop");
        runningTimer.stop();
        fillingTimer.stop();
        heatingTimer.stop();
        pumpingTimer.stop();
        airpumpingTimer.stop();
        faultflag = false;
        gui.setPumpState(false);
        gui.setValveState(false);
        gui.setAirState(false);
        gui.setHeaterState(false);
        gui.setAirFlowRate(0.0);
        gui.setAirPressureRate(0.0);
        gui.setFlowRate(0.0);
        gui.setPressureRate(0.0);
        gui.setBallState(false);
        gui.setWaterLevel(initWaterLevel / SumWaterLevel);
        gui.setTemperatureDisplay(initWaterTemp);
        if (stopTimer.isRunning()) {
            stopTimer.stop();
        }
        menugui.setAbfStatus("stop");
        menugui.setStopButtonEnable(false);
        menugui.setStartButtonEnable(true);
        menugui.setFillingButtonEnable(true);
        menugui.setHeatingButtonEnable(true);
        menugui.setPumpingButtonEnable(true);
        menugui.setAirPumpingButtonEnable(true);
        menugui.setExecuteButtonEnable(false);
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

    public void FillingFunction() {
        fillingTimer.start();
        menugui.setAbfStatus("running");
        menugui.setStopButtonEnable(true);
        menugui.setStartButtonEnable(false);
        menugui.setFillingButtonEnable(false);
        menugui.setHeatingButtonEnable(false);
        menugui.setPumpingButtonEnable(false);
        menugui.setAirPumpingButtonEnable(false);
    }

    public void HeatingFunction() {
        heatingTimer.start();
        menugui.setAbfStatus("running");
        menugui.setStopButtonEnable(true);
        menugui.setStartButtonEnable(false);
        menugui.setFillingButtonEnable(false);
        menugui.setHeatingButtonEnable(false);
        menugui.setPumpingButtonEnable(false);
        menugui.setAirPumpingButtonEnable(false);
    }

    public void PumpingFunction() {
        pumpingTimer.start();
        menugui.setAbfStatus("running");
        menugui.setStopButtonEnable(true);
        menugui.setStartButtonEnable(false);
        menugui.setFillingButtonEnable(false);
        menugui.setHeatingButtonEnable(false);
        menugui.setPumpingButtonEnable(false);
        menugui.setAirPumpingButtonEnable(false);
    }

    public void AirPumpingFunction() {
        airpumpingTimer.start();
        menugui.setAbfStatus("running");
        menugui.setStopButtonEnable(true);
        menugui.setStartButtonEnable(false);
        menugui.setFillingButtonEnable(false);
        menugui.setHeatingButtonEnable(false);
        menugui.setPumpingButtonEnable(false);
        menugui.setAirPumpingButtonEnable(false);
    }

    public void FillLowerTankProcess() throws Exception {
        // fill lower tank
        gui.setProcessLabelText("Function: Filling");
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setManualValveState(true);
        gui.setBallState(true);
        if (faultflag == false) {
            gui.setValveState(true);
        }
        if ((gui.getWaterLevel() < sollWaterLevel / SumWaterLevel) && (gui.getValveState() == true)
                && (gui.getManualValveState() == true)) {
            gui.setWaterLevel(gui.getWaterLevel() + (flowrateValve / 1000 * delay));
            gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
            gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
            gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        }
        if (((System.currentTimeMillis()) - starttime) > 500) {
            calcChangeRate(System.currentTimeMillis() - starttime);
            DataBuffer.data.getJSONObject(0).put("value", "on");
            DataBuffer.data.getJSONObject(1).put("value", "off");
            DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(3).put("value", "OK");
            DataBuffer.data.getJSONObject(4).put("value", "on");
            DataBuffer.data.getJSONObject(5).put("value", "off");
            DataBuffer.data.getJSONObject(6).put("value", "on");
            DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
            DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
            DataBuffer.data.getJSONObject(8).put("value", "off");
            DataBuffer.data.getJSONObject(9).put("value", "off");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "off");
            DataBuffer.data.getJSONObject(12).put("value", "off");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", "off");
            DataBuffer.data.getJSONObject(16).put("value", "off");
            DataBuffer.data.getJSONObject(17).put("value", "on");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "on");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(22).put("value", "on");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            JSONObject sendData = new JSONObject();
            sendData.put("components", DataBuffer.data);
            sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
            sendData.put("process_id", new Integer(1));
            starttime = System.currentTimeMillis();
            watchListGUI.refresh();
            http.postComponentsValue(sendData);
        }
    }

    public void AirPumpingProcess() {
        gui.setProcessLabelText("Function: Air Pumping");
        gui.setAirState(true);
        gui.setAirFlowRate(45.0);
        gui.setAirPressureRate(6.0);
    }

    // check Water Level for sollWaterLevel
    public void CheckSollWaterLevelProcess() throws Exception {
        gui.setProcessLabelText("Function: Checking Filling");
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);
        gui.setAirPressureRate(0.0);
        gui.setBallState(false);
        gui.setValveState(false);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        calcChangeRate(System.currentTimeMillis() - starttime);
        DataBuffer.data.getJSONObject(0).put("value", "on");
        DataBuffer.data.getJSONObject(1).put("value", "off");
        DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
        DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
        DataBuffer.data.getJSONObject(3).put("value", "OK");
        DataBuffer.data.getJSONObject(4).put("value", "off");
        DataBuffer.data.getJSONObject(5).put("value", "on");
        DataBuffer.data.getJSONObject(6).put("value", "off");
        DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
        DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
        DataBuffer.data.getJSONObject(8).put("value", "on");
        DataBuffer.data.getJSONObject(9).put("value", "off");
        DataBuffer.data.getJSONObject(10).put("value", "on");
        DataBuffer.data.getJSONObject(11).put("value", "off");
        DataBuffer.data.getJSONObject(12).put("value", "off");
        DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
        DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
        DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
        DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
        DataBuffer.data.getJSONObject(15).put("value", "on");
        DataBuffer.data.getJSONObject(16).put("value", "off");
        DataBuffer.data.getJSONObject(17).put("value", "on");
        DataBuffer.data.getJSONObject(18).put("value", "on");
        DataBuffer.data.getJSONObject(19).put("value", "off");
        DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirPressure()));
        DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirPressure));
        DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirFlow()));
        DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirFlow));
        DataBuffer.data.getJSONObject(22).put("value", "off");
        DataBuffer.data.getJSONObject(23).put("value", "on");
        DataBuffer.data.getJSONObject(24).put("value", "on");
        DataBuffer.data.getJSONObject(25).put("value", "OK");
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("process_id", new Integer(1));
        starttime = System.currentTimeMillis();
        watchListGUI.refresh();
        http.postComponentsValue(sendData);
    }

    public void HeatProcess() throws Exception {
        gui.setProcessLabelText("Function: Heating");
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setHeaterState(true);
        gui.setTemperatureDisplay(gui.getTemperature() + heatPower * delay / 1000);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        if (((System.currentTimeMillis()) - starttime) > 500) {
            calcChangeRate(System.currentTimeMillis() - starttime);
            DataBuffer.data.getJSONObject(0).put("value", "on");
            DataBuffer.data.getJSONObject(1).put("value", "on");
            DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(3).put("value", "OK");
            DataBuffer.data.getJSONObject(4).put("value", "off");
            DataBuffer.data.getJSONObject(5).put("value", "off");
            DataBuffer.data.getJSONObject(6).put("value", "on");
            DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
            DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
            DataBuffer.data.getJSONObject(8).put("value", "off");
            DataBuffer.data.getJSONObject(9).put("value", "off");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "off");
            DataBuffer.data.getJSONObject(12).put("value", "off");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", "off");
            DataBuffer.data.getJSONObject(16).put("value", "off");
            DataBuffer.data.getJSONObject(17).put("value", "on");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "off");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(22).put("value", "off");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            JSONObject sendData = new JSONObject();
            sendData.put("components", DataBuffer.data);
            sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
            sendData.put("process_id", new Integer(2));
            starttime = System.currentTimeMillis();
            watchListGUI.refresh();
            http.postComponentsValue(sendData);
        }

    }

    public void CheckTemperaturProcess() throws Exception {
        gui.setProcessLabelText("Function: Checking Temperature");
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setHeaterState(false);
        gui.setPumpState(true);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        calcChangeRate(System.currentTimeMillis() - starttime);
        DataBuffer.data.getJSONObject(0).put("value", "on");
        DataBuffer.data.getJSONObject(1).put("value", "off");
        DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
        DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
        DataBuffer.data.getJSONObject(3).put("value", "OK");
        DataBuffer.data.getJSONObject(4).put("value", "off");
        DataBuffer.data.getJSONObject(5).put("value", "on");
        DataBuffer.data.getJSONObject(6).put("value", "off");
        DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
        DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
        DataBuffer.data.getJSONObject(8).put("value", "on");
        DataBuffer.data.getJSONObject(9).put("value", "off");
        DataBuffer.data.getJSONObject(10).put("value", "on");
        DataBuffer.data.getJSONObject(11).put("value", "off");
        DataBuffer.data.getJSONObject(12).put("value", "off");
        DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
        DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
        DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
        DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
        DataBuffer.data.getJSONObject(15).put("value", "on");
        DataBuffer.data.getJSONObject(16).put("value", "off");
        DataBuffer.data.getJSONObject(17).put("value", "on");
        DataBuffer.data.getJSONObject(18).put("value", "on");
        DataBuffer.data.getJSONObject(19).put("value", "off");
        DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirPressure()));
        DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirPressure));
        DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirFlow()));
        DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirFlow));
        DataBuffer.data.getJSONObject(22).put("value", "off");
        DataBuffer.data.getJSONObject(23).put("value", "on");
        DataBuffer.data.getJSONObject(24).put("value", "on");
        DataBuffer.data.getJSONObject(25).put("value", "OK");
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("process_id", new Integer(2));
        starttime = System.currentTimeMillis();
        watchListGUI.refresh();
        http.postComponentsValue(sendData);
    }

    public void FillUpperTankProcess() throws Exception {
        gui.setProcessLabelText("Function: Pumping");
        double flowrate = gui.getSliderValue();
        if (flowrate == 0.0) {
            gui.setPumpMotorState(false);
            gui.setFlowRate(0.0);
            gui.setPressureRate(0.0);
            flowrate = gui.getSliderValue();
            gui.setAperturePercentage(gui.getSliderValue());
        } else {
            gui.setPumpMotorState(true);
            flowrate = gui.getSliderValue();
            gui.setAperturePercentage(gui.getSliderValue());
        }

        gui.setFlowRate(Math.round((flowrate * 100.0) / 10.0));
        gui.setPressureRate(Math.round((flowrate * 600.0) / 10.0));
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setWaterLevel(gui.getWaterLevel() - ((flowrate / 10) / 1000 * delay));
        WaterTempIn102 = (WaterTempIn102 * (1 - gui.getTankLevel())
                + gui.getTemperature() * (flowrateValve / 1000) * delay)
                / (((flowrateValve / 1000) * delay) + 1 - gui.getTankLevel());
        gui.setTemperatureDisplay(gui.getTemperature() - 0.1 * delay / 1000);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        if (gui.getTemperature() < 0.0) {
            gui.setTemperatureDisplay(0.0);
            gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
            gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
            gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        }
        if (((System.currentTimeMillis()) - starttime) > 2000) {
            calcChangeRate(System.currentTimeMillis() - starttime);
            DataBuffer.data.getJSONObject(0).put("value", "on");
            DataBuffer.data.getJSONObject(1).put("value", "off");
            DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(3).put("value", "OK");
            DataBuffer.data.getJSONObject(4).put("value", "off");
            DataBuffer.data.getJSONObject(5).put("value", "off");
            DataBuffer.data.getJSONObject(6).put("value", "on");
            DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
            DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
            DataBuffer.data.getJSONObject(8).put("value", "off");
            DataBuffer.data.getJSONObject(9).put("value", "off");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "on");
            DataBuffer.data.getJSONObject(12).put("value", "on");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", "off");
            DataBuffer.data.getJSONObject(16).put("value", "off");
            DataBuffer.data.getJSONObject(17).put("value", "on");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "off");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(22).put("value", "off");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            JSONObject sendData = new JSONObject();
            sendData.put("components", DataBuffer.data);
            sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
            sendData.put("process_id", new Integer(3));
            starttime = System.currentTimeMillis();
            watchListGUI.refresh();
            http.postComponentsValue(sendData);
        }
    }

    // check water level for initWaterLevel
    public void CheckInitWaterLevelProcess() throws Exception {
        gui.setProcessLabelText("Function: Checking WaterLevel");
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setPumpState(false);
        gui.setFlowRate(0.0);
        gui.setPressureRate(0.0);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        calcChangeRate(System.currentTimeMillis() - starttime);
        DataBuffer.data.getJSONObject(0).put("value", "on");
        DataBuffer.data.getJSONObject(1).put("value", "off");
        DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
        DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
        DataBuffer.data.getJSONObject(3).put("value", "OK");
        DataBuffer.data.getJSONObject(4).put("value", "off");
        DataBuffer.data.getJSONObject(5).put("value", "on");
        DataBuffer.data.getJSONObject(6).put("value", "off");
        DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
        DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
        DataBuffer.data.getJSONObject(8).put("value", "on");
        DataBuffer.data.getJSONObject(9).put("value", "off");
        DataBuffer.data.getJSONObject(10).put("value", "off");
        DataBuffer.data.getJSONObject(11).put("value", "off");
        DataBuffer.data.getJSONObject(12).put("value", "off");
        DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
        DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
        DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
        DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
        DataBuffer.data.getJSONObject(15).put("value", "off");
        DataBuffer.data.getJSONObject(16).put("value", "on");
        DataBuffer.data.getJSONObject(17).put("value", "on");
        DataBuffer.data.getJSONObject(18).put("value", "on");
        DataBuffer.data.getJSONObject(19).put("value", "off");
        DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirPressure()));
        DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirPressure));
        DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirFlow()));
        DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirFlow));
        DataBuffer.data.getJSONObject(22).put("value", "off");
        DataBuffer.data.getJSONObject(23).put("value", "on");
        DataBuffer.data.getJSONObject(24).put("value", "on");
        DataBuffer.data.getJSONObject(25).put("value", "OK");
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("process_id", new Integer(3));
        starttime = System.currentTimeMillis();
        watchListGUI.refresh();
        http.postComponentsValue(sendData);
    }

    public WatchListGUI getWatchListGUI() {
        return watchListGUI;
    }

    public void setWatchListGUI(WatchListGUI watchListGUI) {
        this.watchListGUI = watchListGUI;
    }

    public AddFaultGUI getAddFaultGUI() {
        return addFaultGUI;
    }

    public void setAddFaultGUI(AddFaultGUI addFaultGUI) {
        this.addFaultGUI = addFaultGUI;
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
                        menugui.setExecuteButtonEnable(true);
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

    private void calcChangeRate(long timeDiff) {
        changeRateTemperature = (gui.getTemperature() - oldTemperature) * 1000 / timeDiff;
        changeRateWaterLevel = (gui.getWaterLevel() - oldWaterLevel) * 1000 / timeDiff;
        changeRateWaterPressure = (gui.getWaterPressure() - oldWaterPressure) * 1000 / timeDiff;
        changeRateWaterFlow = (gui.getWaterFlow() - oldWaterFlow) * 1000 / timeDiff;
        changeRateAirPressure = (gui.getAirPressure() - oldAirPressure) * 1000 / timeDiff;
        changeRateAirFlow = (gui.getAirFlow() - oldAirFlow) * 1000 / timeDiff;
        oldTemperature = gui.getTemperature();
        oldWaterLevel = gui.getWaterLevel();
        oldWaterPressure = gui.getWaterPressure();
        oldWaterFlow = gui.getWaterFlow();
        oldAirPressure = gui.getAirPressure();
        oldAirFlow = gui.getAirFlow();
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
}
