/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator.guis;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.controllers.SimulatorCenterController;
import simulator.utils.ErrorLogger;
import simulator.utils.SimulatorSetting;

/**
 *
 * @author abysmli
 */
public class TasksList extends JPanel {

    private final SimulatorCenterController simulatorCenterController;
    private DefaultTableModel avtastlist;
    private JTable taskTable;
    private ListSelectionModel model;
    private JLabel taskLabel;
    private JLabel equipmentLabel;
    private JLabel addressLabel;
    private JLabel remoteLabel;
    private JTextField remoteTextField;

    /**
     * Create the panel.
     *
     * @param simulatorCenterController
     * @throws Exception
     */
    public TasksList(SimulatorCenterController simulatorCenterController) throws Exception {
        this.simulatorCenterController = simulatorCenterController;
    }

    public void init() throws Exception {
        setBorder(new LineBorder(new Color(128, 128, 128)));
        setBounds(797, 0, 304, 800);
        setLayout(null);

        JPanel taskpanel = new JPanel();
        taskpanel.setBounds(0, 50, 300, 490);
        taskpanel.setLayout(null);
        String[] taskcolumn = {"Nr.", "ID", "Task Name", "Status"};
        avtastlist = new DefaultTableModel();
        taskTable = new JTable();
        taskTable.setRowSelectionAllowed(true);
        taskTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        taskTable.setRowHeight(20);
        avtastlist.setColumnIdentifiers(taskcolumn);

        taskTable.setModel(avtastlist);
        taskTable.setEnabled(false);

        model = taskTable.getSelectionModel();
        model.clearSelection();

        JScrollPane taskscroll = new JScrollPane(taskTable);
        taskscroll.setBounds(3, 0, 298, 490);
        taskpanel.add(taskscroll);

        JButton clearbutton = new JButton("Remove Tasks");
        clearbutton.setBounds(20, 550, 168, 25);
        clearbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delTasks();
            }
        });
        taskLabel = new JLabel("List of Tasks");
        taskLabel.setFont(new Font("Ubuntu", 0, 20));
        taskLabel.setBounds(20, 10, 150, 20);
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(30);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);

        equipmentLabel = new JLabel("Equipment ID: " + SimulatorSetting.EquipmentID);
        equipmentLabel.setFont(new Font("Ubuntu", 0, 14));
        equipmentLabel.setBounds(20, 600, 290, 20);

        addressLabel = new JLabel("Local IP Address: " + InetAddress.getLocalHost().getHostAddress());
        addressLabel.setFont(new Font("Ubuntu", 0, 14));
        addressLabel.setBounds(20, 630, 290, 20);

        remoteLabel = new JLabel("Server IP Address: ");
        remoteLabel.setFont(new Font("Ubuntu", 0, 14));
        remoteLabel.setBounds(20, 660, 290, 20);

        remoteTextField = new JTextField(SimulatorSetting.FDSAddress);
        remoteTextField.setBounds(20, 690, 260, 27);

        JButton connectButton = new JButton("Connect Server");
        connectButton.setBounds(20, 730, 168, 25);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimulatorSetting.FDSAddress = remoteTextField.getText();
                simulatorCenterController.checkConnection();
            }
        });

        add(taskLabel);
        add(clearbutton);
        add(taskpanel);
        add(equipmentLabel);
        add(addressLabel);
        add(remoteLabel);
        add(remoteTextField);
        add(connectButton);
    }

    public void addTasks(JSONObject taskObj) {
        avtastlist.addRow(new String[]{String.valueOf(avtastlist.getRowCount() + 1), String.valueOf(taskObj.getInt("task_id")), taskObj.getString("task_name"), taskObj.getString("task_status")});
    }

    public void removeTasks(int index) {
        avtastlist.removeRow(index);
    }

    private void delTasks() {
        for (int i = avtastlist.getRowCount() - 1; i > -1; i--) {
            avtastlist.removeRow(i);
        }
        simulatorCenterController.clearTasks();
    }

    public void disableTask(int TaskNo) {
        avtastlist.moveRow(TaskNo - 1, TaskNo - 1, avtastlist.getRowCount() - 1);
    }

    public JSONArray getList() {
        JSONArray mTaskList = new JSONArray();
        for (int i = 0; i < avtastlist.getRowCount(); i++) {
            JSONObject taskObj = new JSONObject();
            taskObj.put("task_nr", avtastlist.getValueAt(i, 0));
            taskObj.put("task_id", avtastlist.getValueAt(i, 1));
            taskObj.put("task_name", avtastlist.getValueAt(i, 2));
            taskObj.put("task_status", avtastlist.getValueAt(i, 3));
            mTaskList.put(taskObj);
        }
        return mTaskList;
    }
}
