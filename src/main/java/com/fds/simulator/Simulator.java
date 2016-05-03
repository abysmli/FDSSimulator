package com.fds.simulator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.awt.*;

import com.fds.simulator.guis.Gui;
import com.fds.simulator.guis.MenuGui;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.fds.simulator.controllers.AbfuellanlageLogic;
import com.fds.simulator.utils.ErrorLogger;

/**
 *
 * @author abysmli
 */
public class Simulator extends JPanel implements Runnable {
    
    private static final long serialVersionUID = 1L;
    private final static double initWaterLevel = 2;
    private final static double initWaterTemp = 25;
    private final static double SumWaterLevel = 12;
    
    @Override
    public void run() {
        setLayout(null);
        try {
            Gui gui = new Gui();
            add(gui);
            gui.init();
            gui.setWaterLevel(initWaterLevel / SumWaterLevel);
            gui.setHeaterState(false);
            gui.setTemperatureDisplay(initWaterTemp);
            
            MenuGui menugui = new MenuGui();
            add(menugui);
            
            AbfuellanlageLogic ab;
            ab = new AbfuellanlageLogic(gui, menugui);
        } catch (Exception e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.run();
        JFrame jf = new JFrame();
        Container c = jf.getContentPane();
        c.add(simulator);
        jf.setBounds(0, 0, 1100, 800);
        jf.setSize(1100, 800);
        jf.setResizable(false);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(jf,
                        "Are You Sure to Exit this Application?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    System.exit(0);
                }
            }
        });
    }
    
}
