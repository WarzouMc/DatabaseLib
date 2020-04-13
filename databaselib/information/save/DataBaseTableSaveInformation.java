package databaselib.information.save;

import databaselib.tables.DataBaseTable;

public class DataBaseTableSaveInformation {

    private int deletion, addition, modification;
    private double timeTake;

    private DataBaseTable dataBaseTable;
    public DataBaseTableSaveInformation(DataBaseTable dataBaseTable) {
        this.dataBaseTable = dataBaseTable;
    }

    public void setDeletion(int deletion) {
        this.deletion = deletion;
    }

    public void setAddition(int addition) {
        this.addition = addition;
    }

    public void setModification(int modification) {
        this.modification = modification;
    }

    public void setTimeTake(long start, long end) {
        this.timeTake = (end - start) / 1000.0;
    }

    public int getDeletion() {
        return this.deletion;
    }

    public int getAddition() {
        return this.addition;
    }

    public int getModification() {
        return this.modification;
    }

    public double getTimeTake() {
        return this.timeTake;
    }

    public DataBaseTable getDataBaseTable() {
        return this.dataBaseTable;
    }
}
