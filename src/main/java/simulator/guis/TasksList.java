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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.controllers.SimulatorCenterController;

/**
 *
 * @author abysmli
 */
public class TasksList extends JPanel {

    private final SimulatorCenterController simulatorCenterController;
    private DefaultTableModel avtastlist;
    private JTable tasktable;
    private ListSelectionModel model;
    private JLabel tasklabel;

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
        taskpanel.setBounds(0, 50, 300, 690);
        taskpanel.setLayout(null);
        String[] taskcolumn = {"Nr.", "ID", "Task Name", "Status"};
        avtastlist = new DefaultTableModel();
        tasktable = new JTable();
        tasktable.setRowSelectionAllowed(true);
        tasktable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tasktable.setRowHeight(20);
        avtastlist.setColumnIdentifiers(taskcolumn);

        tasktable.setModel(avtastlist);
        tasktable.setEnabled(false);

        model = tasktable.getSelectionModel();
        model.clearSelection();

        JScrollPane taskscroll = new JScrollPane(tasktable);
        taskscroll.setBounds(2, 0, 299, 640);
        taskpanel.add(taskscroll);

        JButton clearbutton = new JButton("Remove Tasks");
        clearbutton.setBounds(16, 700, 168, 25);
        clearbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delTasks();
            }
        });
        tasklabel = new JLabel("List of Tasks");
        tasklabel.setFont(new Font("Ubuntu", 0, 20));
        tasklabel.setBounds(20, 10, 150, 20);
        tasktable.getColumnModel().getColumn(0).setPreferredWidth(30);
        tasktable.getColumnModel().getColumn(1).setPreferredWidth(30);
        tasktable.getColumnModel().getColumn(2).setPreferredWidth(180);
        tasktable.getColumnModel().getColumn(3).setPreferredWidth(80);
        add(tasklabel);
        add(clearbutton);
        add(taskpanel);
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
            JSONObject taskObj=new JSONObject();
            taskObj.put("task_nr", avtastlist.getValueAt(i, 0));
            taskObj.put("task_id", avtastlist.getValueAt(i, 1));
            taskObj.put("task_name", avtastlist.getValueAt(i, 2));
            taskObj.put("task_status", avtastlist.getValueAt(i, 3));
            mTaskList.put(taskObj);
        }
        return mTaskList;
    }
}
