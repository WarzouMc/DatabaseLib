package databaselib.information;

import databaselib.information.save.DatabaseTableSaveInformation;
import databaselib.manager.DatabaseManager;
import databaselib.tables.AbstractDatabaseTables;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class DatabaseInformationLoader {

    private LinkedHashMap<String, String> columnType = new LinkedHashMap<>();
    private LinkedHashMap<String, DatabaseColumnValues> columnValues = new LinkedHashMap<>();
    private LinkedList<String> columns = new LinkedList<>();
    private DatabaseManager databaseLoader;

    private String host;
    private String user;
    private String password;
    private String name;
    private AbstractDatabaseTables abstractDatabaseTables;
    public DatabaseInformationLoader(String host, String user, String password, String name, AbstractDatabaseTables abstractDatabaseTables) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
        this.abstractDatabaseTables = abstractDatabaseTables;
    }

    public void init() {
        this.columnType = new LinkedHashMap<>(this.abstractDatabaseTables.getColumnAndType());
        this.databaseLoader = new DatabaseManager(this.host, this.user, this.password, this.name, this.abstractDatabaseTables)
                .connection()
                .init();
        this.columnType.keySet().forEach(s -> {
            DatabaseColumnValues databaseColumnValues = new DatabaseColumnValues(this.abstractDatabaseTables,
                    this.databaseLoader, s, this.abstractDatabaseTables.getOptionOfColumn(s));
            databaseColumnValues.init();
            this.columnValues.put(s, databaseColumnValues);
            this.columns.add(s);
        });
    }

    public boolean add(LinkedList<Object> values) {
        if (values.size() < this.getColumnValues().size())
            return false;
        for (Map.Entry<String, DatabaseColumnValues> entry : this.getColumnValues().entrySet()) {
            String s = entry.getKey();
            DatabaseColumnValues databaseColumnValues = entry.getValue();
            int index = this.getColumns().indexOf(s);
            if (!databaseColumnValues.add(values.get(index), index == 0))
                break;
        }
        return true;
    }

    public boolean delete(LinkedList<Object> values) {
        if (values.size() < this.getColumnValues().size())
            return false;
        for (Map.Entry<String, DatabaseColumnValues> entry : this.getColumnValues().entrySet()) {
            String s = entry.getKey();
            DatabaseColumnValues databaseColumnValues = entry.getValue();
            int index = this.getColumns().indexOf(s);
            if (!databaseColumnValues.del(values.get(index), index == 0))
                break;
        }
        return true;
    }

    public boolean modify(LinkedList<Object> values, LinkedList<Object> newValues) {
        if (values.size() < this.getColumnValues().size())
            return false;
        int columnIndex = this.getColumnValues().get(this.getColumns().get(0)).getValues().indexOf(values.get(0));
        for (Map.Entry<String, DatabaseColumnValues> entry : this.getColumnValues().entrySet()) {
            String s = entry.getKey();
            DatabaseColumnValues databaseColumnValues = entry.getValue();
            int index = this.getColumns().indexOf(s);
            if (!databaseColumnValues.modify(values.get(index), newValues.get(index), index == 0, columnIndex))
                break;
        }
        return true;
    }

    public DatabaseTableSaveInformation save() {
        DatabaseTableSaveInformation databaseTableSaveInformation = this.getDatabaseLoader().save(this.getColumnValues());
        this.getColumnValues().forEach((s, databaseColumnValues) -> databaseColumnValues.reload());
        return databaseTableSaveInformation;
    }

    public Object get(Object referenceValue, String column) {
        String referenceColumn = this.getColumns().get(0);
        int index = this.columnValues.get(referenceColumn).indexOf(referenceValue, false);
        if (index == -1 || !this.columnValues.containsKey(column))
            return null;
        return this.columnValues.get(column).get(index, false);
    }

    public LinkedHashMap<String, DatabaseColumnValues> getColumnValues() {
        return this.columnValues;
    }

    public LinkedHashMap<String, String> getColumnType() {
        return this.columnType;
    }

    public DatabaseManager getDatabaseLoader() {
        return this.databaseLoader;
    }

    public LinkedList<String> getColumns() {
        return this.columns;
    }

    public String getReferenceColumn() {
        return this.getColumns().get(0);
    }
}
