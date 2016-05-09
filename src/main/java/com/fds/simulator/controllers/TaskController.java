/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fds.simulator.controllers;

import com.fds.simulator.guis.Gui;
import com.fds.simulator.guis.MenuGui;
import com.fds.simulator.utils.DataBuffer;
import com.fds.simulator.utils.ErrorLogger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author abysmli
 */
public class TaskController extends ProcessController {

    private int TaskNo = 0;
    private int ProcessNo;
    private final Timer SimulationAllProcessTaskTimer, SimulationWithHeatTaskTimer, SimulationWithAirPumpTaskTimer, CleanTaskTimer;

    /**
     *
     * @param simulatorCenterController
     * @param gui
     * @param menuGui
     */
    public TaskController(SimulatorCenterController simulatorCenterController, Gui gui, MenuGui menuGui) {
        super(simulatorCenterController, gui, menuGui);
        ActionListener SimulationAllProcessTimerListener = (ActionEvent e) -> {
            try {
                switch (ProcessNo) {
                    case 1: 
                        if (gui.getAirPressure() >= sollAirPressure) {
                            ProcessNo = 2;
                        } else {
                            AirPumpingProcess();
                        }
                        break;
                    case 2:
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            ProcessNo = 3;
                        } else {
                            FillLowerTankProcess();
                        }
                        break;
                    case 3:
                        CheckSollWaterLevelProcess();
                        ProcessNo = 4;
                        break;
                    case 4:
                        if (gui.getTemperature() > sollWaterTemp) {
                            ProcessNo = 5;
                        } else {
                            HeatProcess();
                        }
                        break;
                    case 5:
                        CheckTemperaturProcess();
                        ProcessNo = 6;
                        break;
                    case 6:
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            ProcessNo = 7;
                        } else {
                            FillUpperTankProcess();
                        }
                        break;
                    case 7:
                        CheckInitWaterLevelProcess();
                        ProcessNo = 1;
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        SimulationAllProcessTaskTimer = new Timer(delay, SimulationAllProcessTimerListener);
        
        ActionListener SimulationHeatTimerListener = (ActionEvent e) -> {
            try {
                switch (ProcessNo) {
                    case 1:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 2) {
                                simulatorCenterController.Stop();
                            }
                        });
                        super.FillLowerTankProcess();
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            ProcessNo = 2;
                        }
                        break;
                    case 2:
                        CheckSollWaterLevelProcess();
                        ProcessNo = 3;
                        break;
                    case 3:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 1) {
                                simulatorCenterController.Stop();
                            }
                        });
                        if (gui.getTemperature() > sollWaterTemp) {
                            ProcessNo = 4;
                        } else {
                            HeatProcess();
                        }
                        break;
                    case 4:
                        CheckTemperaturProcess();
                        ProcessNo = 5;
                        break;
                    case 5:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 3) {
                                simulatorCenterController.Stop();
                            }
                        });
                        FillUpperTankProcess();
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            ProcessNo = 6;
                        }
                        break;
                    case 6:
                        CheckInitWaterLevelProcess();
                        ProcessNo = 1;
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        SimulationWithHeatTaskTimer = new Timer(delay, SimulationHeatTimerListener);
        
        ActionListener SimulationAirPumpTimerListener = (ActionEvent e) -> {
            try {
                switch (ProcessNo) {
                    case 1:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 2) {
                                simulatorCenterController.Stop();
                            }
                        });
                        super.FillLowerTankProcess();
                        AirPumpingProcess();
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            ProcessNo = 2;
                        }
                        break;
                    case 2:
                        CheckSollWaterLevelProcess();
                        ProcessNo = 3;
                        break;
                    case 3:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 3) {
                                simulatorCenterController.Stop();
                            }
                        });
                        FillUpperTankProcess();
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            ProcessNo = 4;
                        }
                        break;
                    case 4:
                        CheckInitWaterLevelProcess();
                        ProcessNo = 1;
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        SimulationWithAirPumpTaskTimer = new Timer(delay, SimulationAirPumpTimerListener);
        
        ActionListener CleanTimerListener = (ActionEvent e) -> {
            try {
                switch (ProcessNo) {
                    case 1:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 2) {
                                simulatorCenterController.Stop();
                            }
                        });
                        super.FillLowerTankProcess();
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            ProcessNo = 2;
                        }
                        break;
                    case 2:
                        CheckSollWaterLevelProcess();
                        ProcessNo = 3;
                        break;
                    case 3:
                        DataBuffer.deactivedFunction.forEach(function_id -> {
                            if (function_id == 3) {
                                simulatorCenterController.Stop();
                            }
                        });
                        FillUpperTankProcess();
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            ProcessNo = 4;
                        }
                        break;
                    case 4:
                        CheckInitWaterLevelProcess();
                        ProcessNo = 1;
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        CleanTaskTimer = new Timer(delay, CleanTimerListener);
    }

    public void startSimulationAllProcess() {
        stop();
        TaskNo = 1;
        ProcessNo = 1;
        starttime = System.currentTimeMillis();
        SimulationAllProcessTaskTimer.start();
    }

    public void startSimulationWithHeat() {
        stop();
        TaskNo = 2;
        ProcessNo = 1;
        starttime = System.currentTimeMillis();
        SimulationWithHeatTaskTimer.start();
    }

    public void startSimulationWithAirPump() {
        stop();
        TaskNo = 3;
        ProcessNo = 1;
        starttime = System.currentTimeMillis();
        SimulationWithAirPumpTaskTimer.start();
    }

    public void startClean() {
        stop();
        TaskNo = 4;
        ProcessNo = 1;
        starttime = System.currentTimeMillis();
        CleanTaskTimer.start();
    }

    @Override
    public void stop() {
        SimulationAllProcessTaskTimer.stop();
        SimulationWithHeatTaskTimer.stop();
        SimulationWithAirPumpTaskTimer.stop();
        CleanTaskTimer.stop();
        super.stop();
    }

    @Override
    public void reset() {
        TaskNo = 0;
        SimulationAllProcessTaskTimer.stop();
        SimulationWithHeatTaskTimer.stop();
        SimulationWithAirPumpTaskTimer.stop();
        CleanTaskTimer.stop();
        super.reset();
    }

    public void checkTastAvailability() {

    }
}
