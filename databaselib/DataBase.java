package databaselib;

import databaselib.inforation.DataBaseColumnValues;
import databaselib.inforation.DataBaseInformationLoader;
import databaselib.tables.DataBaseTable;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DataBase {

    private DataBaseInformationLoader dataBaseInformationLoader;

    private String host;
    private String user;
    private String password;
    private String name;
    private DataBaseTable dataBaseTable;
    public DataBase(String host, String user, String password, String name, DataBaseTable dataBaseTable) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
        this.dataBaseTable = dataBaseTable;
    }

    public void load() {
        this.dataBaseInformationLoader = new DataBaseInformationLoader(this.host, this.user, this.password, this.name, this.dataBaseTable);
        this.dataBaseInformationLoader.init();
    }

    public LinkedList<Object> fullLineByReference(String reference) {
        LinkedList<Object> list = new LinkedList<>();
        LinkedHashMap<String, DataBaseColumnValues> columnValues = this.dataBaseInformationLoader.getColumnValues();
        int index = columnValues.get(this.dataBaseInformationLoader.getColumns().get(0)).getValues().indexOf(reference);
        columnValues.forEach((s, dataBaseColumnValues) -> {
            list.add(dataBaseColumnValues.get(index, false));
        });
        return list;
    }

    public void add(LinkedList<Object> values) {
        this.dataBaseInformationLoader.add(values);
    }

    public void delete(LinkedList<Object> values) {
        this.dataBaseInformationLoader.delete(values);
    }

    public void modify(LinkedList<Object> values, LinkedList<String> newValues) {
        this.dataBaseInformationLoader.modify(values, newValues);
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
        return this.dataBaseInformationLoader.get(referenceValue, column);
    }

    public void save() {
        this.dataBaseInformationLoader.save();
    }
}
