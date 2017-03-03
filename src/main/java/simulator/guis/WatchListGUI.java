package simulator.guis;

import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.json.JSONObject;

import simulator.utils.DataBuffer;

public class WatchListGUI {

    JFrame WatchListGUI = new JFrame("Watch List");
    List<JLabel> valueLabels = new ArrayList<>();
    List<JCheckBox> defektCheckboxs = new ArrayList<>();

    public WatchListGUI() {
        WatchListGUI.setBounds(0, 0, 1200, 700);
        WatchListGUI.setResizable(false);
        JPanel pane = new JPanel();
        pane.setBounds(14, 14, 1200, 690);
        pane.setLayout(new GridLayout(0, 5));
        JLabel ComponentIDLabel = new JLabel("Component ID", SwingConstants.CENTER);
        JLabel ComponentNameLabel = new JLabel("Component Name");
        JLabel SeriesLabel = new JLabel("Series");
        JLabel ValueLabel = new JLabel("Value");
        JLabel DefektLabel = new JLabel("Defekt");
        pane.add(ComponentIDLabel);
        pane.add(ComponentNameLabel);
        pane.add(SeriesLabel);
        pane.add(ValueLabel);
        pane.add(DefektLabel);
        for (int i = 0; i < DataBuffer.initData.length(); i++) {
            JSONObject obj = DataBuffer.initData.getJSONObject(i);
            JLabel componentID = new JLabel(String.valueOf(obj.getInt("component_id")), SwingConstants.CENTER);
            componentID.setFont(new Font("Ubuntu", 0, 14));
            JLabel componentName = new JLabel(obj.getString("component_name"));
            componentName.setFont(new Font("Ubuntu", 0, 14));
            JLabel series = new JLabel(obj.getString("series"));
            series.setFont(new Font("Ubuntu", 0, 14));
            JLabel value = new JLabel("null");
            value.setFont(new Font("Ubuntu", 0, 14));
            JCheckBox defekt = new JCheckBox();
            defekt.setEnabled(false);
            pane.add(componentID);
            pane.add(componentName);
            pane.add(series);
            pane.add(value);
            pane.add(defekt);
            valueLabels.add(value);
            defektCheckboxs.add(defekt);
        }
        WatchListGUI.add(pane);
    }

    public void refresh() {
        for (int i = 0; i < DataBuffer.data.length(); i++) {
            JSONObject obj = DataBuffer.data.getJSONObject(i);
            if (obj.getString("type").equals("sensor")) {
                valueLabels.get(i)
                        .setText((new BigDecimal(Double.valueOf(obj.getString("value")))
                                .setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()) + " | (change rate: "
                                + (new BigDecimal(Double.valueOf(obj.getString("change_rate")))
                                .setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue()) + ")");
            } else {
                valueLabels.get(i).setText(obj.getString("value"));
            }
        }
    }

    public void resetDefektComponent() {
        defektCheckboxs.forEach(cb -> {
            cb.setSelected(false);
        });
    }

    public void setDefektComponent(int component_id, boolean defektFlag) {
        defektCheckboxs.get(component_id - 1).setSelected(defektFlag);
    }

    public void setVisible(boolean b) {
        WatchListGUI.setVisible(b);
    }

    public boolean isVisible() {
        return WatchListGUI.isVisible();
    }
}
