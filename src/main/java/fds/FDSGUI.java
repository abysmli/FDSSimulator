/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds;

import simulator.utils.DataBuffer;
import fds.controllers.FDSController;
import fds.controllers.guis.EmpiricalChartViewController;
import fds.controllers.guis.FaultTableViewController;
import fds.controllers.guis.ReconfigurationStrategyViewController;
import fds.controllers.guis.SensorDataViewController;
import fds.model.DatabaseHandler;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import org.json.JSONException;
import org.json.JSONObject;
import simulator.utils.ErrorLogger;

/**
 *
 * @author abysmli
 */
public class FDSGUI extends JApplet {

    private final JFrame rootFrame;
    private static final int JFXPANEL_WIDTH_INT = 1280;
    private static final int JFXPANEL_HEIGHT_INT = 800;
    private static JFXPanel fxContainer;

    final private AnchorPane SensorDataPane = new AnchorPane();
    final private GridPane chartGrid = new GridPane();
    final private AnchorPane ReconfPane = new AnchorPane();
    final private AnchorPane FaultTablePane = new AnchorPane();
    private TabPane tabPane;

    private Label statusLabel;
    private Label faultIDLabel;
    private Label faultTypeLabel;
    private Label responseLabel;
    private Button ExecuteStrategyButton;
    private Button ShowEmpiricalButton;
    private Button ShowRunningDataButton;
    private Button ShowFaultTableButton;
    private Button ResetButton;

    private SensorDataViewController SensorDataView;
    private ReconfigurationStrategyViewController ReconfigurationStrategyView;
    private FaultTableViewController FaultTableViewController;
    private EmpiricalChartViewController EmpiricalChartViewController;

    private FDSController FDMController;
    private DatabaseHandler databaseHandler;

    public FDSGUI(JFrame frame) {
        rootFrame = frame;
    }

    public void setFDMController(FDSController FDMController) {
        this.FDMController = FDMController;
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
            } catch (Exception e) {
                ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        });
    }

    private void createScene() throws SQLException, NamingException, Exception {
        /* splitpane setting */
        SensorDataView = new SensorDataViewController(SensorDataPane);
        ReconfigurationStrategyView = new ReconfigurationStrategyViewController(ReconfPane);
        FaultTableViewController = new FaultTableViewController(FaultTablePane);
        EmpiricalChartViewController = new EmpiricalChartViewController(chartGrid);

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

        SensorDataView.generateInfos(DataBuffer.initData);
        EmpiricalChartViewController.generateCharts(DataBuffer.initData);
        FaultTableViewController.generateFaultTable(databaseHandler.getFaultKnowledge());
    }

    private void createBanner(AnchorPane bannerPane) {
        ImageView LogoView = new ImageView();
        LogoView.setImage(new Image(FDSGUI.class.getResourceAsStream("/imgs/IAS-Logo.jpg")));
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

        AnchorPane controllerPane = new AnchorPane();
        controllerPane.setStyle("-fx-min-width: 280px; -fx-max-width: 280px; -fx-width: 280px; -fx-min-height: 700px; -fx-max-height: 700px; -fx-height: 700px;");
        controllerPane.setLayoutX(1000);
        createControllerPane(controllerPane);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(chartGrid);
        tabContainer.getChildren().add(tabPane);

        ScrollPane reconfScrollPane = new ScrollPane();
        reconfScrollPane.setContent(ReconfPane);

        SensorDataTab.setContent(SensorDataPane);
        EmpiricalChartTab.setContent(scrollPane);
        FaultTableTab.setContent(FaultTablePane);
        ReconfTab.setContent(reconfScrollPane);

        FaultTableViewController.initFaultTable();
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

    public void refresh(JSONArray components, int process_id) {
        SensorDataView.updateInfos(components, process_id);
        EmpiricalChartViewController.updateChart(components);
    }

    private void setSetStrategyButtonState(boolean state) {
        ExecuteStrategyButton.setDisable(!state);
    }

    private void selectTabPane(int index) {
        tabPane.getSelectionModel().select(index);
    }

    private void resetFDS() {
        try {
            databaseHandler.resetDatabase();
            showAlert("Reset Database", "ResetDatabase Successfully!", AlertType.INFORMATION);
        } catch (SQLException | NamingException e) {
            ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
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
                ReconfigurationStrategyView.generateReconfigurationStratgyView(result);
            } catch (JSONException e) {
                ErrorLogger.log(e, "Error Exception", e.getMessage(), ErrorLogger.ERROR_MESSAGE);
            }
        });
    }

    public void setFDSStatus(String status) {
        statusLabel.setText("Status: " + status);
    }

    public void showAlert(String Title, String Content, @NamedArg("alertType") AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(Title);
        alert.setHeaderText(null);
        alert.setContentText(Content);
        alert.showAndWait();
    }
}
