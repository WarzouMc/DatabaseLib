package fr.warzou.databaselib.manager;

import fr.warzou.databaselib.information.DatabaseColumnValues;
import fr.warzou.databaselib.information.save.DatabaseTableSaveInformation;
import fr.warzou.databaselib.tables.DatabaseTablesRegister;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Manage the database with.
 * <p>No real interest in making changes from this class.</p>
 * @author Warzou
 * @version 1.1.1
 * @since 0.0.1
 */
public class DatabaseManager {

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
     * Database {@link BasicDataSource}
     */
    private BasicDataSource connectionPool;

    /**
     * Construct a new {@link DatabaseManager}
     * @param host your database host
     * @param user access account username
     * @param password access account password
     * @param groupName group where is your table
     * @param databaseTablesRegister instance of {@link DatabaseTablesRegister}
     * @since 0.0.1
     */
    public DatabaseManager(String host, String user, String password, String groupName, DatabaseTablesRegister databaseTablesRegister) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.groupName = groupName;
        this.databaseTablesRegister = databaseTablesRegister;
    }

    /**
     * Create the database connection
     * @return instance of this class
     * @since 0.0.1
     */
    public DatabaseManager connection() {
        if (this.databaseTablesRegister == null)
            return null;
        this.connectionPool = new BasicDataSource();
        this.connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.connectionPool.setUsername(this.user);
        this.connectionPool.setPassword(this.password);
        this.connectionPool.setUrl(String.format("jdbc:mysql://%s/%s?autoReconnect=true&useSSL=false", this.host, this.groupName));
        this.connectionPool.setInitialSize(1);
        this.connectionPool.setMaxTotal(-1);
        return this;
    }

    /**
     * Init your table (creation if not already exist)
     * @return instance of this class
     * @since 0.0.1
     */
    public DatabaseManager init() {
        if (this.databaseTablesRegister == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        this.databaseTablesRegister.getRawColumns().forEach(s -> {
            stringBuilder.append(s);
            String option = this.databaseTablesRegister.getOptionOfRawColumn(s);
            if (option != null)
                stringBuilder.append(" ").append(option);
            stringBuilder.append(", ");
        });
        update("CREATE TABLE IF NOT EXISTS " + databaseTablesRegister.getTableName() + " (" +
                stringBuilder.substring(0, stringBuilder.toString().length() - 2) +
                ")", this.getConnection());
        System.out.println("Success \"" + this.databaseTablesRegister.getTableName() + "\" initialization !\n" +
                "Wait few moment ...\n");
        return this;
    }

    /**
     * Send an sql query
     * @param query sql query
     * @param connection database connection
     * @return instance of this class
     */
    public DatabaseManager update(String query, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Use with {@link #selectorQuery(String, Object)}
     * @param query sql query
     * @param resultSetConsumer result
     * @param connection database connection
     * @since 0.0.1
     */
    public void query(String query, Consumer<ResultSet> resultSetConsumer, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSetConsumer.accept(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save all modification on your table
     * @param map key: column name, value: {@link DatabaseColumnValues}
     * @return save log in {@link DatabaseTableSaveInformation}
     */
    public DatabaseTableSaveInformation save(LinkedHashMap<String, DatabaseColumnValues> map) {
        if (this.databaseTablesRegister.isNoStorage())
            return null;
        DatabaseTableSaveInformation databaseTableSaveInformation = new DatabaseTableSaveInformation(this.databaseTablesRegister);
        long start = now();

        System.out.println("Save operation for '" + this.getDatabaseTablesRegister().getTableName() + "' !\n" +
                "...");
        int tableYDimension = map.size();
        int tableXDimension = map.get(map.keySet().iterator().next()).getValues().size();
        System.out.println("Table dimension :\n" +
                "   x: " + tableXDimension + "\n" +
                "   y: " + tableYDimension);

        LinkedList<DatabaseColumnValues> databaseColumnValuesLinkedList = new LinkedList<>();

        map.forEach((s, databaseColumnValues) -> databaseColumnValuesLinkedList.add(databaseColumnValues));

        String referenceColumn = "";
        List<StringBuilder> addList = this.queryListByAction(databaseColumnValuesLinkedList, tableXDimension,
                tableYDimension, DatabaseColumnValues.Action.ADD);
        List<StringBuilder> delList = this.queryListByAction(databaseColumnValuesLinkedList, tableXDimension,
                tableYDimension, DatabaseColumnValues.Action.DELETE);
        List<StringBuilder> modifyList = this.queryListByAction(databaseColumnValuesLinkedList, tableXDimension,
                tableYDimension, DatabaseColumnValues.Action.MODIFY);
        StringBuilder syntax = new StringBuilder();

        syntax.append("(");
        for (int i = 0; i < tableYDimension; i++) {
            if (i > 0) syntax.append(", ");
            String column = databaseColumnValuesLinkedList.get(i).getColumnName();
            syntax.append(column);
            if (i == 0)
                referenceColumn = column;
        }
        syntax.append(")");

        System.out.println("Syntax : '" + syntax.toString() + "' !\n" +
                "Reference column : '" + referenceColumn + "' !");

        if (addList.size() > 0)
            addLine(addList, referenceColumn, syntax);

        if (delList.size() > 0)
            delLine(delList, referenceColumn);

        if (modifyList.size() > 0)
            modifyLine(modifyList, referenceColumn);

        System.out.println("Save success !");

        long end = now();

        databaseTableSaveInformation.setDeletion(delList.size());
        databaseTableSaveInformation.setAddition(addList.size());
        databaseTableSaveInformation.setModification(modifyList.size());
        databaseTableSaveInformation.setTimeTake(start, end);
        return databaseTableSaveInformation;
    }

    /**
     * Add many line in table
     * @param list all new line need to be add in the table
     * @param referenceColumn column name where is the reference value
     * @param syntax column name format "(x, y, ...)"
     * @since 0.0.1
     */
    private void addLine(List<StringBuilder> list, String referenceColumn, StringBuilder syntax) {
        Connection connection = this.getConnection();
        for (StringBuilder line : list) {
            String referenceValue = line.toString().split("'")[1];
            System.out.println("Look for '" + referenceValue + "' !");
            if (referenceValue.equals("null")) {
                System.err.println("Fail for line '" + line + "'.\n" +
                        "-> null reference value !");
                continue;
            }
            String[] query = queryCustomer(line, syntax, DatabaseColumnValues.Action.ADD);
            this.query(this.selectorQuery(referenceColumn, referenceValue), resultSet -> {
                try {
                    if (!resultSet.next()) {
                        System.out.println("####\n" +
                                "Add line for value '" + referenceValue + "' !");
                        this.update(this.addQuery(query[1], query[0].replace("\\", "\\\\")), connection);
                        System.out.println("Success add line '" + line + "' !\n" +
                                "####\n");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }, connection);
        }
        System.out.println("Addition success !\n");
    }

    /**
     * Delete many line in table
     * @param list all line need to be delete in the table
     * @param referenceColumn column name where is the reference value
     * @since 0.0.1
     */
    private void delLine(List<StringBuilder> list, String referenceColumn) {
        Connection connection = this.getConnection();
        for (StringBuilder line : list) {
            String referenceValue = line.toString().split("'")[1];
            System.out.println("Look for '" + referenceValue + "' !");
            this.query(this.selectorQuery(referenceColumn, referenceValue), resultSet -> {
                try {
                    if (resultSet.next()) {
                        System.out.println("####\n" +
                                "Del line for value '" + referenceValue + "' !");
                        this.update(this.deleteQuery(referenceColumn, referenceValue), connection);
                        System.out.println("Success del line '" + line + "' !\n" +
                                "####");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }, connection);
        }
        System.out.println("Deletion success !\n");
    }

    /**
     * Modify many line in table
     * @param list all line need to be modify in the table
     * @param referenceColumn column name where is the reference value
     * @since 0.0.1
     */
    private void modifyLine(List<StringBuilder> list, String referenceColumn) {
        Connection connection = this.getConnection();
        for (StringBuilder line : list) {
            String referenceValue = line.toString().split("'")[1];
            System.out.println("Look for '" + referenceValue + "' !");
            this.query(this.selectorQuery(referenceColumn, referenceValue), resultSet -> {
                try {
                    if (resultSet.next()) {
                        System.out.println("####\n" +
                                "Modify line for value '" + referenceValue + "' !");
                        String newLine = queryCustomer(line, new StringBuilder(), DatabaseColumnValues.Action.MODIFY)[0];
                        this.update(this.modifyQuery(referenceColumn, referenceValue, newLine), connection);
                        System.out.println("Success modification line '" + line + "' !\n" +
                                "####");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }, connection);
        }
        System.out.println("Modification success !\n");
    }

    /**
     * Get selection query
     * @param referenceColumn column where is your reference value
     * @param referenceValue reference value (first) in your line
     * @return selection query
     * @since 0.0.1
     */
    private String selectorQuery(String referenceColumn, Object referenceValue) {
        return "SELECT * FROM " + this.getDatabaseTablesRegister().getTableName() + " WHERE " + referenceColumn + "='" + referenceValue + "'";
    }

    /**
     * Get delete query
     * @param referenceColumn column where is your reference value
     * @param referenceValue reference value (first) in your line
     * @return delete query
     * @since 0.0.1
     */
    private String deleteQuery(String referenceColumn, String referenceValue) {
        return "DELETE FROM " + this.getDatabaseTablesRegister().getTableName() + " WHERE " + referenceColumn + "='" + referenceValue + "'";
    }

    /**
     * Get addition query
     * @param syntax column name format "(x, y, ...)"
     * @param line line you to add in table
     * @return addition query
     * @since 0.0.1
     */
    private String addQuery(String syntax, String line) {
        return "INSERT INTO " + this.getDatabaseTablesRegister().getTableName() + syntax + " VALUES " + line;
    }

    /**
     * Get modification query
     * @param referenceColumn column where is your reference value
     * @param referenceValue reference value (first) in your line
     * @param line line you to change in table
     * @return modification query
     * @since 0.0.1
     */
    private String modifyQuery(String referenceColumn, String referenceValue, String line) {
        return "UPDATE " + this.getDatabaseTablesRegister().getTableName() + " SET " + line + " WHERE " + referenceColumn + "='" + referenceValue + "'";
    }

    /**
     * Create list of all query by action type
     * @param databaseColumnValuesLinkedList all {@link DatabaseColumnValues} in a {@link LinkedList}
     * @param tableXDimension height table dimension
     * @param tableYDimension width table dimension
     * @param action type
     * @return list of all query need to update table
     * @since 0.0.1
     */
    private List<StringBuilder> queryListByAction(LinkedList<DatabaseColumnValues> databaseColumnValuesLinkedList, int tableXDimension, int tableYDimension, DatabaseColumnValues.Action action) {
        List<StringBuilder> list = new ArrayList<>();
        boolean isModification = action == DatabaseColumnValues.Action.MODIFY;
        for (int i = 0; i < tableXDimension; i++) {
            StringBuilder builder = new StringBuilder();
            if (!isModification)
                builder.append("(");
            for (int j = 0; j < tableYDimension; j++) {
                if (databaseColumnValuesLinkedList.get(j).getAction(i) == action) {
                    if (isModification) {
                        Object value = databaseColumnValuesLinkedList.get(j).get(i, true);
                        String columnName = databaseColumnValuesLinkedList.get(j).getColumnName();
                        builder.append(", ").append(columnName).append("='").append(value).append("'");
                        continue;
                    }
                    builder.append(", ");
                    Object value = databaseColumnValuesLinkedList.get(j).get(i, true);
                    builder.append("'").append(value).append("'");
                } else break;
            }
            if (!isModification) {
                builder.append(")");
                if (!builder.toString().equals("()")) {
                    builder.replace(1, 3, "");
                    list.add(builder);
                }
            } else {
                if (!builder.toString().equals("")) {
                    builder.replace(0, 2, "");
                    list.add(builder);
                }
            }
        }
        return list;
    }

    /**
     * Get database {@link Connection}
     * @return database connection
     * @since 0.0.1
     */
    public Connection getConnection() {
        try {
            return this.connectionPool.getConnection();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * Get associate {@link DatabaseTablesRegister}
     * @return {@link DatabaseTablesRegister} to this table
     * @since 1.0.0
     */
    public DatabaseTablesRegister getDatabaseTablesRegister() {
        return this.databaseTablesRegister;
    }

    /**
     * Get current time
     * @return current time
     * @since 0.0.2
     */
    private long now() {
        return new Date().getTime();
    }

    /**
     * Return all value in your table of now
     * @return {@link LinkedHashMap} with column names and their values
     * @since 1.1.1
     */
    public LinkedHashMap<String, LinkedList<Object>> getAll() {
        LinkedHashMap<String, LinkedList<Object>> map = new LinkedHashMap<>();
        Connection connection = getConnection();
        for (String column : this.databaseTablesRegister.getColumns()) {
            LinkedList<Object> values = new LinkedList<>();
            query("SELECT " + column + " FROM " + this.databaseTablesRegister.getTableName(), resultSet -> {
                try {
                    while (resultSet.next())
                        values.add(resultSet.getObject(column));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }, connection);
            map.put(column, values);
        }
        return map;
    }

    /**
     * Return all the values in one line on your table
     * @param referenceColumn first column on your table
     * @param referenceValue value of you want on referenceColumn
     * @return {@link LinkedList} of all value in a line
     * @since 1.1.1
     */
    public LinkedList<Object> rawLineGetter(String referenceColumn, Object referenceValue) {
        LinkedList<Object> list = new LinkedList<>();
        LinkedHashMap<String, LinkedList<Object>> map = getAll();
        int index = map.get(referenceColumn).indexOf(referenceValue);
        if (index == -1)
            return null;
        map.keySet().forEach(s -> list.add(map.get(s).get(index)));
        return list;
    }

    /**
     * Get the value of a target cell
     * @param referenceColumn first column in your table
     * @param referenceValue target line value on referenceColumn
     * @param column where is your cell
     * @return value of your target cell
     * @since 1.1.1
     */
    public Object rawGetter(String referenceColumn, Object referenceValue, String column) {
        Connection connection = getConnection();
        AtomicReference<Object> object = new AtomicReference<>();
        object.set(null);
        query(selectorQuery(referenceColumn, referenceValue), resultSet -> {
            try {
                if (resultSet.next())
                    object.set(resultSet.getObject(column));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, connection);
        return object.get();
    }

    /**
     * Add/del/modify values in table
     * @param referenceColumn first column in your table
     * @param values {@link LinkedList} of your values
     * @param action what you want to do
     * @param lastReferenceValue reference value before the modification (only for {@link DatabaseColumnValues.Action#MODIFY}
     * @return true is the changes is a success
     * @since 1.1.1
     */
    public boolean rawSetter(String referenceColumn, LinkedList<Object> values, DatabaseColumnValues.Action action, Object lastReferenceValue) {
        Object referenceValue = values.get(0);
        String syntax = createSyntax();
        if (action == DatabaseColumnValues.Action.NULL)
            return false;
        if (rawGetter(referenceColumn, referenceValue, referenceColumn) != null && action == DatabaseColumnValues.Action.ADD)
            return false;
        StringBuilder line = new StringBuilder();
        if (action == DatabaseColumnValues.Action.MODIFY) {
            for (int i = 0; i < values.size(); i++) {
                Object value = values.get(i);
                String columnName = this.databaseTablesRegister.getColumns().get(i);
                line.append(", ").append(columnName).append("='").append(value).append("'");
            }
            line.replace(0, 2, "");
            String[] query = queryCustomer(line, new StringBuilder(syntax), action);
            update(modifyQuery(referenceColumn, lastReferenceValue + "", query[0]), getConnection());
            return true;
        }
        line.append('(');
        values.forEach(o -> {
            line.append(", ");
            line.append("'").append(o).append("'");
        });
        line.append(')');
        line.replace(1, 3, "");
        String[] query = queryCustomer(line, new StringBuilder(syntax), action);
        String actionQuery = action == DatabaseColumnValues.Action.ADD ? addQuery(query[1], query[0])
                : deleteQuery(referenceColumn, referenceValue + "");
        update(actionQuery, getConnection());
        return true;
    }

    /**
     * Modify query to not take null values
     * @param line original line for the query
     * @param syntax original syntax for the query
     * @param action what is the query action
     * @return query with modification to not take null values (0: line ; 1: syntax)
     * @since 1.1.0
     */
    private String[] queryCustomer(StringBuilder line, StringBuilder syntax, DatabaseColumnValues.Action action) {
        String rawLine = line.toString();

        //modify
        if (action == DatabaseColumnValues.Action.MODIFY)
            return new String[] {rawLine.replace("'null'", "NULL"), ""};

        //add
        String rawSyntax = syntax.toString();
        String[] splitInsertValue = line.toString().split("'");
        List<Integer> removeIndex = new ArrayList<>();
        for (int i = 2; i < splitInsertValue.length; i++)
            if (splitInsertValue[i].equals("null"))
                removeIndex.add(i);
        String[] splitRawSyntax = rawSyntax.split(",");
        removeIndex.forEach(integer -> splitRawSyntax[(integer - 1) / 2] = "");
        StringBuilder newSyntax = new StringBuilder();
        newSyntax.append("(");
        for (int i = 0; i < splitRawSyntax.length; i++) {
            if (splitRawSyntax[i].equals(""))
                continue;
            if (i > 0) newSyntax.append(", ");
            String column = splitRawSyntax[i].replace(" ", "").replace("(", "").replace(")", "");
            newSyntax.append(column);
        }
        newSyntax.append(")");
        rawLine = rawLine.replace(", 'null'", "");
        return new String[] {rawLine, newSyntax.toString()};
    }

    /**
     * Create the query syntax
     * @return query syntax for values
     * @since 1.1.1
     */
    private String createSyntax() {
        StringBuilder syntax = new StringBuilder();
        LinkedList<String> columns = this.databaseTablesRegister.getColumns();
        syntax.append("(");
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) syntax.append(", ");
            String column = columns.get(i);
            syntax.append(column);
        }
        syntax.append(")");
        return syntax.toString();
    }
}
