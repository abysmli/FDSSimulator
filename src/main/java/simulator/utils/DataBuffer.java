package simulator.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class DataBuffer {
    public static JSONArray data = new JSONArray();
    public static JSONArray initData = new JSONArray();
    public static JSONArray faultData = new JSONArray();
    public static JSONArray localFaultData = new JSONArray();
    public static JSONArray strategy = new JSONArray();
    public static List<Integer> deactivedFunction = new ArrayList<>();
    public static JSONArray task = new JSONArray();
}
