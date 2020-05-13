package databaselib.tables;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public abstract class AbstractDatabaseTables {

    private String host;
    private String user;
    private String password;
    private String name;
    private String tableName;

    private final LinkedList<String> columns = new LinkedList<>();
    private final LinkedList<String> option = new LinkedList<>();
    public AbstractDatabaseTables(String host, String user, String password, String name, String tableName) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
        this.tableName = tableName;
    }

    public AbstractDatabaseTables registry(String line) {
        return this.registry(new LinkedList<>(Collections.singletonList(line)));
    }

    public AbstractDatabaseTables registry(String... line) {
        return this.registry(new LinkedList<>(Arrays.asList(line)));
    }

    public AbstractDatabaseTables registry(LinkedList<String> line) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        line.forEach(s -> map.put(s, null));
        return this.registry(map);
    }

    public AbstractDatabaseTables registry(LinkedHashMap<String, String> line) {
        line.forEach(this::registry);
        return this;
    }

    public AbstractDatabaseTables registry(String line, String option) {
        this.columns.add(line);
        this.option.add(option);
        return this;
    }

    public LinkedList<String> getRawColumns() {
        return this.columns;
    }

    public LinkedList<String> getColumns() {
        return new LinkedList<>(this.getColumnAndType().keySet());
    }

    public LinkedList<String> getOption() {
        return this.option;
    }

    public LinkedHashMap<String, String> getRawColumnOption() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        this.getRawColumns().forEach(s -> {
            int index = this.option.indexOf(s);
            map.put(s, this.option.get(index));
        });
        return map;
    }

    public LinkedHashMap<String, String> getColumnAndType() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        this.getRawColumns().forEach(s -> map.put(s.split(" ")[0],
                s.replace(s.split(" ")[0] + " ", "")));
        return map;
    }

    public LinkedHashMap<String, String> getColumnsAndRawColumns() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        this.getColumnAndType().forEach((s, s2) -> map.put(s, s + " " + s2));
        return map;
    }

    public String getHost() {
        return this.host;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.name;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getOptionOfRawColumn(String column) {
        System.out.println("raw column " + column);
        return this.getOption().get(this.getRawColumns().indexOf(column));
    }

    public String getOptionOfColumn(String column) {
        System.out.println("column " + column);
        return this.getOption().get(this.getColumns().indexOf(column));
    }
}
