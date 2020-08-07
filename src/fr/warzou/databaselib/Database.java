package fr.warzou.databaselib;

import fr.warzou.databaselib.information.DatabaseColumnValues;
import fr.warzou.databaselib.information.DatabaseInformationLoader;
import fr.warzou.databaselib.information.save.DatabaseTableSaveInformation;
import fr.warzou.databaselib.tables.DatabaseTablesRegister;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Main manager for your table : getter, add/del/modify line
 * <p>
 *     <i>To use this lib you need mysql-connector-java and dbcp2</i>
 * </p>
 * @version 1.1.0
 * @since 0.0.1
 * @author Warzou
 */
public class Database {

    /**
     * Instance variable of {@link DatabaseInformationLoader}
     */
    private DatabaseInformationLoader databaseInformationLoader;

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
     * Construct a new {@link Database}
     * @param databaseTablesRegister instance of a new {@link DatabaseTablesRegister} with table properties
     * @since 0.0.1
     */
    public Database(DatabaseTablesRegister databaseTablesRegister) {
        this.databaseTablesRegister = databaseTablesRegister;
        this.host = databaseTablesRegister.getHost();
        this.user = databaseTablesRegister.getUser();
        this.password = databaseTablesRegister.getPassword();
        this.groupName = databaseTablesRegister.getGroupName();
    }

    /**
     * Load the table
     * @since 0.0.1
     */
    public void load() {
        this.databaseInformationLoader = new DatabaseInformationLoader(this.host, this.user, this.password, this.groupName, this.databaseTablesRegister);
        this.databaseInformationLoader.init();
    }

    /**
     * Check if your value exist
     * @param referenceValue first value in your line
     * @return true if your table contain a line with referenceValue
     * @since 0.0.1
     */
    public boolean exist(String referenceValue) {
        return this.get(referenceValue, this.databaseInformationLoader.getReferenceColumn()) != null;
    }

    /**
     * Add line in your database (null value in the list set the default value of your table)
     * @param values {@link LinkedList} of all value in one line
     * @since 0.0.1
     */
    public void add(LinkedList<Object> values) {
        this.databaseInformationLoader.add(values);
    }

    /**
     * Delete line in your table
     * @param values {@link LinkedList} of all value in one line
     * @since 0.0.1
     */
    public void delete(LinkedList<Object> values) {
        this.databaseInformationLoader.delete(values);
    }

    /**
     * Change a line
     * @param values {@link LinkedList} of all value in one line
     * @param newValues {@link LinkedList} of all new value in one line
     * @since 0.0.1
     */
    public void modify(LinkedList<Object> values, LinkedList<Object> newValues) {
        this.databaseInformationLoader.modify(values, newValues);
    }

    /**
     * Get {@link Character} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link Character} for the line with first value "referenceValue" and with column "column"
     * @since 1.1.0
     */
    public char getChar(Object referenceValue, String column) {
        return (char) get(referenceValue, column);
    }

    /**
     * Get {@link Double} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link Double} for the line with first value "referenceValue" and with column "column"
     * @since 1.0.0
     */
    public double getDouble(Object referenceValue, String column) {
        return (double) get(referenceValue, column);
    }

    /**
     * Get {@link Float} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link Float} for the line with first value "referenceValue" and with column "column"
     * @since 1.0.0
     */
    public float getFloat(Object referenceValue, String column) {
        return (float) get(referenceValue, column);
    }

    /**
     * Get {@link Long} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link Long} for the line with first value "referenceValue" and with column "column"
     * @since 0.0.1
     */
    public long getLong(Object referenceValue, String column) {
        return (long) get(referenceValue, column);
    }

    /**
     * Get {@link Integer} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link Integer} for the line with first value referenceValue and with column column
     * @since 0.0.1
     */
    public int getInt(Object referenceValue, String column) {
        return (int) get(referenceValue, column);
    }

    /**
     * Get {@link Boolean} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link Boolean} for the line with first value referenceValue and with column column
     * @since 0.0.1
     */
    public boolean getBoolean(Object referenceValue, String column) {
        return (boolean) get(referenceValue, column);
    }

    /**
     * Get {@link String} value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return {@link String} for the line with first value referenceValue and with column column
     * @since 0.0.1
     */
    public String getString(Object referenceValue, String column) {
        return (String) get(referenceValue, column);
    }

    /**
     * Get value in specific cell in your table
     * @param referenceValue first value of your line
     * @param column specific column where is your value
     * @return for the line with first value referenceValue and with column column
     * @since 0.0.1
     */
    public Object get(Object referenceValue, String column) {
        return this.databaseInformationLoader.get(referenceValue, column);
    }

    /**
     * Get a {@link LinkedList} of all value in a line
     * @param reference first value of your line
     * @return {@link LinkedList} of all value in one line
     * @since 1.0.1
     */
    public LinkedList<Object> fullLineByReference(String reference) {
        LinkedList<Object> list = new LinkedList<>();
        LinkedHashMap<String, DatabaseColumnValues> columnValues = this.databaseInformationLoader.getColumnValues();
        int index = columnValues.get(this.databaseInformationLoader.getColumns().get(0)).getValues().indexOf(reference);
        columnValues.forEach((s, databaseColumnValues) -> {
            list.add(databaseColumnValues.get(index, false));
        });
        return list;
    }

    /**
     * @return {@link LinkedHashMap} in key column and in value {@link LinkedList}
     * @since 1.0.2
     */
    public LinkedHashMap<String, LinkedList<Object>> getTable() {
        LinkedHashMap<String, LinkedList<Object>> map = new LinkedHashMap<>();
        this.databaseInformationLoader.getColumnValues().forEach((s, databaseColumnValues) -> {
            LinkedList<Object> values = databaseColumnValues.getValues();
            map.put(s, values);
        });
        return map;
    }

    /**
     * Save your table
     * @return save information object {@link DatabaseTableSaveInformation}
     * @since 0.0.1
     */
    public DatabaseTableSaveInformation save() {
        return this.databaseInformationLoader.save(this.databaseTablesRegister.isNewCallOnReload());
    }

    /**
     * @return the name of your table
     * @since 1.1.0
     */
    public String getGroupName() {
        return this.groupName;
    }

    /**
     * @return the host of the database where is your table
     * @since 1.1.0
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @return assigned password to access at your database
     * @since 1.1.0
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @return assigned user to access at your database
     * @since 1.1.0
     */
    public String getUser() {
        return this.user;
    }
}
