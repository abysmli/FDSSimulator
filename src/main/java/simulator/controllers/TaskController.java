/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.controllers;

import simulator.guis.Gui;
import simulator.guis.MenuGui;
import simulator.utils.ErrorLogger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

/**
 *
 * @author abysmli
 */
public class TaskController extends FunctionController {

    public final List<Integer> taskList;
    private int FunctionNumber;
    private final Timer AllFunctionsTimer, CleanTaskTimer, StartTimer;
    private boolean signalFlag = true;

    /**
     *
     * @param simulatorCenterController
     * @param gui
     * @param menuGui
     */
    public TaskController(SimulatorCenterController simulatorCenterController, Gui gui, MenuGui menuGui) {
        super(simulatorCenterController, gui, menuGui);
        this.taskList = new ArrayList<>();
        ActionListener AllFunctionsTimerListener = (ActionEvent e) -> {
            try {
                switch (FunctionNumber) {
                    case 1:
                        if (gui.getAirPressure() >= sollAirPressure) {
                            FunctionNumber = 2;
                        } else {
                            AirPumpingProcess();
                        }
                        break;
                    case 2:
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            FunctionNumber = 3;
                        } else {
                            FillLowerTankProcess();
                        }
                        break;
                    case 3:
                        CheckSollWaterLevelProcess();
                        FunctionNumber = 4;
                        break;
                    case 4:
                        if (gui.getTemperature() > sollWaterTemp && gui.getTemperature() < sollWaterTemp + 1) {
                            FunctionNumber = 5;
                        } else {
                            HeatProcess();
                        }
                        break;
                    case 5:
                        CheckTemperaturProcess();
                        FunctionNumber = 6;
                        break;
                    case 6:
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            FunctionNumber = 7;
                        } else {
                            FillUpperTankProcess();
                        }
                        break;
                    case 7:
                        CheckInitWaterLevelProcess();
                        this.stop();
                        removeFinishedTask();
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        AllFunctionsTimer = new Timer(delay, AllFunctionsTimerListener);

//        ActionListener SimulationHeatTimerListener = (ActionEvent e) -> {
//            try {
//                switch (FunctionNumber) {
//                    case 1:
//                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
//                            FunctionNumber = 2;
//                        } else {
//                            FillLowerTankProcess();
//                        }
//                        break;
//                    case 2:
//                        CheckSollWaterLevelProcess();
//                        FunctionNumber = 3;
//                        break;
//                    case 3:
//                        if (gui.getTemperature() > sollWaterTemp) {
//                            FunctionNumber = 4;
//                        } else {
//                            HeatProcess();
//                        }
//                        break;
//                    case 4:
//                        CheckTemperaturProcess();
//                        FunctionNumber = 5;
//                        break;
//                    case 5:
//                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
//                            FunctionNumber = 6;
//                        } else {
//                            FillUpperTankProcess();
//                        }
//                        break;
//                    case 6:
//                        CheckInitWaterLevelProcess();
//                        FunctionNumber = 1;
//                        break;
//                    default:
//                        break;
//                }
//            } catch (Exception e1) {
//                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
//            }
//        };
//        SimulationWithHeatTaskTimer = new Timer(delay, SimulationHeatTimerListener);
//
//        ActionListener SimulationAirPumpTimerListener = (ActionEvent e) -> {
//            try {
//                switch (FunctionNumber) {
//                    case 1:
//                        if (gui.getAirPressure() >= sollAirPressure) {
//                            FunctionNumber = 2;
//                        } else {
//                            AirPumpingProcess();
//                        }
//                        break;
//                    case 2:
//                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
//                            FunctionNumber = 3;
//                        } else {
//                            FillLowerTankProcess();
//                        }
//                        break;
//                    case 3:
//                        CheckSollWaterLevelProcess();
//                        FunctionNumber = 4;
//                        break;
//                    case 4:
//                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
//                            FunctionNumber = 5;
//                        } else {
//                            FillUpperTankProcess();
//                        }
//                        break;
//                    case 5:
//                        CheckInitWaterLevelProcess();
//                        FunctionNumber = 1;
//                        break;
//                    default:
//                        break;
//                }
//            } catch (Exception e1) {
//                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
//            }
//        };
//        SimulationWithAirPumpTaskTimer = new Timer(delay, SimulationAirPumpTimerListener);
        ActionListener CleanTimerListener = (ActionEvent e) -> {
            try {
                switch (FunctionNumber) {
                    case 1:
                        if (gui.getWaterLevel() >= sollWaterLevel / SumWaterLevel) {
                            FunctionNumber = 2;
                        } else {
                            FillLowerTankProcess();
                        }
                        break;
                    case 2:
                        CheckSollWaterLevelProcess();
                        FunctionNumber = 3;
                        break;
                    case 3:
                        if (gui.getWaterLevel() <= initWaterLevel / SumWaterLevel) {
                            FunctionNumber = 4;
                        } else {
                            FillUpperTankProcess();
                        }
                        break;
                    case 4:
                        CheckInitWaterLevelProcess();
                        FunctionNumber = 1;
                        this.stop();
                        removeFinishedTask();
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                ErrorLogger.log(e1, "Error Exception", e1.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        };
        CleanTaskTimer = new Timer(delay, CleanTimerListener);

        ActionListener StartTimerListener = (ActionEvent e) -> {
            if (!taskList.isEmpty()) {
                if (!AllFunctionsTimer.isRunning() && !CleanTaskTimer.isRunning()) {
                    switch (taskList.get(0)) {
                        case 1:
                            Heat35Water();
                            super.gui.setProcessLabelText("Current Task: Heat 35°C Water");
                            break;
                        case 2:
                            Heat55Water();
                            super.gui.setProcessLabelText("Current Task: Heat 55°C Water");
                            break;
                        case 3:
                            Heat75Water();
                            super.gui.setProcessLabelText("Current Task: Heat 75°C Water");
                            break;
                        case 4:
                            startClean();
                            super.gui.setProcessLabelText("Current Task: Clean Pipe");
                            break;
                        default:
                            break;
                    }
                }
            } else {
                this.reset();
            }
        };
        StartTimer = new Timer(delay, StartTimerListener);
    }

    public void Heat35Water() {
        starttime = System.currentTimeMillis();
        super.sollWaterTemp = 35;
        FunctionNumber = 1;
        AllFunctionsTimer.start();

    }

    public void Heat55Water() {
        starttime = System.currentTimeMillis();
        super.sollWaterTemp = 55;
        FunctionNumber = 1;
        AllFunctionsTimer.start();
    }

    public void Heat75Water() {
        starttime = System.currentTimeMillis();
        super.sollWaterTemp = 75;
        FunctionNumber = 1;
        AllFunctionsTimer.start();
    }

    public void startClean() {
        starttime = System.currentTimeMillis();
        FunctionNumber = 1;
        CleanTaskTimer.start();
    }

    @Override
    public void stop() {
        AllFunctionsTimer.stop();
        CleanTaskTimer.stop();
        super.stop();
    }

    @Override
    public void reset() {
        AllFunctionsTimer.stop();
        CleanTaskTimer.stop();
        StartTimer.stop();
        super.reset();
    }

    void startTasks() {
        StartTimer.start();
    }

    private void removeFinishedTask() {
        taskList.remove(0);
        super.simulatorCenterController.tasksList.removeTasks(0);
    }
   
}
