package databaselib.information.save;

import databaselib.tables.AbstractDatabaseTables;

public class DatabaseTableSaveInformation {

    private int deletion, addition, modification;
    private double timeTake;

    private AbstractDatabaseTables abstractDatabaseTables;
    public DatabaseTableSaveInformation(AbstractDatabaseTables abstractDatabaseTables) {
        this.abstractDatabaseTables = abstractDatabaseTables;
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

    public AbstractDatabaseTables getAbstractDatabaseTables() {
        return this.abstractDatabaseTables;
    }
}
