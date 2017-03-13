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
                        if (gui.getTemperature() >= sollWaterTemp) {
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
                        this.finishTaskStop();
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
                        this.finishTaskStop();
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
                            AutomaticCycling();
                            super.gui.setTaskLabelText("Current Task: Automatic Cycling 30°C");
                            break;
                        case 2:
                            Water3L45();
                            super.gui.setTaskLabelText("Current Task: Heat 3L,45°C Water");
                            break;
                        case 3:
                            Water5L();
                            super.gui.setTaskLabelText("Current Task: Pour 5L Water");
                            break;
                        case 4:
                            startClean();
                            super.gui.setTaskLabelText("Current Task: Clean Pipe");
                            break;
                        default:
                            break;
                    }
                }
            } else {
                this.reset();
            }
        };
        StartTimer = new Timer(500, StartTimerListener);
    }

    public void AutomaticCycling() {
        starttime = System.currentTimeMillis();
        super.task_id = 1;
        super.sollWaterTemp = 30;
        FunctionNumber = 1;
        AllFunctionsTimer.start();

    }

    public void Water3L45() {
        starttime = System.currentTimeMillis();
        super.task_id = 3;
        super.sollWaterTemp = 45;
        super.sollWaterLevel = 3;
        FunctionNumber = 1;
        AllFunctionsTimer.start();
    }

    public void Water5L() {
        starttime = System.currentTimeMillis();
        super.task_id = 2;
        super.sollWaterLevel = 5;
        FunctionNumber = 1;
        AllFunctionsTimer.start();
    }

    public void startClean() {
        starttime = System.currentTimeMillis();
        super.task_id = 4;
        FunctionNumber = 1;
        CleanTaskTimer.start();
    }
    
    public void finishTaskStop() {
        AllFunctionsTimer.stop();
        CleanTaskTimer.stop();
        super.stop();
    }

    @Override
    public void stop() {
        StartTimer.stop();
        AllFunctionsTimer.stop();
        CleanTaskTimer.stop();
        super.stop();
    }

    @Override
    public void reset() {
        AllFunctionsTimer.stop();
        CleanTaskTimer.stop();
        StartTimer.stop();
        super.simulatorCenterController.buttonStatusChange(false);
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
