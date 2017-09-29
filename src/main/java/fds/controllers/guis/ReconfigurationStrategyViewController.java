/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.controllers.guis;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

/**
 *
 * @author abysm
 */
public class ReconfigurationStrategyViewController {

    private AnchorPane ReconfPane;

    public ReconfigurationStrategyViewController(AnchorPane ReconfPane) {
        this.ReconfPane = ReconfPane;
    }

    public void generateReconfigurationStratgyView(JSONObject result) {
        /* setting Pane */
        VBox ReconfVBox = new VBox();
        ReconfVBox.setLayoutY(20);
        ReconfVBox.setLayoutX(30);
        ReconfVBox.setStyle("-fx-spacing: 20px;");
        int fault_no = result.getInt("fault_no");
        String fault_name = result.getString("fault_name");
        int symptom_id = result.getInt("symptom_id");
        String symptom_desc = result.getString("symptom_desc");
        JSONObject reconfCommand = result.getJSONObject("reconf_command");
        JSONObject availableFunctions = result.getJSONObject("available_functions");
        String fault_parameter = result.getString("fault_parameter");
        String fault_value = result.getString("fault_value");
        String fault_effect = result.getString("fault_effect");
        String fault_location = result.getString("fault_location");
        String fault_message = result.getString("fault_message");
        String check_status = result.getString("check_status");
        String equipment_id = result.getString("equipment_id");

        Label fault_no_label = new Label("Fault Number: " + String.valueOf(fault_no));
        Label fault_name_label = new Label("Fault Name: " + fault_name);
        Label symptom_id_label = new Label("Symptom ID: " + String.valueOf(symptom_id));
        Label symptom_desc_label = new Label("Symptom Description: " + symptom_desc);
        Label reconfCommand_label = new Label("Reconfiguration Command: " + reconfCommand.toString());
        reconfCommand_label.setWrapText(true);
        Label availableFunctions_label = new Label("Available Functions: " + availableFunctions.toString());
        availableFunctions_label.setWrapText(true);
        Label fault_parameter_label = new Label("Fault Parameter: " + fault_parameter);
        Label fault_value_label = new Label("Fault Value: " + fault_value);
        Label fault_effect_label = new Label("Fault Effect: " + fault_effect);
        Label fault_location_label = new Label("Fault Location: " + fault_location);
        Label fault_message_label = new Label("Fault Message: " + fault_message);
        Label check_status_label = new Label("Check Status: " + check_status);
        Label equipment_id_label = new Label("Equipment ID: " + equipment_id);

        fault_no_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_name_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        symptom_id_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        symptom_desc_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        reconfCommand_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        availableFunctions_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_parameter_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_value_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_effect_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_location_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_message_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        check_status_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        equipment_id_label.setStyle("-fx-font: 12 \"Segoe UI\";");
        fault_no_label.setMaxWidth(700);
        fault_name_label.setMaxWidth(700);
        symptom_id_label.setMaxWidth(700);
        symptom_desc_label.setMaxWidth(700);
        reconfCommand_label.setMaxWidth(700);
        availableFunctions_label.setMaxWidth(700);
        fault_parameter_label.setMaxWidth(700);
        fault_value_label.setMaxWidth(700);
        fault_effect_label.setMaxWidth(700);
        fault_location_label.setMaxWidth(700);
        fault_message_label.setMaxWidth(700);
        check_status_label.setMaxWidth(700);
        equipment_id_label.setMaxWidth(700);

        ReconfVBox.getChildren().add(equipment_id_label);
        ReconfVBox.getChildren().add(fault_no_label);
        ReconfVBox.getChildren().add(fault_name_label);
        ReconfVBox.getChildren().add(fault_parameter_label);
        ReconfVBox.getChildren().add(fault_value_label);
        ReconfVBox.getChildren().add(fault_effect_label);
        ReconfVBox.getChildren().add(fault_location_label);
        ReconfVBox.getChildren().add(fault_message_label);
        ReconfVBox.getChildren().add(symptom_id_label);
        ReconfVBox.getChildren().add(symptom_desc_label);
        ReconfVBox.getChildren().add(check_status_label);
        ReconfVBox.getChildren().add(reconfCommand_label);
        ReconfVBox.getChildren().add(availableFunctions_label);
        ReconfPane.getChildren().add(ReconfVBox);
    }
}
