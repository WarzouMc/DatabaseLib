package fr.warzou.databaselib.tables;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Define your table properties to allow the creation of this table
 * @author Warzou
 * @version 1.1.1
 * @since 1.0.0
 */
public class DatabaseTablesRegister {

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
     * Table name
     */
    private String tableName;
    /**
     * Take the modifications from another source when you reload table if true
     */
    private boolean newCallOnReload;
    /**
     * Do not save values in code
     */
    private boolean noStorage;

    /**
     * Raw column {@link LinkedList}
     */
    private final LinkedList<String> columns = new LinkedList<>();
    /**
     * Column option {@link LinkedList}
     */
    private final LinkedList<String> option = new LinkedList<>();

    /**
     * Construct a new {@link DatabaseTablesRegister}
     * @param host database host
     * @param user connection account username
     * @param password connection account password
     * @param groupName name of the group where is your table
     * @param tableName name of the table
     * @param newCallOnReload refresh your table with modification as from another source
     * @param noStorage if true do not save values in code
     * @since 1.0.0
     */
    public DatabaseTablesRegister(String host, String user, String password, String groupName, String tableName, boolean newCallOnReload, boolean noStorage) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.groupName = groupName;
        this.tableName = tableName;
        this.newCallOnReload = newCallOnReload;
        this.noStorage = noStorage;
    }

    /**
     * Add column in the table
     * @param column column name and type -- name TYPE
     * @return instance of this class
     * @since 1.0.0
     */
    public DatabaseTablesRegister registry(String column) {
        return this.registry(new LinkedList<>(Collections.singletonList(column)));
    }

    /**
     * Add many columns in the table
     * @param columns column list of name and type -- name TYPE
     * @return instance of this class
     * @since 1.0.0
     */
    public DatabaseTablesRegister registry(String... columns) {
        return this.registry(new LinkedList<>(Arrays.asList(columns)));
    }

    /**
     * Add many columns in the table
     * @param columns column ({@link LinkedList} name and type -- name TYPE
     * @return instance of this class
     * @since 1.0.0
     */
    public DatabaseTablesRegister registry(LinkedList<String> columns) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        columns.forEach(s -> map.put(s, null));
        return this.registry(map);
    }

    /**
     * Add many columns with option in table
     * @param columnsWithOption add columns with the name and type in key of a {@link LinkedHashMap} and in value an option
     * @return instance of this class
     * @since 1.0.0
     */
    public DatabaseTablesRegister registry(LinkedHashMap<String, String> columnsWithOption) {
        columnsWithOption.forEach(this::registry);
        return this;
    }

    /**
     * Add one column with an option
     * @param column column name and type -- name TYPE
     * @param option column option
     * @return instance of this class
     * @since 1.0.0
     */
    public DatabaseTablesRegister registry(String column, String option) {
        this.columns.add(column);
        this.option.add(option);
        return this;
    }

    /**
     * Change refresh modification call value
     * @param newCallOnReload true to refresh your table with modification as from another source
     * @since 1.1.1
     */
    public void setNewCallOnReload(boolean newCallOnReload) {
        this.newCallOnReload = newCallOnReload;
    }

    /**
     * Get raw column {@link LinkedList}
     * @return {@link LinkedList} of all column with their type
     * @since 1.0.0
     */
    public LinkedList<String> getRawColumns() {
        return this.columns;
    }

    /**
     * Get column name {@link LinkedList}
     * @return {@link LinkedList} of all columns name
     * @since 1.0.0
     */
    public LinkedList<String> getColumns() {
        return new LinkedList<>(this.getColumnAndType().keySet());
    }

    /**
     * Get column option {@link LinkedList}
     * @return {@link LinkedList} of all columns option
     * @since 1.0.0
     */
    public LinkedList<String> getOption() {
        return this.option;
    }

    /**
     * Get raw column and assigned option {@link LinkedHashMap}
     * @return {@link LinkedHashMap} with in key raw column and in value column option
     * @since 1.0.0
     */
    public LinkedHashMap<String, String> getRawColumnOption() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        this.getRawColumns().forEach(s -> {
            int index = this.option.indexOf(s);
            map.put(s, this.option.get(index));
        });
        return map;
    }

    /**
     * Get columns and assigner type {@link LinkedHashMap}
     * @return {@link LinkedHashMap} with in key column name and in value column type
     * @since 1.0.0
     */
    public LinkedHashMap<String, String> getColumnAndType() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        this.getRawColumns().forEach(s -> map.put(s.split(" ")[0],
                s.replace(s.split(" ")[0] + " ", "")));
        return map;
    }

    /**
     * Get column and their raw value {@link LinkedHashMap}
     * @return {@link LinkedHashMap} with in key column name and in value raw columns
     * @since 1.0.0
     */
    public LinkedHashMap<String, String> getColumnsAndRawColumns() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        this.getColumnAndType().forEach((s, s2) -> map.put(s, s + " " + s2));
        return map;
    }

    /**
     * Get your database host
     * @return database host
     * @since 1.0.0
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Get account username
     * @return connection account username
     * @since 1.0.0
     */
    public String getUser() {
        return this.user;
    }

    /**
     * Get account password
     * @return connection account password
     * @since 1.0.0
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Get group where is your table in the database
     * @return table group in database
     * @since 1.0.0
     */
    public String getGroupName() {
        return this.groupName;
    }

    /**
     * Get the name of your table
     * @return the table name
     * @since 1.0.0
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Get the option of a target raw column
     * @param column target raw column
     * @return raw column option
     * @since 1.0.0
     */
    public String getOptionOfRawColumn(String column) {
        System.out.println("raw column " + column);
        return this.getOption().get(this.getRawColumns().indexOf(column));
    }

    /**
     * Get the option of a target column
     * @param column target column name
     * @return column option
     * @since 1.0.0
     */
    public String getOptionOfColumn(String column) {
        System.out.println("column " + column);
        return this.getOption().get(this.getColumns().indexOf(column));
    }

    /**
     * Return refresh modification call value
     * @return true if your table take the modification of another source when it's reload
     * @since 1.1.0
     */
    public boolean isNewCallOnReload() {
        return this.newCallOnReload;
    }

    /**
     * Return storage keeping value
     * @return true if you don't keep values
     * @since 1.1.1
     */
    public boolean isNoStorage() {
        return this.noStorage;
    }
}
