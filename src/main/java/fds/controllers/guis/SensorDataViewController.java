/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.controllers.guis;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysm
 */
public class SensorDataViewController {

    private GridPane SensorDataGrid = new GridPane();
    private Label process;
    private AnchorPane SensorDataPane;
    
    List<Label> sensorLabelsContainer = new ArrayList<>();
    List<Label> sensorRateLabelsContainer = new ArrayList<>();
    List<Label> actorLabelsContainer = new ArrayList<>();
    List<Label> ventilLabelsContainer = new ArrayList<>();
    List<Label> schalterLabelsContainer = new ArrayList<>();
    List<Label> controllerLabelsContainer = new ArrayList<>();

    public SensorDataViewController(AnchorPane SensorDataPane) {
        this.SensorDataPane = SensorDataPane;
    }

    public void generateInfos(JSONArray allAbfComponents) {
        SensorDataGrid.setPadding(new Insets(30, 30, 30, 30));
        SensorDataGrid.setVgap(20);
        SensorDataGrid.setHgap(20);
        SensorDataGrid.setMinWidth(1000);
        AnchorPane.setTopAnchor(SensorDataGrid, 0.0);
        AnchorPane.setRightAnchor(SensorDataGrid, 0.0);
        AnchorPane.setLeftAnchor(SensorDataGrid, 0.0);
        AnchorPane.setBottomAnchor(SensorDataGrid, 0.0);
        SensorDataPane.getChildren().add(SensorDataGrid);
        
        
        final Label caption = new Label("Fault Diagnose GUI");
        caption.setStyle("-fx-font-weight: bold; -fx-font-family: Ubuntu; -fx-font-size: 20px");
        GridPane.setConstraints(caption, 0, 0);
        GridPane.setColumnSpan(caption, 4);
        SensorDataGrid.getChildren().add(caption);

        process = new Label("");
        process.setStyle("-fx-font-family: Ubuntu; -fx-font-size: 16px");
        GridPane.setConstraints(process, 4, 0);
        GridPane.setColumnSpan(process, 5);
        SensorDataGrid.getChildren().add(process);

        final Separator sepTitle = new Separator();
        sepTitle.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepTitle, 0, 1);
        GridPane.setColumnSpan(sepTitle, 9);
        SensorDataGrid.getChildren().add(sepTitle);

        final Label sensorNameLabel = new Label("Sensor Name");
        sensorNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(sensorNameLabel, 0, 2);
        SensorDataGrid.getChildren().add(sensorNameLabel);

        final Label sensorValueLabel = new Label("Current Value");
        sensorValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(sensorValueLabel, 1, 2);
        SensorDataGrid.getChildren().add(sensorValueLabel);

        final Label sensorRateLabel = new Label("Current Rate");
        sensorRateLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(sensorRateLabel, 2, 2);
        SensorDataGrid.getChildren().add(sensorRateLabel);

