/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.controllers;

import simulator.guis.Gui;
import simulator.guis.MenuGui;
import simulator.utils.DataBuffer;
import simulator.utils.ErrorLogger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class FunctionController {

    protected int delay = 50;
    protected double flowrateValve = 0.5;
    protected double sollWaterLevel = 8;
    protected double initWaterLevel = 1;
    protected double SumWaterLevel = 10;
    protected double sollWaterTemp = 20;
    protected double initWaterTemp = 15;
    protected double heatPower = 3;
    protected double AirFlow = 7.0;
    protected double initAirPressure = 0.0;
    protected double sollAirPressure = 6.0;

    protected int task_id = 0;

    private double oldTemperature, oldWaterLevel, oldWaterPressure, oldWaterFlow, oldAirPressure, oldAirFlow,
            changeRateTemperature, changeRateWaterLevel, changeRateWaterPressure, changeRateWaterFlow,
            changeRateAirPressure, changeRateAirFlow;

    private final Timer stopTimer, fillingTimer, fillingReplaceTimer, heatingTimer, pumpingTimer, airpumpingTimer;
    protected long starttime;

    protected final Gui gui;

    protected final MenuGui menuGui;

    protected final SimulatorCenterController simulatorCenterController;

    public FunctionController(SimulatorCenterController simulatorCenterController, Gui gui, MenuGui menuGui) {
        this.simulatorCenterController = simulatorCenterController;
        this.gui = gui;
        this.menuGui = menuGui;

        gui.setWaterLevel(initWaterLevel / SumWaterLevel);
        gui.setHeaterState(false);
        gui.setTemperatureDisplay(initWaterTemp);
        gui.setTemperatureDisplay2(initWaterTemp);

        ActionListener stopTimerListener = (ActionEvent e) -> {
            if (gui.getTemperature() > 15.0) {
                gui.setTemperatureDisplay(gui.getTemperature() - 0.1 * delay / 1000);
                gui.setTemperatureDisplay2(gui.getTemperature() - 0.1 * delay / 1000);
            }
            if (gui.getAirPressure() > 0) {
                gui.setAirPressure(gui.getAirPressure() - 0.1 * delay / 3000);
            }
        };
        stopTimer = new Timer(delay, stopTimerListener);

        ActionListener fillingTimerListener = (ActionEvent e) -> {
            if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                simulatorCenterController.Stop();
            } else {
                try {
                    FillLowerTankProcess();
                } catch (Exception e1) {
                    ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
                }
            }
        };
        fillingTimer = new Timer(delay, fillingTimerListener);

        ActionListener fillingReplaceTimerListener = (ActionEvent e) -> {
            if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                simulatorCenterController.Stop();
            } else {
                try {
                    FillReplaceLowerTankProcess();
                } catch (Exception e1) {
                    ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
                }
            }
        };
        fillingReplaceTimer = new Timer(delay, fillingReplaceTimerListener);

        ActionListener heatingTimerListener = (ActionEvent e) -> {
            if (gui.getTemperature() > sollWaterTemp) {
                simulatorCenterController.Stop();
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
            if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                simulatorCenterController.Stop();
            } else {
                try {
                    FillUpperTankProcess();

                } catch (Exception e1) {
                    ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
                }
            }

        };
        pumpingTimer = new Timer(delay, pumpingTimerListener);

        ActionListener airpumpingTimerListener = (ActionEvent e) -> {
            if (gui.getAirPressure() > sollAirPressure) {
                simulatorCenterController.Stop();
            } else {
                try {
                    AirPumpingProcess();
                } catch (Exception ex) {
                    Logger.getLogger(FunctionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        airpumpingTimer = new Timer(delay, airpumpingTimerListener);
    }

    public void startAirPumpingProcess() {
        airpumpingTimer.start();
    }

    public void startCheckInitWaterLevelProcess() throws Exception {
        CheckInitWaterLevelProcess();
    }

    public void startCheckSollWaterLevelProcess() throws Exception {
        CheckSollWaterLevelProcess();
    }

    public void startCheckTemperaturProcess() throws Exception {
        CheckTemperaturProcess();
    }

    public void startFillLowerTankProcess() {
        fillingTimer.start();
    }

    public void startFillReplaceLowerTankProcess() {
        fillingReplaceTimer.start();
    }

    public void startFillUpperTankProcess() {
        pumpingTimer.start();
    }

    public void startHeatProcess() {
        heatingTimer.start();
    }

    protected void AirPumpingProcess() throws Exception {
        gui.setAirState(true);
        gui.setAirFlowRate(AirFlow);
        gui.setAirPressure(gui.getAirPressure() + gui.getAirFlow() * delay / 3000);
        gui.setMCState(true);
        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
        if (((System.currentTimeMillis()) - starttime) > 500) {
            calcChangeRate(System.currentTimeMillis() - starttime);
            DataBuffer.data.getJSONObject(0).put("value", "on"); // Kontroller
            DataBuffer.data.getJSONObject(1).put("value", "off"); // Heizung
            DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature())); // Temperatur Sensor
            DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature)); // Temperatur Sensor
            DataBuffer.data.getJSONObject(3).put("value", "OK");
            DataBuffer.data.getJSONObject(4).put("value", "off"); // V102
            DataBuffer.data.getJSONObject(5).put("value", "off"); // S115
            DataBuffer.data.getJSONObject(6).put("value", "on"); // S116
            DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
            DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
            DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
            DataBuffer.data.getJSONObject(8).put("change_rate", "0");
            DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
            DataBuffer.data.getJSONObject(9).put("change_rate", "0");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "off");
            DataBuffer.data.getJSONObject(12).put("value", "off");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
            DataBuffer.data.getJSONObject(15).put("change_rate", "0");
            DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
            DataBuffer.data.getJSONObject(16).put("change_rate", "0");
            DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
            DataBuffer.data.getJSONObject(17).put("change_rate", "0");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "on");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(22).put("value", "on");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on"); // Ventil 112
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(27).put("value", "off"); // Ventil 113
            DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
            DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
            DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
            DataBuffer.data.getJSONObject(31).put("value", "on"); // MC 02
            setFaultValue();
            starttime = System.currentTimeMillis();
            simulatorCenterController.getWatchListGUI().refresh();
            simulatorCenterController.getFDMController().checkData(generateSendData(1));
        }
        setFaultState();
    }

    protected void FillLowerTankProcess() throws Exception {
        // fill lower tank
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setManualValveState(true);
        gui.setValveState(true);
        gui.setBallState(true);
        gui.setMCState(true);
        if ((gui.getWaterLevel() <= sollWaterLevel / SumWaterLevel) && (gui.getValveState() == true)
                && (gui.getManualValveState() == true)) {
            gui.setWaterLevel((double) Math.round((gui.getWaterLevel() + (flowrateValve / 1000 * delay)) * 100) / 100);
            gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 10);
            gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 10);
            gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        }
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);
        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));

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
            DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
            DataBuffer.data.getJSONObject(8).put("change_rate", "0");
            DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
            DataBuffer.data.getJSONObject(9).put("change_rate", "0");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "off");
            DataBuffer.data.getJSONObject(12).put("value", "off");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
            DataBuffer.data.getJSONObject(15).put("change_rate", "0");
            DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
            DataBuffer.data.getJSONObject(16).put("change_rate", "0");
            DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
            DataBuffer.data.getJSONObject(17).put("change_rate", "0");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "on");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(22).put("value", "on");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(27).put("value", "off"); // Ventil 113
            DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
            DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
            DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
            DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
            setFaultValue();
            starttime = System.currentTimeMillis();
            simulatorCenterController.getWatchListGUI().refresh();
            simulatorCenterController.getFDMController().checkData(generateSendData(2));
        }
        setFaultState();
    }

    private void FillReplaceLowerTankProcess() throws Exception {
        // fill lower tank
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setValveStateV113(true);
        gui.setMCState(true);
        if ((gui.getWaterLevel() <= sollWaterLevel / SumWaterLevel) && (gui.getValveStateV113() == true)) {
            gui.setWaterLevel((double) Math.round((gui.getWaterLevel() + (flowrateValve / 1000 * delay)) * 100) / 100);
            gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 10);
            gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 10);
            gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        }
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);

        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
        if (((System.currentTimeMillis()) - starttime) > 500) {
            calcChangeRate(System.currentTimeMillis() - starttime);
            DataBuffer.data.getJSONObject(0).put("value", "on");
            DataBuffer.data.getJSONObject(1).put("value", "off");
            DataBuffer.data.getJSONObject(2).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(2).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(3).put("value", "OK");
            DataBuffer.data.getJSONObject(4).put("value", "off"); // V102
            DataBuffer.data.getJSONObject(5).put("value", "off"); // S115
            DataBuffer.data.getJSONObject(6).put("value", "on"); // S116
            DataBuffer.data.getJSONObject(7).put("value", String.valueOf(gui.getWaterLevel()));
            DataBuffer.data.getJSONObject(7).put("change_rate", String.valueOf(changeRateWaterLevel));
            DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
            DataBuffer.data.getJSONObject(8).put("change_rate", "0");
            DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
            DataBuffer.data.getJSONObject(9).put("change_rate", "0");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "off");
            DataBuffer.data.getJSONObject(12).put("value", "off");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
            DataBuffer.data.getJSONObject(15).put("change_rate", "0");
            DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
            DataBuffer.data.getJSONObject(16).put("change_rate", "0");
            DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
            DataBuffer.data.getJSONObject(17).put("change_rate", "0");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "on");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(22).put("value", "on");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "off");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(27).put("value", "on"); // Ventil 113
            DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
            DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
            DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
            DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
            setFaultValue();
            starttime = System.currentTimeMillis();
            simulatorCenterController.getWatchListGUI().refresh();
            simulatorCenterController.getFDMController().checkData(generateSendData(2));
        }
        setFaultState();
    }

    // check Water Level for sollWaterLevel
    protected void CheckSollWaterLevelProcess() throws Exception {
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);
        gui.setBallState(false);
        gui.setValveState(false);
        gui.setMCState(true);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 10);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 10);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);

        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
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
        DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
        DataBuffer.data.getJSONObject(8).put("change_rate", "0");
        DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
        DataBuffer.data.getJSONObject(9).put("change_rate", "0");
        DataBuffer.data.getJSONObject(10).put("value", "on");
        DataBuffer.data.getJSONObject(11).put("value", "off");
        DataBuffer.data.getJSONObject(12).put("value", "off");
        DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
        DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
        DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
        DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
        DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
        DataBuffer.data.getJSONObject(15).put("change_rate", "0");
        DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
        DataBuffer.data.getJSONObject(16).put("change_rate", "0");
        DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
        DataBuffer.data.getJSONObject(17).put("change_rate", "0");
        DataBuffer.data.getJSONObject(18).put("value", "on");
        DataBuffer.data.getJSONObject(19).put("value", "off");
        DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
        DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
        DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
        DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
        DataBuffer.data.getJSONObject(22).put("value", "off");
        DataBuffer.data.getJSONObject(23).put("value", "on");
        DataBuffer.data.getJSONObject(24).put("value", "on");
        DataBuffer.data.getJSONObject(25).put("value", "OK");
        DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
        DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
        DataBuffer.data.getJSONObject(27).put("value", "off");
        DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
        DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
        DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
        DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
        setFaultValue();
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("process_id", new Integer(2));
        starttime = System.currentTimeMillis();
        setFaultState();
    }

    protected void HeatProcess() throws Exception {
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setHeaterState(true);
        gui.setTemperatureDisplay(gui.getTemperature() + heatPower * delay / 1000);
        gui.setTemperatureDisplay2(gui.getTemperature() + heatPower * delay / 1000);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 10);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 10);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);
        gui.setMCState(true);
        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
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
            DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
            DataBuffer.data.getJSONObject(8).put("change_rate", "0");
            DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
            DataBuffer.data.getJSONObject(9).put("change_rate", "0");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "off");
            DataBuffer.data.getJSONObject(12).put("value", "off");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
            DataBuffer.data.getJSONObject(15).put("change_rate", "0");
            DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
            DataBuffer.data.getJSONObject(16).put("change_rate", "0");
            DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
            DataBuffer.data.getJSONObject(17).put("change_rate", "0");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "off");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(22).put("value", "off");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(27).put("value", "off");
            DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
            DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
            DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
            DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
            setFaultValue();
            starttime = System.currentTimeMillis();
            simulatorCenterController.getWatchListGUI().refresh();
            simulatorCenterController.getFDMController().checkData(generateSendData(3));
        }
        setFaultState();
    }

    protected void CheckTemperaturProcess() throws Exception {
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setHeaterState(false);
        gui.setPumpState(true);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 10);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 10);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);
        gui.setMCState(true);
        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
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
        DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
        DataBuffer.data.getJSONObject(8).put("change_rate", "0");
        DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
        DataBuffer.data.getJSONObject(9).put("change_rate", "0");
        DataBuffer.data.getJSONObject(10).put("value", "on");
        DataBuffer.data.getJSONObject(11).put("value", "off");
        DataBuffer.data.getJSONObject(12).put("value", "off");
        DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
        DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
        DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
        DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
        DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
        DataBuffer.data.getJSONObject(15).put("change_rate", "0");
        DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
        DataBuffer.data.getJSONObject(16).put("change_rate", "0");
        DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
        DataBuffer.data.getJSONObject(17).put("change_rate", "0");
        DataBuffer.data.getJSONObject(18).put("value", "on");
        DataBuffer.data.getJSONObject(19).put("value", "off");
        DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
        DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
        DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
        DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
        DataBuffer.data.getJSONObject(22).put("value", "off");
        DataBuffer.data.getJSONObject(23).put("value", "on");
        DataBuffer.data.getJSONObject(24).put("value", "on");
        DataBuffer.data.getJSONObject(25).put("value", "OK");
        DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
        DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
        DataBuffer.data.getJSONObject(27).put("value", "off");
        DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
        DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
        DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
        DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
        setFaultValue();
        starttime = System.currentTimeMillis();
        simulatorCenterController.getWatchListGUI().refresh();
        simulatorCenterController.getFDMController().checkData(generateSendData(3));
        setFaultState();
    }

    protected void FillUpperTankProcess() throws Exception {
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
        gui.setMCState(true);
        gui.setFlowRate(Math.round((flowrate * 100.0) / 10.0));
        gui.setPressureRate(Math.round((flowrate * 600.0) / 10.0));
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setWaterLevel((double) Math.round((gui.getWaterLevel() - (flowrate * flowrateValve / 1000 * delay)) * 1000) / 1000);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 10);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 10);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);

        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
        if (((System.currentTimeMillis()) - starttime) > 500) {
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
            DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
            DataBuffer.data.getJSONObject(8).put("change_rate", "0");
            DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
            DataBuffer.data.getJSONObject(9).put("change_rate", "0");
            DataBuffer.data.getJSONObject(10).put("value", "on");
            DataBuffer.data.getJSONObject(11).put("value", "on");
            DataBuffer.data.getJSONObject(12).put("value", "on");
            DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
            DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
            DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
            DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
            DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
            DataBuffer.data.getJSONObject(15).put("change_rate", "0");
            DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
            DataBuffer.data.getJSONObject(16).put("change_rate", "0");
            DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
            DataBuffer.data.getJSONObject(17).put("change_rate", "0");
            DataBuffer.data.getJSONObject(18).put("value", "on");
            DataBuffer.data.getJSONObject(19).put("value", "off");
            DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
            DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
            DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
            DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
            DataBuffer.data.getJSONObject(22).put("value", "off");
            DataBuffer.data.getJSONObject(23).put("value", "on");
            DataBuffer.data.getJSONObject(24).put("value", "on");
            DataBuffer.data.getJSONObject(25).put("value", "OK");
            DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
            DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
            DataBuffer.data.getJSONObject(27).put("value", "off");
            DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
            DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
            DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
            DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
            setFaultValue();
            starttime = System.currentTimeMillis();
            simulatorCenterController.getWatchListGUI().refresh();
            simulatorCenterController.getFDMController().checkData(generateSendData(4));
        }
        setFaultState();
    }

    // check water level for initWaterLevel
    protected void CheckInitWaterLevelProcess() throws Exception {
        gui.setAperturePercentage(gui.getSliderValue());
        gui.setPumpState(false);
        gui.setFlowRate(0.0);
        gui.setPressureRate(0.0);
        gui.setAirState(false);
        gui.setAirFlowRate(0.0);
        gui.setShowWaterLevel1((double) Math.round(gui.getTankLevel() * 100) / 100);
        gui.setShowWaterLevel2((double) Math.round(gui.getWaterLevel() * 100) / 100);
        gui.setShowWaterTemp((double) Math.round((gui.getTemperature() * 10)) / 10);
        gui.setMCState(true);
        gui.setUltraSensor117(checkUltrasonicSensorState(9));
        gui.setUltraSensor114(checkUltrasonicSensorState(10));
        gui.setUltraSensor111(checkUltrasonicSensorState(16));
        gui.setUltraSensor112(checkUltrasonicSensorState(17));
        gui.setUltraSensor113(checkUltrasonicSensorState(18));
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
        DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
        DataBuffer.data.getJSONObject(8).put("change_rate", "0");
        DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
        DataBuffer.data.getJSONObject(9).put("change_rate", "0");
        DataBuffer.data.getJSONObject(10).put("value", "off");
        DataBuffer.data.getJSONObject(11).put("value", "off");
        DataBuffer.data.getJSONObject(12).put("value", "off");
        DataBuffer.data.getJSONObject(13).put("value", String.valueOf(gui.getWaterPressure()));
        DataBuffer.data.getJSONObject(13).put("change_rate", String.valueOf(changeRateWaterPressure));
        DataBuffer.data.getJSONObject(14).put("value", String.valueOf(gui.getWaterFlow()));
        DataBuffer.data.getJSONObject(14).put("change_rate", String.valueOf(changeRateWaterFlow));
        DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
        DataBuffer.data.getJSONObject(15).put("change_rate", "0");
        DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
        DataBuffer.data.getJSONObject(16).put("change_rate", "0");
        DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
        DataBuffer.data.getJSONObject(17).put("change_rate", "0");
        DataBuffer.data.getJSONObject(18).put("value", "on");
        DataBuffer.data.getJSONObject(19).put("value", "off");
        DataBuffer.data.getJSONObject(20).put("value", String.valueOf(gui.getAirFlow()));
        DataBuffer.data.getJSONObject(20).put("change_rate", String.valueOf(changeRateAirFlow));
        DataBuffer.data.getJSONObject(21).put("value", String.valueOf(gui.getAirPressure()));
        DataBuffer.data.getJSONObject(21).put("change_rate", String.valueOf(changeRateAirPressure));
        DataBuffer.data.getJSONObject(22).put("value", "off");
        DataBuffer.data.getJSONObject(23).put("value", "on");
        DataBuffer.data.getJSONObject(24).put("value", "on");
        DataBuffer.data.getJSONObject(25).put("value", "OK");
        DataBuffer.data.getJSONObject(26).put("value", String.valueOf(gui.getTemperature()));
        DataBuffer.data.getJSONObject(26).put("change_rate", String.valueOf(changeRateTemperature));
        DataBuffer.data.getJSONObject(27).put("value", "off");
        DataBuffer.data.getJSONObject(28).put("value", "on"); // Rohr 101 ober
        DataBuffer.data.getJSONObject(29).put("value", "on"); // Rohr 102 ersatz
        DataBuffer.data.getJSONObject(30).put("value", "on"); // Rohr 103 unter
        DataBuffer.data.getJSONObject(31).put("value", "off"); // MC 02
        setFaultValue();
        starttime = System.currentTimeMillis();
        simulatorCenterController.getWatchListGUI().refresh();
        simulatorCenterController.getFDMController().checkData(generateSendData(4));
        setFaultState();
    }

    protected void calcChangeRate(long timeDiff) {
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

    public void stop() {
        stopTimer.start();
        fillingTimer.stop();
        fillingReplaceTimer.stop();
        heatingTimer.stop();
        pumpingTimer.stop();
        airpumpingTimer.stop();
        gui.setTaskLabelText("Task: Stop");
        gui.setPumpState(false);
        gui.setValveState(false);
        gui.setValveStateV113(false);
        gui.setAirState(false);
        gui.setHeaterState(false);
        gui.setBallState(false);
        gui.setAirFlowRate(0.0);
        gui.setFlowRate(0.0);
        gui.setPressureRate(0.0);
    }

    public void reset() {
        stop();
        gui.setWaterLevel(initWaterLevel / SumWaterLevel);
        gui.setTemperatureDisplay(initWaterTemp);
        gui.setTemperatureDisplay2(initWaterTemp);
        gui.setAirPressure(initAirPressure);
        gui.setMCState(false);
        gui.setUltraSensor111(0);
        gui.setUltraSensor112(1);
        gui.setUltraSensor113(1);
        gui.setUltraSensor114(0);
        gui.setUltraSensor117(0);
        if (stopTimer.isRunning()) {
            stopTimer.stop();
        }
    }

    public void setFaultState() {
        for (int i = 0; i < DataBuffer.localFaultData.length(); i++) {
            JSONObject fault = DataBuffer.localFaultData.getJSONObject(i);
            switch (fault.getInt("component_id")) {
                case 1:
                    break;
                case 2:
                    gui.setHeaterState(false);
                    gui.setTemperatureDisplay(Double.valueOf(fault.getString("shift_value")));
                    gui.setTemperatureDisplay2(Double.valueOf(fault.getString("shift_value")));
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    gui.setValveStateID5(false);
                    break;
                case 6:
                    gui.setBallState(false);
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    gui.setSliderValue(Double.valueOf(fault.getString("shift_value")));
                    break;
                case 12:
                    break;
                case 13:
                    gui.setPumpState(false);
                    break;
                case 14:
                    gui.setPressureRate(Double.valueOf(fault.getString("shift_value")));
                    break;
                case 15:
                    gui.setFlowRate(Double.valueOf(fault.getString("shift_value")));
                    break;
                case 16:
                    break;
                case 17:
                    break;
                case 18:
                    break;
                case 19:
                    gui.setManualValveLower2State(false);
                    break;
                case 20:
                    gui.setAirValveState(false);
                    break;
                case 21:
                    gui.setAirFlowRate(Double.valueOf(fault.getString("shift_value")));
                    break;
                case 22:
                    gui.setAirPressure(Double.valueOf(fault.getString("shift_value")));
                    break;
                case 23:
                    gui.setAirPumpState(false);
                    break;
                case 24:
                    gui.setManualValveLowerState(false);
                    break;
                case 25:
                    gui.setManualValveState(false);
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    break;
                case 29:
                    break;
                case 30:
                    break;
                case 31:
                    break;
                case 32:
                    break;
                default:
                    break;
            }
        }

    }

    public void setFaultValue() {
        for (int i = 0; i < DataBuffer.localFaultData.length(); i++) {
            JSONObject fault = DataBuffer.localFaultData.getJSONObject(i);
            switch (fault.getInt("component_id")) {
                case 1:
                    DataBuffer.data.getJSONObject(0).put("value", "off");
                    break;
                case 2:
                    DataBuffer.data.getJSONObject(1).put("value", "off");
                    break;
                case 3:
                    DataBuffer.data.getJSONObject(2).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(2).put("change_rate", "0.0");
                    break;
                case 4:
                    DataBuffer.data.getJSONObject(3).put("value", "Defect");
                    break;
                case 5:
                    DataBuffer.data.getJSONObject(4).put("value", "off");
                    break;
                case 6:
                    DataBuffer.data.getJSONObject(5).put("value", "off");
                    break;
                case 7:
                    DataBuffer.data.getJSONObject(6).put("value", "off");
                    break;
                case 8:
                    DataBuffer.data.getJSONObject(7).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(7).put("change_rate", "0.0");
                    break;
                case 9:
                    DataBuffer.data.getJSONObject(8).put("value", String.valueOf(checkUltrasonicSensorState(9)));
                    DataBuffer.data.getJSONObject(8).put("change_rate", "0");
                    break;
                case 10:
                    DataBuffer.data.getJSONObject(9).put("value", String.valueOf(checkUltrasonicSensorState(10)));
                    DataBuffer.data.getJSONObject(9).put("change_rate", "0");
                    break;
                case 11:
                    DataBuffer.data.getJSONObject(10).put("value", "off");
                    break;
                case 12:
                    DataBuffer.data.getJSONObject(11).put("value", "off");
                    break;
                case 13:
                    DataBuffer.data.getJSONObject(12).put("value", "off");
                    break;
                case 14:
                    DataBuffer.data.getJSONObject(13).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(13).put("change_rate", "0.0");
                    break;
                case 15:
                    DataBuffer.data.getJSONObject(14).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(14).put("change_rate", "0.0");
                    break;
                case 16:
                    DataBuffer.data.getJSONObject(15).put("change_rate`", "0");
                    DataBuffer.data.getJSONObject(15).put("value", String.valueOf(checkUltrasonicSensorState(16)));
                    break;
                case 17:
                    DataBuffer.data.getJSONObject(16).put("change_rate", "0");
                    DataBuffer.data.getJSONObject(16).put("value", String.valueOf(checkUltrasonicSensorState(17)));
                    break;
                case 18:
                    DataBuffer.data.getJSONObject(17).put("change_rate", "0");
                    DataBuffer.data.getJSONObject(17).put("value", String.valueOf(checkUltrasonicSensorState(18)));
                    break;
                case 19:
                    DataBuffer.data.getJSONObject(18).put("value", "off");
                    break;
                case 20:
                    DataBuffer.data.getJSONObject(19).put("value", "off");
                    break;
                case 21:
                    DataBuffer.data.getJSONObject(20).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(20).put("change_rate", "0.0");
                    break;
                case 22:
                    DataBuffer.data.getJSONObject(21).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(21).put("change_rate", "0.0");
                    break;
                case 23:
                    DataBuffer.data.getJSONObject(22).put("value", "off");
                    break;
                case 24:
                    DataBuffer.data.getJSONObject(23).put("value", "off");
                    break;
                case 25:
                    DataBuffer.data.getJSONObject(24).put("value", "off");
                    break;
                case 26:
                    DataBuffer.data.getJSONObject(25).put("value", "Defect");
                    break;
                case 27:
                    DataBuffer.data.getJSONObject(26).put("value", fault.getString("shift_value"));
                    DataBuffer.data.getJSONObject(26).put("change_rate", "0.0");
                    break;
                case 28:
                    DataBuffer.data.getJSONObject(27).put("value", "off");
                    break;
                case 29:
                    DataBuffer.data.getJSONObject(28).put("value", "Defect");
                    break;
                case 30:
                    DataBuffer.data.getJSONObject(29).put("value", "Defect");
                    break;
                case 31:
                    DataBuffer.data.getJSONObject(30).put("value", "Defect");
                    break;
                case 32:
                    DataBuffer.data.getJSONObject(30).put("value", "off");
                    break;
                default:
                    break;
            }
        }
    }

    private int checkUltrasonicSensorState(int ComponentID) {
        int state = 0;
        switch (ComponentID) {
            case 9:
                if (gui.getWaterLevel() * 10 < 5) {
                    state = 0;
                } else {
                    state = 1;
                }
                break;
            case 10:
                if (gui.getWaterLevel() * 10 < 9) {
                    state = 0;
                } else {
                    state = 1;
                }
                break;
            case 16:
                if (gui.getTankLevel() * 10 < 9) {
                    state = 0;
                } else {
                    state = 1;
                }
                break;
            case 17:
                if (gui.getTankLevel() * 10 < 5) {
                    state = 0;
                } else {
                    state = 1;
                }
                break;
            case 18:
                if (gui.getWaterLevel() * 10 < 1) {
                    state = 0;
                } else {
                    state = 1;
                }
                break;
            default:
                break;
        }
        return state;
    }

    private JSONObject generateSendData(int function_id) {
        JSONObject sendData = new JSONObject();
        sendData.put("components", DataBuffer.data);
        sendData.put("stamp_time", String.valueOf(System.currentTimeMillis()));
        sendData.put("task_id", task_id);
        sendData.put("function_id", function_id);
        return sendData;
    }
}
