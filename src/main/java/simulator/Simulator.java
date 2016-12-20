package simulator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;

import simulator.controllers.SimulatorCenterController;
import fds.FDMGUI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abysmli
 */
public class Simulator extends JFrame {

    private Simulator Application;
    private FDMGUI FDMGui;

    public void init() {
        setTitle("Remote Fault Handling and Reconfiguration System Simulator");
        setBounds(0, 0, 1302, 800);
        setSize(1302, 800);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        this.Application = this;
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                int confirm = JOptionPane.showOptionDialog(Application,
//                        "Are You Sure to Exit this Application?",
//                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
//                        JOptionPane.QUESTION_MESSAGE, null, null, null);
//                if (confirm == 0) {
//                    System.exit(0);
//                }
//            }
//        });
    }

    public void start() {
        try {
            SimulatorCenterController simulatorController = new SimulatorCenterController(this, FDMGui);
            setVisible(true);
        } catch (Exception e) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void startFDMGUI() {
        JFrame frame = new JFrame("Fault Diagnose GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        FDMGui = new FDMGUI();
        FDMGui.init();
        frame.setContentPane(FDMGui.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        FDMGui.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        Simulator simulator = new Simulator();
        simulator.init();
        simulator.startFDMGUI();
        simulator.start();
    }
}
