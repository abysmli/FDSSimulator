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
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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

    public JSONArray getFaults() throws SQLException, NamingException {
        this.initConnections();
        ResultSet result = stmt.executeQuery("SELECT * FROM fault_table");
        JSONArray jsonarray = new JSONArray();
        while (result.next()) {
            JSONObject obj = new JSONObject();
            obj.put("fault_id", result.getInt(1));
            obj.put("component_id", result.getInt(2));
            obj.put("series", result.getString(3));
            obj.put("fault_type", result.getString(4));
            obj.put("fault_desc", result.getString(5));
            obj.put("execute_command", result.getString(6));
            obj.put("insert_date", result.getTimestamp(7));
            jsonarray.put(obj);
        }
        result.close();
        this.releaseConnections();
        return jsonarray;
    }

    public void saveFault(JSONObject mMainObj) throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate(
                "INSERT INTO `FRS_Simulator`.`fault_table` (`fault_id`, `component_id`, `series`, `fault_type`, `fault_desc`, `execute_command`, `insert_date`, `update_date`) VALUES (NULL, '"
                + mMainObj.getInt("component_id") + "', '" + mMainObj.getString("series") + "', '" + mMainObj.getString("fault_type") + "', '"
                + mMainObj.getString("fault_desc") + "', '" + mMainObj.getJSONObject("execute_command")
                + "', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        this.releaseConnections();
    }

    public JSONObject resetDatabase() throws SQLException, NamingException {
        this.initConnections();
        stmt.executeUpdate("TRUNCATE `FRS_Simulator`.`fault_table`");
        this.releaseConnections();
        JSONObject obj = new JSONObject();
        obj.put("result", "success");
        return obj;
    }
}
