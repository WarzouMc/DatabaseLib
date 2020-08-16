package fr.warzou.databaselib.information;

import fr.warzou.databaselib.manager.DatabaseManager;
import fr.warzou.databaselib.tables.DatabaseTablesRegister;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Manage all value for a target column.
 * @author Warzou
 * @version 1.1.1
 * @since 0.0.1
 */
public class DatabaseColumnValues {

    /**
     * List of all values in column
     */
    private LinkedList<Object> values;
    /**
     * List of all values action type
     * <p>It's use on update and to stock current modification</p>
     */
    private LinkedList<Action> goals;

    /**
     * Associate {@link DatabaseTablesRegister}
     */
    private DatabaseTablesRegister databaseTablesRegister;
    /**
     * Associate {@link DatabaseManager}
     */
    private DatabaseManager databaseManager;
    /**
     * Target column name.
     */
    private String column;
    /**
     * Target column option.
     */
    private String option;

    /**
     * Construct a new {@link DatabaseColumnValues}
     * @param databaseTablesRegister associate {@link DatabaseTablesRegister}
     * @param databaseManager associate {@link DatabaseManager}
     * @param column name
     * @param option column option
     * @since 0.0.1
     */
    public DatabaseColumnValues(DatabaseTablesRegister databaseTablesRegister, DatabaseManager databaseManager, String column, String option) {
        this.databaseTablesRegister = databaseTablesRegister;
        this.databaseManager = databaseManager;
        this.column = column;
        this.option = option;

        this.values = new LinkedList<>();
        this.goals = new LinkedList<>();
    }

    /**
     * Init the target column.
     * <p>Add all value of this column in {@link #values} and set default goal {@link DatabaseColumnValues.Action#NULL}.</p>
     * @since 0.0.1
     */
    void init() {
        this.values.clear();
        this.goals.clear();
        Connection connection = this.databaseManager.getConnection();
        this.databaseManager.query("SELECT " + this.column + " FROM " + this.databaseTablesRegister.getTableName(), resultSet -> {
            try {
                while (resultSet.next()) {
                    Object value = resultSet.getObject(this.column);
                    this.values.add(value);
                    this.goals.add(Action.NULL);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, connection);
    }

    /**
     * Reload all value of the current column.
     * @param newCall call {@link #init()} if true
     * @since 0.0.1
     */
    void reload(boolean newCall) {
        if (newCall) {
            init();
            return;
        }
        for (int i = 0; i < this.values.size(); i++)
            if (this.goals.get(i) == Action.DELETE)
                this.values.remove(i);
        this.goals.clear();
        this.values.forEach(s -> this.goals.add(Action.NULL));
    }

    /**
     * Add value in column
     * @param value add value
     * @param first true mean the value is reference
     * @return true is the addition is a success
     * @since 0.0.1
     */
    public boolean add(Object value, boolean first) {
        if (first && this.values.contains(value))
            return false;
        this.values.add(value);
        this.goals.add(Action.ADD);
        return true;
    }

    /**
     * Remove value in column
     * @param value remove value
     * @param first true mean the value is reference
     * @return true is the remove is a success
     * @since 0.0.1
     */
    public boolean del(Object value, boolean first) {
        if (first && !this.values.contains(value))
            return false;
        int index = this.values.indexOf(value);
        this.goals.set(index, Action.DELETE);
        return true;
    }

    /**
     * Modify value in column
     * @param value old value
     * @param newValue replacement value
     * @param first true mean the value is reference
     * @param index where
     * @return true is the modification is a success
     * @since 0.0.1
     */
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

    /**
     * Get all value in the current column
     * @return column value {@link LinkedList}
     * @since 0.0.1
     */
    public LinkedList<Object> getValues() {
        return this.values;
    }

    /**
     * Get all value goal (update action) in the current column
     * @return column value goal {@link LinkedList}
     * @since 0.0.1
     */
    public LinkedList<Action> getGoals() {
        return this.goals;
    }

    /**
     * Get the column name
     * @return the name of the current column
     * @since 0.0.1
     */
    public String getColumnName() {
        return this.column;
    }

    /**
     * Get value in a specific index
     * @param index where
     * @param force if true the delete value could be return  (while the update is not do)
     * @return value on specific index
     * @since 0.0.1
     */
    public Object get(int index, boolean force) {
        if (this.values.size() < index && (getAction(index) != Action.DELETE && !force))
            return null;
        return this.values.get(index);
    }

    /**
     * @param value target value
     * @param force if true could return the index of a delete value (while the update is not do)
     * @return index of specific value
     * @since 0.0.1
     */
    public int indexOf(Object value, boolean force) {
        if (!this.values.contains(value) && (getAction(value) != Action.DELETE && !force))
            return -1;
        return this.values.indexOf(value);
    }

    /**
     * Get action for a index
     * @param index where
     * @return {@link Action} for value on specific index
     * @since 0.0.1
     */
    public Action getAction(int index) {
        if (this.goals.size() < index)
            return null;
        return this.goals.get(index);
    }

    /**
     * Get action for a target value
     * @param value target value
     * @return {@link Action} for a value
     * @since 0.0.1
     */
    public Action getAction(Object value) {
        if (!this.values.contains(value))
            return null;
        int index = this.values.indexOf(value);
        return this.goals.get(index);
    }

    /**
     * Get option of the current column
     * @return column option
     * @since 0.0.1
     */
    public String getOption() {
        return this.option;
    }

    /**
     * Different action of value can take for next update.
     * @author Warzou
     * @version 0.0.1
     * @since 0.0.1
     */
    public enum Action {
        /**
         * Any action
         */
        NULL,
        /**
         * Delete the value during update
         */
        DELETE,
        /**
         * Add the value during update
         */
        ADD,
        /**
         * Modify the value during update
         */
        MODIFY
    }
}