        final Separator sepMid1 = new Separator();
        sepMid1.setOrientation(Orientation.VERTICAL);
        sepMid1.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepMid1, 3, 2);
        GridPane.setRowSpan(sepMid1, 2);
        SensorDataGrid.getChildren().add(sepMid1);

        final Label actorNameLabel = new Label("Actor Name");
        actorNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(actorNameLabel, 4, 2);
        SensorDataGrid.getChildren().add(actorNameLabel);

        final Label actorValueLabel = new Label("Current Value");
        actorValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(actorValueLabel, 5, 2);
        SensorDataGrid.getChildren().add(actorValueLabel);

        final Separator sepMid11 = new Separator();
        sepMid11.setOrientation(Orientation.VERTICAL);
        sepMid11.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepMid11, 6, 2);
        GridPane.setRowSpan(sepMid11, 2);
        SensorDataGrid.getChildren().add(sepMid11);

        final Label ventilNameLabel = new Label("Ventil Name");
        ventilNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(ventilNameLabel, 7, 2);
        SensorDataGrid.getChildren().add(ventilNameLabel);

        final Label ventilValueLabel = new Label("Current Value");
        ventilValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(ventilValueLabel, 8, 2);
        SensorDataGrid.getChildren().add(ventilValueLabel);

        final AnchorPane sensorPane = new AnchorPane();
        GridPane.setConstraints(sensorPane, 0, 3);
        SensorDataGrid.getChildren().add(sensorPane);

        final AnchorPane sensorValuePane = new AnchorPane();
        GridPane.setConstraints(sensorValuePane, 1, 3);
        SensorDataGrid.getChildren().add(sensorValuePane);

        final AnchorPane sensorChangeRatePane = new AnchorPane();
        GridPane.setConstraints(sensorChangeRatePane, 2, 3);
        SensorDataGrid.getChildren().add(sensorChangeRatePane);

        final AnchorPane actorPane = new AnchorPane();
        GridPane.setConstraints(actorPane, 4, 3);
        SensorDataGrid.getChildren().add(actorPane);

        final AnchorPane actorValuePane = new AnchorPane();
        GridPane.setConstraints(actorValuePane, 5, 3);
        SensorDataGrid.getChildren().add(actorValuePane);

        final AnchorPane ventilPane = new AnchorPane();
        GridPane.setConstraints(ventilPane, 7, 3);
        SensorDataGrid.getChildren().add(ventilPane);

        final AnchorPane ventilValuePane = new AnchorPane();
        GridPane.setConstraints(ventilValuePane, 8, 3);
        SensorDataGrid.getChildren().add(ventilValuePane);

        final Separator sepRow1 = new Separator();
        sepRow1.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepRow1, 0, 4);
        GridPane.setColumnSpan(sepRow1, 9);
        SensorDataGrid.getChildren().add(sepRow1);

        final Label schalterNameLabel = new Label("Schalter Name");
        schalterNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(schalterNameLabel, 0, 5);
        SensorDataGrid.getChildren().add(schalterNameLabel);

        final Label schalterValueLabel = new Label("Current Value");
        schalterValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(schalterValueLabel, 1, 5);
        SensorDataGrid.getChildren().add(schalterValueLabel);

        final Separator sepMid2 = new Separator();
        sepMid2.setOrientation(Orientation.VERTICAL);
        sepMid2.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepMid2, 3, 5);
        GridPane.setRowSpan(sepMid2, 2);
        SensorDataGrid.getChildren().add(sepMid2);

        final Label controllerNameLabel = new Label("Micro Controller Name");
        controllerNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(controllerNameLabel, 4, 5);
        SensorDataGrid.getChildren().add(controllerNameLabel);

        final Label controllerValueLabel = new Label("Current Value");
        controllerValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(controllerValueLabel, 5, 5);
        SensorDataGrid.getChildren().add(controllerValueLabel);

        final AnchorPane schalterPane = new AnchorPane();
        GridPane.setConstraints(schalterPane, 0, 6);
        SensorDataGrid.getChildren().add(schalterPane);

        final AnchorPane schalterValuePane = new AnchorPane();
        GridPane.setConstraints(schalterValuePane, 1, 6);
        SensorDataGrid.getChildren().add(schalterValuePane);

        final AnchorPane controllerPane = new AnchorPane();
        GridPane.setConstraints(controllerPane, 4, 6);
        SensorDataGrid.getChildren().add(controllerPane);

        final AnchorPane controllerValuePane = new AnchorPane();
        GridPane.setConstraints(controllerValuePane, 5, 6);
        SensorDataGrid.getChildren().add(controllerValuePane);

        final Separator sepRow2 = new Separator();
        sepRow2.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepRow2, 0, 7);
        GridPane.setColumnSpan(sepRow2, 9);
        SensorDataGrid.getChildren().add(sepRow2);

        int sensorCount = 0, actorCount = 0, ventilCount = 0, schalterCount = 0, MCCount = 0;
        for (int i = 0; i < allAbfComponents.length(); i++) {
            JSONObject component = allAbfComponents.getJSONObject(i);
            switch (component.getString("type")) {
                case "sensor": {
                    Label componentLabel = new Label();
                    componentLabel.setText(component.getString("component_name") + ": ");
                    componentLabel.setLayoutX(0);
                    componentLabel.setLayoutY(sensorCount * 20);
                    Label componentValueLabel = new Label();
                    componentValueLabel.setText("0.0");
                    componentValueLabel.setLayoutX(0);
                    componentValueLabel.setLayoutY(sensorCount * 20);
                    sensorLabelsContainer.add(componentValueLabel);
                    Label componentChangeRageLabel = new Label();
                    componentChangeRageLabel.setText("0.0");
                    componentChangeRageLabel.setLayoutX(0);
                    componentChangeRageLabel.setLayoutY(sensorCount * 20);
                    sensorRateLabelsContainer.add(componentChangeRageLabel);
                    sensorPane.getChildren().add(componentLabel);
                    sensorValuePane.getChildren().add(componentValueLabel);
                    sensorChangeRatePane.getChildren().add(componentChangeRageLabel);
                    sensorCount++;
                    break;
                }
                case "actor": {
                    Label componentLabel = new Label();
                    componentLabel.setText(component.getString("component_name") + ": ");
                    componentLabel.setLayoutX(0);
                    componentLabel.setLayoutY(actorCount * 20);
                    Label componentValueLabel = new Label();
                    componentValueLabel.setText("-");
                    componentValueLabel.setLayoutX(0);
                    componentValueLabel.setLayoutY(actorCount * 20);
                    actorLabelsContainer.add(componentValueLabel);
                    actorPane.getChildren().add(componentLabel);
                    actorValuePane.getChildren().add(componentValueLabel);
                    actorCount++;
                    break;
                }
                case "ventil": {
                    Label componentLabel = new Label();
                    componentLabel.setText(component.getString("component_name") + ": ");
                    componentLabel.setLayoutX(0);
                    componentLabel.setLayoutY(ventilCount * 20);
                    Label componentValueLabel = new Label();
                    componentValueLabel.setText("-");
                    componentValueLabel.setLayoutX(0);
                    componentValueLabel.setLayoutY(ventilCount * 20);
                    ventilLabelsContainer.add(componentValueLabel);
                    ventilPane.getChildren().add(componentLabel);
                    ventilValuePane.getChildren().add(componentValueLabel);
                    ventilCount++;
                    break;
                }
                case "schalter": {
                    Label componentLabel = new Label();
                    componentLabel.setText(component.getString("component_name") + ": ");
                    componentLabel.setLayoutX(0);
                    componentLabel.setLayoutY(schalterCount * 20);
                    Label componentValueLabel = new Label();
                    componentValueLabel.setText("-");
                    componentValueLabel.setLayoutX(0);
                    componentValueLabel.setLayoutY(schalterCount * 20);
                    schalterLabelsContainer.add(componentValueLabel);
                    schalterPane.getChildren().add(componentLabel);
                    schalterValuePane.getChildren().add(componentValueLabel);
                    schalterCount++;
                    break;
                }
                case "MC": {
                    Label componentLabel = new Label();
                    componentLabel.setText(component.getString("component_name") + ": ");
                    componentLabel.setLayoutX(0);
                    componentLabel.setLayoutY(MCCount * 20);
                    Label componentValueLabel = new Label();
                    componentValueLabel.setText("-");
                    componentValueLabel.setLayoutX(0);
                    componentValueLabel.setLayoutY(MCCount * 20);
                    controllerLabelsContainer.add(componentValueLabel);
                    controllerPane.getChildren().add(componentLabel);
                    controllerValuePane.getChildren().add(componentValueLabel);
                    MCCount++;
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void updateInfos(JSONArray allAbfComponents, int process_id) {
        displayProcess(process_id);
        int sensorCount = 0, actorCount = 0, ventilCount = 0, schalterCount = 0, MCCount = 0;
        for (int i = 0; i < allAbfComponents.length(); i++) {
            JSONObject component = allAbfComponents.getJSONObject(i);
            if (component.getString("type").equals("sensor")) {
                sensorLabelsContainer.get(sensorCount).setText(String.format("%.2f", component.getDouble("value")));
                sensorRateLabelsContainer.get(sensorCount)
                        .setText(String.format("%.2f", component.getDouble("change_rate")));
                sensorCount++;
            }
            if (component.getString("type").equals("actor")) {
                actorLabelsContainer.get(actorCount).setText(component.getString("value"));
                actorCount++;
            }
            if (component.getString("type").equals("ventil")) {
                ventilLabelsContainer.get(ventilCount).setText(component.getString("value"));
                ventilCount++;
            }
            if (component.getString("type").equals("schalter")) {
                schalterLabelsContainer.get(schalterCount).setText(component.getString("value"));
                schalterCount++;
            }
            if (component.getString("type").equals("MC")) {
                controllerLabelsContainer.get(MCCount).setText(component.getString("value"));
                MCCount++;
            }
        }
    }

    public void displayProcess(int process_id) {
        switch (process_id) {
            case 1:
                process.setText("Current Process: Air Pumping");
                break;
            case 2:
                process.setText("Current Process: Filling");
                break;
            case 3:
                process.setText("Current Process: Heating");
                break;
            case 4:
                process.setText("Current Process: Pumping");
                break;
            default:
                process.setText("Current Process: Stop");
                break;
        }
    }
}
