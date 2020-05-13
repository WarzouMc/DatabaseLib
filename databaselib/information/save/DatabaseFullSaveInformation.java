package databaselib.information.save;

import java.util.LinkedList;

public class DatabaseFullSaveInformation {

    private double timeTake;
    private LinkedList<DatabaseTableSaveInformation> databaseTableSaveInformation = new LinkedList<>();

    public void setTimeTake(long start, long end) {
        this.timeTake = (end - start) / 1000.0;
    }

    public void addTableSave(DatabaseTableSaveInformation dataBaseTableSaveInformation) {
        this.databaseTableSaveInformation.add(dataBaseTableSaveInformation);
    }

    public int getTotalDeletion() {
        return getDatabaseTableSaveInformation().stream().mapToInt(DatabaseTableSaveInformation::getDeletion).sum();
    }

    public int getTotalAddition() {
        return getDatabaseTableSaveInformation().stream().mapToInt(DatabaseTableSaveInformation::getAddition).sum();
    }

    public int getTotalModification() {
        return getDatabaseTableSaveInformation().stream().mapToInt(DatabaseTableSaveInformation::getModification).sum();
    }

    public double getTimeTake() {
        return this.timeTake;
    }

    public LinkedList<DatabaseTableSaveInformation> getDatabaseTableSaveInformation() {
        return this.databaseTableSaveInformation;
    }
}
