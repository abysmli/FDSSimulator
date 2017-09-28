package fds.model;

import org.json.JSONObject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Fault.
 *
 * @author Li, Yuan
 */
public class Fault_List {

    private final IntegerProperty faultId;
    private final IntegerProperty faultNr;
    private final StringProperty faultName;
    private final StringProperty faultParameter;
    private final StringProperty faultLocation;
    private final StringProperty symptomDesc;
    private final ObjectProperty<JSONObject> reconfCommand;
    private final StringProperty insertDate;

    /**
     * Default constructor.
     */
    public Fault_List() {
        this(0, 0, null, null, null, null, null, null);
    }

    /**
     * Constructor with some initial data.
     * 
     * @param faultId
     * @param faultNr
     */
    public Fault_List(int faultId, int faultNr, String faultName, String faultParameter, String faultLocation, String symptomDesc, JSONObject reconfCommand, String insertDate) {
        this.faultId = new SimpleIntegerProperty(faultId);
        this.faultNr = new SimpleIntegerProperty(faultNr);
        this.faultName = new SimpleStringProperty(faultName);
        this.faultParameter = new SimpleStringProperty(faultParameter);
        this.faultLocation = new SimpleStringProperty(faultLocation);
        this.symptomDesc = new SimpleStringProperty(symptomDesc);
        this.reconfCommand = new SimpleObjectProperty<JSONObject>(reconfCommand);
        this.insertDate = new SimpleStringProperty(insertDate);
    }

    public int getfaultId() {
        return faultId.get();
    }

    public void setfaultId(int faultId) {
        this.faultId.set(faultId);
    }

    public IntegerProperty faultIdProperty() {
        return faultId;
    }

    public int getfaultNr() {
        return faultNr.get();
    }

    public void setfaultNr(int faultNr) {
        this.faultNr.set(faultNr);
    }

    public IntegerProperty faultNrProperty() {
        return faultNr;
    }

    public String getfaultName() {
        return faultName.get();
    }

    public void setfaultName(String faultName) {
        this.faultName.set(faultName);
    }

    public StringProperty faultNameProperty() {
        return faultName;
    }
    
    public String getfaultfaultParameter() {
        return faultParameter.get();
    }

    public void setfaultfaultParameter(String faultfaultParameter) {
        this.faultParameter.set(faultfaultParameter);
    }

    public StringProperty faultfaultParameterProperty() {
        return faultParameter;
    }
    
    public String getfaultLocation() {
        return faultLocation.get();
    }

    public void setfaultLocation(String faultLocation) {
        this.faultLocation.set(faultLocation);
    }

    public StringProperty faultLocationProperty() {
        return faultLocation;
    }

    public String getsymptomDesc() {
        return symptomDesc.get();
    }

    public void setsymptomDesc(String symptomDesc) {
        this.symptomDesc.set(symptomDesc);
    }

    public StringProperty symptomDescProperty() {
        return symptomDesc;
    }

    public JSONObject getreconfCommand() {
        return reconfCommand.get();
    }

    public void setreconfCommand(JSONObject reconfCommand) {
        this.reconfCommand.set(reconfCommand);
    }

    public ObjectProperty<JSONObject> reconfCommandProperty() {
        return reconfCommand;
    }

    public String getinsertDate() {
        return insertDate.get();
    }

    public void setinsertDate(String insertDate) {
        this.insertDate.set(insertDate);
    }

    public StringProperty insertDateProperty() {
        return insertDate;
    }
}