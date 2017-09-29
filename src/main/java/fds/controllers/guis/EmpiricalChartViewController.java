/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fds.controllers.guis;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author abysm
 */
public class EmpiricalChartViewController {

    private GridPane chartGrid;
    private List<ChartViewContorller> charts = new ArrayList<>();

    public EmpiricalChartViewController(GridPane chartGrid) {
        this.chartGrid = chartGrid;
    }

    public void updateChart(JSONArray allAbfComponents) {
        int chart_num = 0;
        for (int i = 0; i < allAbfComponents.length(); i++) {
            JSONObject Component = allAbfComponents.getJSONObject(i);
            if (Component.getString("type").equals("sensor")) {
                charts.get(chart_num++).update(Component.getDouble("value"));
            }
        }
    }

    public void generateCharts(JSONArray allAbfComponents) {
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
    }
}
