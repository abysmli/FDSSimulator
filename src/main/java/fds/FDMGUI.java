/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds;

import simulator.utils.DataBuffer;
import fds.controllers.guis.ChartViewContorller;
import fds.controllers.FDMController;
import fds.model.DatabaseHandler;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javax.naming.NamingException;
import javax.swing.JApplet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysmli
 */
public class FDMGUI extends JApplet {

    private static final int JFXPANEL_WIDTH_INT = 1280;
    private static final int JFXPANEL_HEIGHT_INT = 800;
    private static JFXPanel fxContainer;

    final GridPane grid = new GridPane();
    final AnchorPane systeminfo_panel = new AnchorPane();
    boolean firstRunFlag = true;
    List<ChartViewContorller> charts = new ArrayList<>();

    List<Label> sensorLabelsContainer = new ArrayList<>();
    List<Label> sensorRateLabelsContainer = new ArrayList<>();
    List<Label> actorLabelsContainer = new ArrayList<>();
    List<Label> ventilLabelsContainer = new ArrayList<>();
    List<Label> schalterLabelsContainer = new ArrayList<>();
    List<Label> controllerLabelsContainer = new ArrayList<>();

    Label process;
    private FDMController FDMController;
    private DatabaseHandler databaseHandler;
    Button ExecuteButton;

    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(() -> {
            try {
                createScene();
            } catch (SQLException ex) {
                Logger.getLogger(FDMGUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(FDMGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void createScene() throws SQLException, NamingException {
        StackPane root = new StackPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(systeminfo_panel);
        root.getChildren().add(scrollPane);
        fxContainer.setScene(new Scene(root));
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setVgap(20);
        grid.setHgap(20);
        grid.setMinWidth(1000);
        AnchorPane.setTopAnchor(grid, 0.0);
        AnchorPane.setRightAnchor(grid, 0.0);
        AnchorPane.setLeftAnchor(grid, 0.0);
        AnchorPane.setBottomAnchor(grid, 0.0);
        systeminfo_panel.getChildren().add(grid);
        
        this.databaseHandler = new DatabaseHandler();
        if (DataBuffer.initData.length() == 0) {
            DataBuffer.initData = databaseHandler.getComponents();
        }
        generateInfos(DataBuffer.initData);
        generateCharts(DataBuffer.initData);

        ExecuteButton = new Button("Set Strategy");
        ExecuteButton.setDisable(true);
        GridPane.setConstraints(ExecuteButton, 8, 14);
        GridPane.setColumnSpan(ExecuteButton, 1);
        grid.getChildren().add(ExecuteButton);
        ExecuteButton.setOnAction((ActionEvent e) -> {
            FDMController.executeStrategy();
        });
    }

    public void refresh(JSONArray components, int process_id) {
        updateInfos(components, process_id);
        updateChart(components);
    }

    private void updateInfos(JSONArray allAbfComponents, int process_id) {
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

    private void generateCharts(JSONArray allAbfComponents) {
        int chart_num = 0;
        for (int i = 0; i < allAbfComponents.length(); i++) {
            JSONObject Component = allAbfComponents.getJSONObject(i);
            if (Component.getString("type").equals("sensor")) {
                final AnchorPane chartsPane = new AnchorPane();
                GridPane.setConstraints(chartsPane, chart_num % 2 * 4, 10 + chart_num / 2);
                GridPane.setColumnSpan(chartsPane, 4 + chart_num % 2);
                grid.getChildren().add(chartsPane);
                charts.add(new ChartViewContorller(chartsPane, Component.getString("component_name"), "time 1/s",
                        "Signal", 60, 0, 0, 530, 400));
                chart_num++;
            }
        }
        firstRunFlag = false;
    }

    private void generateInfos(JSONArray allAbfComponents) {
        final Label caption = new Label("Fault Diagnose GUI");
        caption.setStyle("-fx-font-weight: bold; -fx-font-family: Ubuntu; -fx-font-size: 20px");
        GridPane.setConstraints(caption, 0, 0);
        GridPane.setColumnSpan(caption, 4);
        grid.getChildren().add(caption);

        process = new Label("");
        process.setStyle("-fx-font-family: Ubuntu; -fx-font-size: 16px");
        GridPane.setConstraints(process, 4, 0);
        GridPane.setColumnSpan(process, 5);
        grid.getChildren().add(process);

        final Separator sepTitle = new Separator();
        sepTitle.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepTitle, 0, 1);
        GridPane.setColumnSpan(sepTitle, 9);
        grid.getChildren().add(sepTitle);

        final Label sensorNameLabel = new Label("Sensor Name");
        sensorNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(sensorNameLabel, 0, 2);
        grid.getChildren().add(sensorNameLabel);

        final Label sensorValueLabel = new Label("Current Value");
        sensorValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(sensorValueLabel, 1, 2);
        grid.getChildren().add(sensorValueLabel);

        final Label sensorRateLabel = new Label("Current Rate");
        sensorRateLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(sensorRateLabel, 2, 2);
        grid.getChildren().add(sensorRateLabel);

        final Separator sepMid1 = new Separator();
        sepMid1.setOrientation(Orientation.VERTICAL);
        sepMid1.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepMid1, 3, 2);
        GridPane.setRowSpan(sepMid1, 2);
        grid.getChildren().add(sepMid1);

        final Label actorNameLabel = new Label("Actor Name");
        actorNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(actorNameLabel, 4, 2);
        grid.getChildren().add(actorNameLabel);

        final Label actorValueLabel = new Label("Current Value");
        actorValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(actorValueLabel, 5, 2);
        grid.getChildren().add(actorValueLabel);

        final Separator sepMid11 = new Separator();
        sepMid11.setOrientation(Orientation.VERTICAL);
        sepMid11.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepMid11, 6, 2);
        GridPane.setRowSpan(sepMid11, 2);
        grid.getChildren().add(sepMid11);

        final Label ventilNameLabel = new Label("Ventil Name");
        ventilNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(ventilNameLabel, 7, 2);
        grid.getChildren().add(ventilNameLabel);

        final Label ventilValueLabel = new Label("Current Value");
        ventilValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(ventilValueLabel, 8, 2);
        grid.getChildren().add(ventilValueLabel);

        final AnchorPane sensorPane = new AnchorPane();
        GridPane.setConstraints(sensorPane, 0, 3);
        grid.getChildren().add(sensorPane);

        final AnchorPane sensorValuePane = new AnchorPane();
        GridPane.setConstraints(sensorValuePane, 1, 3);
        grid.getChildren().add(sensorValuePane);

        final AnchorPane sensorChangeRatePane = new AnchorPane();
        GridPane.setConstraints(sensorChangeRatePane, 2, 3);
        grid.getChildren().add(sensorChangeRatePane);

        final AnchorPane actorPane = new AnchorPane();
        GridPane.setConstraints(actorPane, 4, 3);
        grid.getChildren().add(actorPane);

        final AnchorPane actorValuePane = new AnchorPane();
        GridPane.setConstraints(actorValuePane, 5, 3);
        grid.getChildren().add(actorValuePane);

        final AnchorPane ventilPane = new AnchorPane();
        GridPane.setConstraints(ventilPane, 7, 3);
        grid.getChildren().add(ventilPane);

        final AnchorPane ventilValuePane = new AnchorPane();
        GridPane.setConstraints(ventilValuePane, 8, 3);
        grid.getChildren().add(ventilValuePane);

        final Separator sepRow1 = new Separator();
        sepRow1.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepRow1, 0, 4);
        GridPane.setColumnSpan(sepRow1, 9);
        grid.getChildren().add(sepRow1);

        final Label schalterNameLabel = new Label("Schalter Name");
        schalterNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(schalterNameLabel, 0, 5);
        grid.getChildren().add(schalterNameLabel);

        final Label schalterValueLabel = new Label("Current Value");
        schalterValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(schalterValueLabel, 1, 5);
        grid.getChildren().add(schalterValueLabel);

        final Separator sepMid2 = new Separator();
        sepMid2.setOrientation(Orientation.VERTICAL);
        sepMid2.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepMid2, 3, 5);
        GridPane.setRowSpan(sepMid2, 2);
        grid.getChildren().add(sepMid2);

        final Label controllerNameLabel = new Label("Micro Controller Name");
        controllerNameLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(controllerNameLabel, 4, 5);
        grid.getChildren().add(controllerNameLabel);

        final Label controllerValueLabel = new Label("Current Value");
        controllerValueLabel.setStyle("-fx-font-weight: bold");
        GridPane.setConstraints(controllerValueLabel, 5, 5);
        grid.getChildren().add(controllerValueLabel);

        final AnchorPane schalterPane = new AnchorPane();
        GridPane.setConstraints(schalterPane, 0, 6);
        grid.getChildren().add(schalterPane);

        final AnchorPane schalterValuePane = new AnchorPane();
        GridPane.setConstraints(schalterValuePane, 1, 6);
        grid.getChildren().add(schalterValuePane);

        final AnchorPane controllerPane = new AnchorPane();
        GridPane.setConstraints(controllerPane, 4, 6);
        grid.getChildren().add(controllerPane);

        final AnchorPane controllerValuePane = new AnchorPane();
        GridPane.setConstraints(controllerValuePane, 5, 6);
        grid.getChildren().add(controllerValuePane);

        final Separator sepRow2 = new Separator();
        sepRow2.setValignment(VPos.CENTER);
        GridPane.setConstraints(sepRow2, 0, 7);
        GridPane.setColumnSpan(sepRow2, 9);
        grid.getChildren().add(sepRow2);

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

    private void updateChart(JSONArray allAbfComponents) {
        int chart_num = 0;
        for (int i = 0; i < allAbfComponents.length(); i++) {
            JSONObject Component = allAbfComponents.getJSONObject(i);
            if (Component.getString("type").equals("sensor")) {
                charts.get(chart_num++).update(Component.getDouble("value"));
            }
        }
    }

    private void displayProcess(int process_id) {
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

    public void setFDMController(FDMController FDMController) {
        this.FDMController = FDMController;
    }

    public void setSetStrategyButtonState(boolean state) {
        ExecuteButton.setDisable(!state);
    }

}
