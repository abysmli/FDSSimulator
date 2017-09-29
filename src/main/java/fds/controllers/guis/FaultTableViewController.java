/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.controllers.guis;

import fds.model.Fault_List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysm
 */
public class FaultTableViewController {

    /* ----------------- faultTable -------------------- */
    private AnchorPane FaulTablePane;
    private TableView<Fault_List> faultTable = new TableView<>();
    private TableColumn<Fault_List, Integer> faultIdColumn, faultNrColumn;
    private TableColumn<Fault_List, String> faultNameColumn, faultParameterColumn, faultLocationColumn, symptomDescColumn, insertDateColumn;
    private TableColumn<Fault_List, Object> reconfCommandColumn;
    private final ObservableList<Fault_List> faultData = FXCollections.observableArrayList();

    public FaultTableViewController(AnchorPane FaultTablePane) {
        this.FaulTablePane = FaultTablePane;
    }

    public void generateFaultTable(JSONArray mFaults) throws Exception {
        for (int i = 0; i < mFaults.length(); i++) {
            JSONObject obj = mFaults.getJSONObject(i);
            if (i >= faultData.size()) {
                faultData.add(new Fault_List(obj.getInt("fault_id"), obj.getInt("fault_no"),
                        obj.getString("fault_name"), obj.getString("fault_parameter"), String.valueOf(obj.getString("fault_location")), obj.getString("symptom_desc"), new JSONObject(obj.getString("reconf_command")),
                        ""));
            }
        }
    }

    public void initFaultTable() {
        faultTable.setEditable(false);
        faultIdColumn = new TableColumn<>("Fault Id");
        faultNrColumn = new TableColumn<>("Fault Nr");
        faultNameColumn = new TableColumn<>("Fault Name");
        faultParameterColumn = new TableColumn<>("Fault Parameter");
        faultLocationColumn = new TableColumn<>("Fault Location");
        symptomDescColumn = new TableColumn<>("Fault Description");
        reconfCommandColumn = new TableColumn<>("Reconf. Command");
        insertDateColumn = new TableColumn<>("Occurred At");
        faultIdColumn.setCellValueFactory(new PropertyValueFactory<>("faultId"));
        faultNrColumn.setCellValueFactory(new PropertyValueFactory<>("faultNr"));
        faultNameColumn.setCellValueFactory(new PropertyValueFactory<>("faultName"));
        faultParameterColumn.setCellValueFactory(new PropertyValueFactory<>("faultParameter"));
        faultLocationColumn.setCellValueFactory(new PropertyValueFactory<>("faultLocation"));
        symptomDescColumn.setCellValueFactory(new PropertyValueFactory<>("symptomDesc"));
        reconfCommandColumn.setCellValueFactory(new PropertyValueFactory<>("reconfCommand"));
        insertDateColumn.setCellValueFactory(new PropertyValueFactory<>("insertDate"));
        faultTable.getColumns().addAll(faultIdColumn, faultNrColumn, faultNameColumn, faultParameterColumn, faultLocationColumn, symptomDescColumn, reconfCommandColumn, insertDateColumn);
        faultTable.setItems(faultData);
        faultTable.prefHeightProperty().bind(FaulTablePane.heightProperty());
        faultTable.prefWidthProperty().bind(FaulTablePane.widthProperty());
        faultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        FaulTablePane.getChildren().add(faultTable);
    }
}
