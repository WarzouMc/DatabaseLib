package databaselib.information.save;

import java.util.LinkedList;

public class DataBaseFullSaveInformation {

    private double timeTake;
    private LinkedList<DataBaseTableSaveInformation> dataBaseTableSaveInformation = new LinkedList<>();

    public void setTimeTake(long start, long end) {
        this.timeTake = (end - start) / 1000.0;
    }

    public void addTableSave(DataBaseTableSaveInformation dataBaseTableSaveInformation) {
        this.dataBaseTableSaveInformation.add(dataBaseTableSaveInformation);
    }

    public int getTotalDeletion() {
        return getDataBaseTableSaveInformation().stream().mapToInt(DataBaseTableSaveInformation::getDeletion).sum();
    }

    public int getTotalAddition() {
        return getDataBaseTableSaveInformation().stream().mapToInt(DataBaseTableSaveInformation::getAddition).sum();
    }

    public int getTotalModification() {
        return getDataBaseTableSaveInformation().stream().mapToInt(DataBaseTableSaveInformation::getModification).sum();
    }

    public double getTimeTake() {
        return this.timeTake;
    }

    public LinkedList<DataBaseTableSaveInformation> getDataBaseTableSaveInformation() {
        return this.dataBaseTableSaveInformation;
    }
}
