package databaselib.information;

import databaselib.manager.DataBaseManager;
import databaselib.tables.DataBaseTable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class DataBaseInformationLoader {

    private LinkedHashMap<String, String> columnType = new LinkedHashMap<>();
    private LinkedHashMap<String, DataBaseColumnValues> columnValues = new LinkedHashMap<>();
    private LinkedList<String> columns = new LinkedList<>();
    private DataBaseManager dataBaseLoader;

    private String host;
    private String user;
    private String password;
    private String name;
    private DataBaseTable dataBaseTable;
    public DataBaseInformationLoader(String host, String user, String password, String name, DataBaseTable dataBaseTable) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
        this.dataBaseTable = dataBaseTable;
    }

    public void init() {
        this.dataBaseTable.getRawValues().forEach(s -> this.columnType.put(s.split(" ")[0],
                    s.replace(s.split(" ")[0] + " ", "")));
        this.dataBaseLoader = new DataBaseManager(this.host, this.user, this.password, this.name, dataBaseTable)
                .connection()
                .init();
        this.columnType.keySet().forEach(s -> {
            DataBaseColumnValues dataBaseColumnValues = new DataBaseColumnValues(dataBaseTable, this.dataBaseLoader, s);
            dataBaseColumnValues.init();
            this.columnValues.put(s, dataBaseColumnValues);
            this.columns.add(s);
        });
    }

    public boolean add(LinkedList<Object> values) {
        if (values.size() < this.getColumnValues().size())
            return false;
        for (Map.Entry<String, DataBaseColumnValues> entry : this.getColumnValues().entrySet()) {
            String s = entry.getKey();
            DataBaseColumnValues dataBaseColumnValues = entry.getValue();
            int index = this.getColumns().indexOf(s);
            if (!dataBaseColumnValues.add(values.get(index), index == 0))
                break;
        }
        return true;
    }

    public boolean delete(LinkedList<Object> values) {
        if (values.size() < this.getColumnValues().size())
            return false;
        for (Map.Entry<String, DataBaseColumnValues> entry : this.getColumnValues().entrySet()) {
            String s = entry.getKey();
            DataBaseColumnValues dataBaseColumnValues = entry.getValue();
            int index = this.getColumns().indexOf(s);
            if (!dataBaseColumnValues.del(values.get(index), index == 0))
                break;
        }
        return true;
    }

    public boolean modify(LinkedList<Object> values, LinkedList<String> newValues) {
        if (values.size() < this.getColumnValues().size())
            return false;
        int columnIndex = this.getColumnValues().get(this.getColumns().get(0)).getValues().indexOf(values.get(0));
        for (Map.Entry<String, DataBaseColumnValues> entry : this.getColumnValues().entrySet()) {
            String s = entry.getKey();
            DataBaseColumnValues dataBaseColumnValues = entry.getValue();
            int index = this.getColumns().indexOf(s);
            if (!dataBaseColumnValues.modify(values.get(index), newValues.get(index), index == 0, columnIndex))
                break;
        }
        return true;
    }

    public void save() {
        this.getDataBaseLoader().save(this.getColumnValues());
        this.getColumnValues().forEach((s, dataBaseColumnValues) -> dataBaseColumnValues.reload());
    }

    public Object get(Object referenceValue, String column) {
        String referenceColumn = this.getColumns().get(0);
        int index = this.columnValues.get(referenceColumn).indexOf(referenceValue, false);
        if (index == -1 || !this.columnValues.containsKey(column))
            return null;
        return this.columnValues.get(column).get(index, false);
    }

    public LinkedHashMap<String, DataBaseColumnValues> getColumnValues() {
        return this.columnValues;
    }

    public LinkedHashMap<String, String> getColumnType() {
        return this.columnType;
    }

    public DataBaseManager getDataBaseLoader() {
        return this.dataBaseLoader;
    }

    public LinkedList<String> getColumns() {
        return this.columns;
    }
}
