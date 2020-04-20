package databaselib.information;

import databaselib.manager.DataBaseManager;
import databaselib.tables.DataBaseTable;

import java.sql.SQLException;
import java.util.LinkedList;

public class DataBaseColumnValues {

    private LinkedList<Object> values = new LinkedList<>();
    private LinkedList<Action> goals = new LinkedList<>();

    private DataBaseTable dataBaseTable;
    private DataBaseManager dataBaseLoader;
    private String column;
    public DataBaseColumnValues(DataBaseTable dataBaseTable, DataBaseManager dataBaseLoader, String column) {
        this.dataBaseTable = dataBaseTable;
        this.dataBaseLoader = dataBaseLoader;
        this.column = column;
    }

    void init() {
        this.dataBaseLoader.query("SELECT " + this.column + " FROM " + this.dataBaseTable.getTableName(), resultSet -> {
            try {
                while (resultSet.next()) {
                    Object value = resultSet.getObject(this.column);
                    this.values.add(value);
                    this.goals.add(Action.NULL);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, this.dataBaseLoader.getConnection());
    }

    void reload() {
        for (int i = 0; i < this.values.size(); i++) {
            if (this.goals.get(i) == Action.DELETE)
                this.values.remove(i);
        }
        this.goals.clear();
        this.values.forEach(s -> this.goals.add(Action.NULL));
    }

    public boolean add(Object value, boolean first) {
        if (first && this.values.contains(value))
            return false;
        this.values.add(value);
        this.goals.add(Action.ADD);
        return true;
    }

    public boolean del(Object value, boolean first) {
        if (first && !this.values.contains(value))
            return false;
        int index = this.values.indexOf(value);
        this.goals.set(index, Action.DELETE);
        return true;
    }

    public boolean modify(Object value, Object newValue, boolean first, int index) {
        if (!this.values.contains(value)) {
            return false;
        }
        if (first) {
            if (getAction(index) == Action.ADD)
                return true;
            this.goals.set(index, Action.MODIFY);
            return true;
        }
        this.values.set(index, newValue);
        if (getAction(index) == Action.ADD)
            return true;
        this.goals.set(index, Action.MODIFY);
        return true;
    }

    public LinkedList<Object> getValues() {
        return this.values;
    }

    public LinkedList<Action> getGoals() {
        return this.goals;
    }

    public String getColumnName() {
        return this.column;
    }

    public Object get(int index, boolean force) {
        if (this.values.size() < index && (getAction(index) != Action.DELETE && !force))
            return null;
        return this.values.get(index);
    }

    public int indexOf(Object value, boolean force) {
        if (!this.values.contains(value) && (getAction(value) != Action.DELETE && !force))
            return -1;
        return this.values.indexOf(value);
    }

    public Action getAction(int index) {
        if (this.goals.size() < index)
            return null;
        return this.goals.get(index);
    }

    public Action getAction(Object value) {
        if (!this.values.contains(value))
            return null;
        int index = this.values.indexOf(value);
        return this.goals.get(index);
    }

    public enum Action {
        NULL, DELETE, ADD, MODIFY
    }
}
