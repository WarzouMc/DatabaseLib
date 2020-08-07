package fr.warzou.databaselib.information;

import fr.warzou.databaselib.information.save.DatabaseTableSaveInformation;
import fr.warzou.databaselib.manager.DatabaseManager;
import fr.warzou.databaselib.tables.DatabaseTablesRegister;
import fr.warzou.databaselib.Database;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Load all information of your table.
 * <p>You could create modification on the table from this class</p>
 * <p>but {@link Database} is better for this.</p>
 * @author Warzou
 * @version 1.1.0
 * @since 0.0.1
 */
public class DatabaseInformationLoader {

    /**
     * {@link LinkedHashMap} in key column name and in value column type
     */
    private LinkedHashMap<String, String> columnType = new LinkedHashMap<>();
    /**
     * {@link LinkedHashMap} in key column name and in value {@link DatabaseColumnValues}
     */
    private LinkedHashMap<String, DatabaseColumnValues> columnValues = new LinkedHashMap<>();
    /**
     * Raw column {@link LinkedList}
     */
    private LinkedList<String> columns = new LinkedList<>();
    /**
     * Associate manager ({@link DatabaseManager})
     */
    private DatabaseManager databaseManager;

    /**
     * Database host
     */
    private String host;
    /**
     * Database account username
     */
    private String user;
    /**
     * Database account password
     */
    private String password;
    /**
     * Group where is your table
     */
    private String groupName;
    /**
     * Associate {@link DatabaseTablesRegister}
     */
    private DatabaseTablesRegister databaseTablesRegister;

    /**
     * Construct a new {@link DatabaseInformationLoader}
     * @param host database host
     * @param user database account username
     * @param password database account password
     * @param groupName group where is your table
     * @param databaseTablesRegister associate {@link DatabaseTablesRegister}
     * @since 0.0.1
     */
    public DatabaseInformationLoader(String host, String user, String password, String groupName, DatabaseTablesRegister databaseTablesRegister) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.groupName = groupName;
        this.databaseTablesRegister = databaseTablesRegister;
    }

    /**
     * Initialise table
     * @since 0.0.1
     */
    public void init() {
        this.columnType = new LinkedHashMap<>(this.databaseTablesRegister.getColumnAndType());
        this.databaseManager = new DatabaseManager(this.host, this.user, this.password, this.groupName, this.databaseTablesRegister)
                .connection()
                .init();
        this.columnType.keySet().forEach(s -> {
            DatabaseColumnValues databaseColumnValues = new DatabaseColumnValues(this.databaseTablesRegister,
                    this.databaseManager, s, this.databaseTablesRegister.getOptionOfColumn(s));
            databaseColumnValues.init();
            this.columnValues.put(s, databaseColumnValues);
            this.columns.add(s);
        });
    }

    /**
     * Add line in your table
     * @param values line values
     * @return true if the addition is a success
     * @since 0.0.1
     */
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

    /**
     * Remove line in your table
     * @param values line values
     * @return true if the remove is a success
     * @since 0.0.1
     */
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

    /**
     * Modify line in your table
     * @param values line values
     * @param newValues new line values
     * @return true if the modification is a success
     * @since 0.0.1
     */
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

    /**
     * Save and update your table
     * @param newCall refresh your table with modification as from another source
     * @return log of the save in {@link DatabaseTableSaveInformation}
     * @since 0.0.1
     */
    public DatabaseTableSaveInformation save(boolean newCall) {
        DatabaseTableSaveInformation databaseTableSaveInformation = this.getDatabaseManager().save(this.getColumnValues());
        this.getColumnValues().forEach((s, databaseColumnValues) -> databaseColumnValues.reload(newCall));
        return databaseTableSaveInformation;
    }

    /**
     * Get object on specific table cell
     * @param referenceValue reference value of line where is your target cell
     * @param column column name where is your target cell
     * @return object on cell with for location column "column" and line "referenceValue"
     * @since 0.0.1
     */
    public Object get(Object referenceValue, String column) {
        String referenceColumn = this.getColumns().get(0);
        int index = this.columnValues.get(referenceColumn).indexOf(referenceValue, false);
        if (index == -1 || !this.columnValues.containsKey(column))
            return null;
        return this.columnValues.get(column).get(index, false);
    }

    /**
     * Get all table
     * @return {@link LinkedHashMap} in key column and value {@link DatabaseColumnValues}
     * @since 0.0.1
     */
    public LinkedHashMap<String, DatabaseColumnValues> getColumnValues() {
        return this.columnValues;
    }

    /**
     * Get column names and types
     * @return {@link LinkedHashMap} in key column name and in value column type
     * @since 0.0.1
     */
    public LinkedHashMap<String, String> getColumnType() {
        return this.columnType;
    }

    /**
     * Get {@link DatabaseManager}
     * @return table manager {@link DatabaseManager}
     * @since 0.0.1
     */
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    /**
     * Get all raw column in your table
     * @return list of raw column {@link LinkedList}
     * @since 0.0.1
     */
    public LinkedList<String> getColumns() {
        return this.columns;
    }

    /**
     * Get reference column (this is your column where all value is different (primary key))
     * @return reference column (first column)
     * @since 0.0.1
     */
    public String getReferenceColumn() {
        return this.getColumns().get(0);
    }
}
