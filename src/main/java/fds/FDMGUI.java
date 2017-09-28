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
import fds.model.Fault_List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.naming.NamingException;
import javax.swing.JApplet;
import javax.swing.JFrame;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.utils.ErrorLogger;

/**
 *
 * @author abysmli
 */
public class FDMGUI extends JApplet {

    JFrame rootFrame;
    private static final int JFXPANEL_WIDTH_INT = 1280;
    private static final int JFXPANEL_HEIGHT_INT = 800;
    private static JFXPanel fxContainer;

    final GridPane SensorDataGrid = new GridPane();
    final GridPane chartGrid = new GridPane();

    TabPane tabPane;

    Label statusLabel;
    Label faultIDLabel;
    Label faultTypeLabel;
    Label responseLabel;
    Button ExecuteStrategyButton;
    Button ShowEmpiricalButton;
    Button ShowRunningDataButton;
    Button ShowFaultTableButton;
    Button ResetButton;
    AnchorPane ReconfPane;

    boolean firstRunFlag = true;

    List<ChartViewContorller> charts = new ArrayList<>();
    List<Label> sensorLabelsContainer = new ArrayList<>();
    List<Label> sensorRateLabelsContainer = new ArrayList<>();
    List<Label> actorLabelsContainer = new ArrayList<>();
    List<Label> ventilLabelsContainer = new ArrayList<>();
    List<Label> schalterLabelsContainer = new ArrayList<>();
    List<Label> controllerLabelsContainer = new ArrayList<>();

    /* ----------------- faultTable -------------------- */
    private TableView<Fault_List> faultTable;
    private TableColumn<Fault_List, Integer> faultIdColumn, faultNrColumn;
    private TableColumn<Fault_List, String> faultNameColumn, faultParameterColumn, faultLocationColumn, symptomDescColumn, insertDateColumn;
    private TableColumn<Fault_List, Object> reconfCommandColumn;
    private final ObservableList<Fault_List> faultData = FXCollections.observableArrayList();

    Label process;
    private FDMController FDMController;
    private DatabaseHandler databaseHandler;

    public FDMGUI(JFrame frame) {
        rootFrame = frame;
    }

