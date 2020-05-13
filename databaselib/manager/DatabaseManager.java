package databaselib.manager;

import databaselib.information.DatabaseColumnValues;
import databaselib.information.save.DatabaseTableSaveInformation;
import databaselib.tables.AbstractDatabaseTables;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class DatabaseManager {

    private String host, user, password, name;
    private AbstractDatabaseTables abstractDatabaseTables;

    private BasicDataSource connectionPool;
    public DatabaseManager(String host, String user, String password, String name, AbstractDatabaseTables abstractDatabaseTables) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
        this.abstractDatabaseTables = abstractDatabaseTables;
    }

    public DatabaseManager connection() {
        if (this.abstractDatabaseTables == null)
            return null;
        this.connectionPool = new BasicDataSource();
        this.connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
        this.connectionPool.setUsername(this.user);
        this.connectionPool.setPassword(this.password);
        this.connectionPool.setUrl(String.format("jdbc:mysql://%s/%s?autoReconnect=true&useSSL=false", this.host, this.name));
        this.connectionPool.setInitialSize(1);
        this.connectionPool.setMaxTotal(-1);
        return this;
    }

    public DatabaseManager init() {
        if (this.abstractDatabaseTables == null)
            return null;
        StringBuilder stringBuilder = new StringBuilder();
        this.abstractDatabaseTables.getRawColumns().forEach(s -> {
            stringBuilder.append(s);
            String option = this.abstractDatabaseTables.getOptionOfRawColumn(s);
            if (option != null)
                stringBuilder.append(" ").append(option);
            stringBuilder.append(", ");
        });
        update("CREATE TABLE IF NOT EXISTS " + abstractDatabaseTables.getTableName() + " (" +
                stringBuilder.toString().substring(0, stringBuilder.toString().length() - 2) +
                ")", this.getConnection());
        System.out.println("Success \"" + this.abstractDatabaseTables.getTableName() + "\" initialization !\n" +
                "Wait few moment ...\n");
        return this;
    }

    public DatabaseManager update(String query, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void query(String query, Consumer<ResultSet> resultSetConsumer, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSetConsumer.accept(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseTableSaveInformation save(LinkedHashMap<String, DatabaseColumnValues> map) {
        DatabaseTableSaveInformation databaseTableSaveInformation = new DatabaseTableSaveInformation(this.abstractDatabaseTables);
        long start = now();

        System.out.println("Save operation for '" + this.getAbstractDatabaseTables().getTableName() + "' !\n" +
                "...");
        int tableYDimension = map.size();
        int tableXDimension = map.get(map.keySet().iterator().next()).getValues().size();
        System.out.println("Table dimension :\n" +
                "   x: " + tableXDimension + "\n" +
                "   y: " + tableYDimension);

        LinkedList<DatabaseColumnValues> databaseColumnValuesLinkedList = new LinkedList<>();

        map.forEach((s, databaseColumnValues) -> {
            databaseColumnValuesLinkedList.add(databaseColumnValues);
        });

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

    private void addLine(List<StringBuilder> list, String referenceColumn, StringBuilder syntax) {
        Connection connection = this.getConnection();
        for (StringBuilder line : list) {
            String referenceValue = line.toString().split("'")[1];
            System.out.println("Look for '" + referenceValue + "' !");
            this.query(this.selectorQuery(referenceColumn, referenceValue), resultSet -> {
                try {
                    if (!resultSet.next()) {
                        System.out.println("####\n" +
                                "Add line for value '" + referenceValue + "' !");
                        this.update(this.addQuery(syntax.toString(), line.toString()), connection);
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
                        this.update(this.modifyQuery(referenceColumn, referenceValue, line.toString()), connection);
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

    private String selectorQuery(String referenceColumn, String referenceValue) {
        return "SELECT * FROM " + this.getAbstractDatabaseTables().getTableName() + " WHERE " + referenceColumn + "='" + referenceValue + "'";
    }

    private String deleteQuery(String referenceColumn, String referenceValue) {
        return "DELETE FROM " + this.getAbstractDatabaseTables().getTableName() + " WHERE " + referenceColumn + "='" + referenceValue + "'";
    }

    private String addQuery(String syntax, String line) {
        return "INSERT INTO " + this.getAbstractDatabaseTables().getTableName() + syntax + " VALUES " + line;
    }

    private String modifyQuery(String referenceColumn, String referenceValue, String line) {
        return "UPDATE " + this.getAbstractDatabaseTables().getTableName() + " SET " + line + " WHERE " + referenceColumn + "='" + referenceValue + "'";
    }

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

    public Connection getConnection() {
        try {
            return this.connectionPool.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AbstractDatabaseTables getAbstractDatabaseTables() {
        return this.abstractDatabaseTables;
    }

    private long now() {
        return new Date().getTime();
    }
}
