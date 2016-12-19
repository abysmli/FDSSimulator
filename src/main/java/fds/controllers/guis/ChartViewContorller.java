package fds.controllers.guis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.AnchorPane;

public class ChartViewContorller {

    int range;
    int upperBound;

    XYChart.Series<Number, Number> series;

    NumberAxis xAxis;

    public ChartViewContorller(AnchorPane pane, String title, String xAxisTitle, String yAxisTitle, int range, double LayoutX, double LayoutY, double Width, double Height) {
        this.range = range;
        upperBound = range;
        xAxis = new NumberAxis(xAxisTitle, 0, range, 1);
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yAxisTitle);

        series = new Series<>();
        series.setName(title);
        ObservableList<Series<Number, Number>> data = FXCollections.observableArrayList();
        data.add(series);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis, data);
        lineChart.setTitle(title);
        lineChart.setLegendVisible(false);
        lineChart.setLayoutX(LayoutX);
        lineChart.setLayoutY(LayoutY);
        lineChart.setMinWidth(Width);
        lineChart.setMinHeight(Height);
        lineChart.setMaxWidth(Width);
        lineChart.setMaxHeight(Height);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        series.nodeProperty().get().setStyle("-fx-stroke-width: 1px;");
        pane.getChildren().add(lineChart);
    }

    public void update(double value) {
        xAxis.setUpperBound(++upperBound);
        xAxis.setLowerBound(upperBound - range + 1);
        if (series.getData().size() >= range) {
            series.getData().remove(0);
        }
        series.getData().add(new Data<>(upperBound, value));
    }
}