    @Override
    public void init() {
        fxContainer = new JFXPanel();
        fxContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        add(fxContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(() -> {
            try {
                createScene();
            } catch (Exception ex) {
                Logger.getLogger(FDMGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void createScene() throws SQLException, NamingException, Exception {
        /* splitpane setting */
        StackPane root = new StackPane();
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        AnchorPane bannerPane = new AnchorPane();
        AnchorPane contentPane = new AnchorPane();
        createBanner(bannerPane);
        createContentPane(contentPane);
        splitPane.getItems().addAll(bannerPane, contentPane);
        root.getChildren().add(splitPane);

        fxContainer.setScene(new Scene(root));

        this.databaseHandler = new DatabaseHandler();
        if (DataBuffer.initData.length() == 0) {
            DataBuffer.initData = databaseHandler.getComponents();
        }
        generateInfos(DataBuffer.initData);
        generateCharts(DataBuffer.initData);
        generateFaultTable(databaseHandler.getFaultKnowledge());

    }

    public void refresh(JSONArray components, int process_id) {
        updateInfos(components, process_id);
        updateChart(components);
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
                chartGrid.getChildren().add(chartsPane);
                charts.add(new ChartViewContorller(chartsPane, Component.getString("component_name"), "time 1/s",
                        "Signal", 60, 0, 0, 450, 300));
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

    private void generateFaultTable(JSONArray mFaults) throws Exception {
        for (int i = 0; i < mFaults.length(); i++) {
            JSONObject obj = mFaults.getJSONObject(i);
            if (i >= faultData.size()) {
                faultData.add(new Fault_List(obj.getInt("fault_id"), obj.getInt("fault_no"),
                        obj.getString("fault_name"), obj.getString("fault_parameter"), String.valueOf(obj.getString("fault_location")), obj.getString("symptom_desc"), new JSONObject(obj.getString("reconf_command")),
                        ""));
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
        ExecuteStrategyButton.setDisable(!state);
    }

    private void createBanner(AnchorPane bannerPane) {
        ImageView LogoView = new ImageView();
        LogoView.setImage(new Image(FDMGUI.class.getResourceAsStream("/imgs/IAS-Logo.jpg")));
        LogoView.setFitWidth(152.0);
        LogoView.setFitHeight(83.0);
        LogoView.setLayoutX(15.0);
        LogoView.setLayoutY(7.0);
        Label Title = new Label("Fault Diagnosis Module for Two-Tank System Simulator");
        Title.setLayoutX(206.0);
        Title.setLayoutY(37.0);
        Title.setStyle("-fx-font-size: 30.0px; -fx-font-family: \"Segoe UI\"");
        bannerPane.getChildren().addAll(LogoView, Title);
        bannerPane.setStyle("-fx-background-color:#ffffff ;-fx-min-height: 100px; -fx-max-height:100px; -fx-height:100px;");
    }

    private void createContentPane(AnchorPane contentPane) {
        AnchorPane tabContainer = new AnchorPane();
        tabContainer.setStyle("-fx-min-width: 1000px; -fx-max-width: 1000px; -fx-width: 1000px; -fx-min-height: 700px; -fx-max-height: 700px; -fx-height: 700px;");
        AnchorPane controllerPane = new AnchorPane();
        controllerPane.setStyle("-fx-min-width: 280px; -fx-max-width: 280px; -fx-width: 280px; -fx-min-height: 700px; -fx-max-height: 700px; -fx-height: 700px;");
        controllerPane.setLayoutX(1000);

        createControllerPane(controllerPane);
        tabPane = new TabPane();
        tabPane.prefHeightProperty().bind(tabContainer.heightProperty());
        tabPane.prefWidthProperty().bind(tabContainer.widthProperty());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab EmpiricalChartTab = new Tab();
        Tab SensorDataTab = new Tab();
        Tab FaultTableTab = new Tab();
        Tab ReconfTab = new Tab();
        EmpiricalChartTab.setText("Empirical Chart");
        SensorDataTab.setText("Sensor Data");
        FaultTableTab.setText("Fault Table");
        ReconfTab.setText("Response from DHFRS");
        tabPane.getTabs().addAll(EmpiricalChartTab, SensorDataTab, FaultTableTab, ReconfTab);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(chartGrid);
        tabContainer.getChildren().add(tabPane);

        AnchorPane SensorDataPane = new AnchorPane();
        SensorDataGrid.setPadding(new Insets(30, 30, 30, 30));
        SensorDataGrid.setVgap(20);
        SensorDataGrid.setHgap(20);
        SensorDataGrid.setMinWidth(1000);
        AnchorPane.setTopAnchor(SensorDataGrid, 0.0);
        AnchorPane.setRightAnchor(SensorDataGrid, 0.0);
        AnchorPane.setLeftAnchor(SensorDataGrid, 0.0);
        AnchorPane.setBottomAnchor(SensorDataGrid, 0.0);
        SensorDataPane.getChildren().add(SensorDataGrid);

        faultTable = new TableView<>();
        
        ScrollPane reconfScrollPane = new ScrollPane();
        ReconfPane = new AnchorPane();
        reconfScrollPane.setContent(ReconfPane);

        SensorDataTab.setContent(SensorDataPane);
        EmpiricalChartTab.setContent(scrollPane);
        FaultTableTab.setContent(faultTable);
        ReconfTab.setContent(reconfScrollPane);

        initFaultTable();
        contentPane.getChildren().addAll(tabContainer, controllerPane);
    }

    private void createControllerPane(AnchorPane controllerPane) {
        VBox vbox = new VBox();
        statusLabel = new Label("Status: Listening");
        statusLabel.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-alignment:center-left; -fx-font: 14 \"Segoe UI\";");
        faultIDLabel = new Label("Fault ID: ");
        faultIDLabel.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-alignment:center-left; -fx-font: 14 \"Segoe UI\";");
        faultTypeLabel = new Label("Fault Type: ");
        faultTypeLabel.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-alignment:center-left; -fx-font: 14 \"Segoe UI\";");
        responseLabel = new Label("Reconfiguration Response: ");
        responseLabel.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-alignment:center-left; -fx-font: 14 \"Segoe UI\";");

        ExecuteStrategyButton = new Button("Execute Strategy");
        ExecuteStrategyButton.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-font: 14 \"Segoe UI\"");
        ExecuteStrategyButton.setDisable(true);
        ExecuteStrategyButton.setOnAction((ActionEvent e) -> {
            FDMController.executeStrategy();
        });

        ShowEmpiricalButton = new Button("Empirical Chart");
        ShowEmpiricalButton.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-font: 14 \"Segoe UI\"");
        ShowEmpiricalButton.setOnAction((ActionEvent e) -> {
            selectTabPane(0);
        });
        ShowRunningDataButton = new Button("Running Data");
        ShowRunningDataButton.setStyle("-fx-min-width:200px; -fx-min-height: 40px;");
        ShowRunningDataButton.setOnAction((ActionEvent e) -> {
            selectTabPane(1);
        });

        ShowFaultTableButton = new Button("Fault Table");
        ShowFaultTableButton.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-font: 14 \"Segoe UI\"");
        ShowFaultTableButton.setOnAction((ActionEvent e) -> {
            selectTabPane(2);
        });

        ResetButton = new Button("Reset FDS");
        ResetButton.setStyle("-fx-min-width:200px; -fx-min-height: 40px; -fx-font: 14 \"Segoe UI\"");
        ResetButton.setOnAction((ActionEvent e) -> {
            resetFDS();
        });
        vbox.setLayoutY(20);
        vbox.setStyle("-fx-alignment: center; -fx-min-width: 280px; -fx-spacing: 30px; ");
        vbox.getChildren().addAll(statusLabel, faultIDLabel, faultTypeLabel, responseLabel, ExecuteStrategyButton, ShowEmpiricalButton, ShowRunningDataButton, ShowFaultTableButton, ResetButton);
        controllerPane.getChildren().add(vbox);
    }

    private void initFaultTable() {
        faultTable.setEditable(false);
        faultIdColumn = new TableColumn<>();
        faultNrColumn = new TableColumn<>();
        faultNameColumn = new TableColumn<>();
        faultParameterColumn = new TableColumn<>();
        faultLocationColumn = new TableColumn<>();
        symptomDescColumn = new TableColumn<>();
        reconfCommandColumn = new TableColumn<>();
        insertDateColumn = new TableColumn<>();
        faultIdColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, Integer>("faultId"));
        faultNrColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, Integer>("faultNr"));
        faultNameColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, String>("faultName"));
        faultParameterColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, String>("faultParameter"));
        faultLocationColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, String>("faultLocation"));
        symptomDescColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, String>("symptomDesc"));
        reconfCommandColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, Object>("reconfCommand"));
        insertDateColumn.setCellValueFactory(new PropertyValueFactory<Fault_List, String>("insertDate"));
        faultTable.setItems(faultData);
    }

    public void selectTabPane(int index) {
        tabPane.getSelectionModel().select(index);
    }

    public void resetFDS() {
        try {
            databaseHandler.resetDatabase();
        } catch (Exception e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
            Logger.getLogger(FDMGUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void setReconfigurationStrategy(JSONObject result) {
        Platform.runLater(() -> {
            try {
                rootFrame.toFront();
                setSetStrategyButtonState(true);
                selectTabPane(3);
                setFDSStatus("Stop");
                /* setting labels */
                faultIDLabel.setText("Fault ID: " + result.getInt("fault_no"));
                faultTypeLabel.setText("Fault Type: " + result.getString("symptom_desc"));
                responseLabel.setText("Reconfiguration Response: DHFRS");

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

            } catch (Exception ex) {
                Logger.getLogger(FDMGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public void setFDSStatus(String status) {
        statusLabel.setText("Status: " + status);
    }

}
