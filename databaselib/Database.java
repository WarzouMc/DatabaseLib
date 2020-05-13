package databaselib;

import databaselib.information.DatabaseColumnValues;
import databaselib.information.DatabaseInformationLoader;
import databaselib.information.save.DatabaseTableSaveInformation;
import databaselib.tables.AbstractDatabaseTables;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Database {

    private DatabaseInformationLoader databaseInformationLoader;

    private String host;
    private String user;
    private String password;
    private String name;

    private AbstractDatabaseTables abstractDatabaseTables;
    public Database(AbstractDatabaseTables abstractDatabaseTables) {
        this.abstractDatabaseTables = abstractDatabaseTables;
        this.host = abstractDatabaseTables.getHost();
        this.user = abstractDatabaseTables.getUser();
        this.password = abstractDatabaseTables.getPassword();
        this.name = abstractDatabaseTables.getName();
    }

    public void load() {
        this.databaseInformationLoader = new DatabaseInformationLoader(this.host, this.user, this.password, this.name, this.abstractDatabaseTables);
        this.databaseInformationLoader.init();
    }

    public LinkedList<Object> fullLineByReference(String reference) {
        LinkedList<Object> list = new LinkedList<>();
        LinkedHashMap<String, DatabaseColumnValues> columnValues = this.databaseInformationLoader.getColumnValues();
        int index = columnValues.get(this.databaseInformationLoader.getColumns().get(0)).getValues().indexOf(reference);
        columnValues.forEach((s, databaseColumnValues) -> {
            list.add(databaseColumnValues.get(index, false));
        });
        return list;
    }

    public boolean exist(String referenceValue) {
        return this.get(referenceValue, this.databaseInformationLoader.getReferenceColumn()) != null;
    }

    public void add(LinkedList<Object> values) {
        this.databaseInformationLoader.add(values);
    }

    public void delete(LinkedList<Object> values) {
        this.databaseInformationLoader.delete(values);
    }

    public void modify(LinkedList<Object> values, LinkedList<Object> newValues) {
        this.databaseInformationLoader.modify(values, newValues);
    }

    public char getChar(Object referenceValue, String column) {
        return (char) get(referenceValue, column);
    }

    public double getDouble(Object referenceValue, String column) {
        return (double) get(referenceValue, column);
    }

    public float getFloat(Object referenceValue, String column) {
        return (float) get(referenceValue, column);
    }

    public long getLong(Object referenceValue, String column) {
        return (long) get(referenceValue, column);
    }

    public int getInt(Object referenceValue, String column) {
        return (int) get(referenceValue, column);
    }

    public boolean getBoolean(Object referenceValue, String column) {
        return (boolean) get(referenceValue, column);
    }

    public String getString(Object referenceValue, String column) {
        return (String) get(referenceValue, column);
    }

    public Object get(Object referenceValue, String column) {
        return this.databaseInformationLoader.get(referenceValue, column);
    }

    public DatabaseTableSaveInformation save() {
        return this.databaseInformationLoader.save();
    }
}
