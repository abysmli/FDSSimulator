/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.model;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.NamingException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class DatabaseHandler {

    private MysqlDataSource DataSource;
    private Statement stmt;
    private Connection connection;

    public DatabaseHandler() {
        DataSource = new MysqlDataSource();
        DataSource.setDatabaseName("FRS_Simulator");
        DataSource.setServerName("localhost");
        DataSource.setPort(3306);
        DataSource.setUser("FDS");
        DataSource.setPassword("FDS");
    }

    public void initConnections() throws NamingException, SQLException {
        this.connection = DataSource.getConnection();
        this.stmt = connection.createStatement();
    }

    private void releaseConnections() throws SQLException {
        this.stmt.close();
        this.connection.close();
    }

    public JSONArray getComponents() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM component_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("component_id", result.getInt(1));
            obj.put("component_name", result.getString(2));
            obj.put("series", result.getString(3));
            obj.put("type", result.getString(4));
            obj.put("component_symbol", result.getString(5));
            obj.put("component_desc", result.getString(6));
            obj.put("activition", result.getString(7));
            obj.put("status", result.getString(8));
            obj.put("insert_date", result.getTimestamp(9));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public JSONArray getFaultKnowledge() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_knowledge");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("fault_id", result.getInt(1));
            obj.put("fault_no", result.getInt(2));
            obj.put("fault_name", result.getString(3));
            obj.put("symptom_id", result.getInt(4));
            obj.put("symptom_desc", result.getString(5));
            obj.put("available_functions", result.getString(6));
            obj.put("reconf_command", result.getString(7));
            obj.put("fault_parameter", result.getString(8));
            obj.put("fault_value", result.getString(9));
            obj.put("fault_effect", result.getString(10));
            obj.put("fault_location", result.getString(11));
            obj.put("fault_message", result.getString(12));
            obj.put("check_status", result.getString(13));
            obj.put("equipment_id", result.getString(14));
            obj.put("occured_at", result.getTimestamp(15));
            obj.put("update_at", result.getTimestamp(16));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public void saveFault(JSONObject mMainObj) throws SQLException, NamingException {
        this.initConnections();
        JSONObject reconfCommand = mMainObj.getJSONObject("reconf_command");
        JSONObject availableFunctions = mMainObj.getJSONObject("available_functions");
        stmt.executeUpdate(
                "INSERT INTO `fault_knowledge` (`fault_id`, `fault_no`, `fault_name`, `symptom_id`, `symptom_desc`, `available_functions`, `reconf_command`, `fault_parameter`, `fault_value`, `fault_effect`, `fault_location`, `fault_message`, `check_status`, `equipment_id`, `occured_at`) VALUES (NULL, '"
                + mMainObj.getInt("fault_no") + "', '"
                + mMainObj.getString("fault_name") + "', '"
                + mMainObj.getInt("symptom_id") + "', '"
                + mMainObj.getString("symptom_desc") + "', '"
                + availableFunctions.toString() + "', '"
                + reconfCommand.toString() + "', '"
                + mMainObj.getString("fault_parameter") + "', '"
                + mMainObj.getString("fault_value") + "', '"
                + mMainObj.getString("fault_effect") + "', '"
                + mMainObj.getString("fault_location") + "', '"
                + mMainObj.getString("fault_message") + "', '"
                + mMainObj.getString("check_status") + "', '"
                + mMainObj.getString("equipment_id")
                + "', CURRENT_TIMESTAMP)");
        this.releaseConnections();
    }

    public JSONArray getTasks() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM task_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("task_id", result.getInt(1));
            obj.put("task_name", result.getString(2));
            obj.put("required_resource", result.getString(3));
            obj.put("required_mainfunction", result.getString(4));
            obj.put("insert_date", result.getTimestamp(5));
            obj.put("update_date", result.getTimestamp(6));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }
    
    public void updateRuntimeData(JSONObject mResult) throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `runtime_data` (meta_data, task_id, function_id, timestamp) VALUES ('"
                + mResult.getJSONArray("components").toString() + "', "
                + mResult.getInt("task_id") + ", "
                + mResult.getInt("function_id") + ", "
                + mResult.getString("stamp_time") + ")");
        this.releaseConnections();
    }

    public JSONObject resetDatabase() throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate("TRUNCATE `FRS_Simulator`.`fault_knowledge`");
        this.releaseConnections();
        JSONObject obj = new JSONObject();
        obj.put("result", "success");
        return obj;
    }
}
