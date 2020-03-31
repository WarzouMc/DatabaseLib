package databaselib.tables;

import java.util.Arrays;
import java.util.List;

public enum DataBaseTable {

    DEFAULT("default_table", Arrays.asList("first_column TEXT"));

    private String tableName;
    private List<String> values;

    DataBaseTable(String tableName, List<String> values) {
        this.tableName = tableName;
        this.values = values;
    }

    public String getTableName() {
        return this.tableName;
    }

    public List<String> getRawValues() {
        return this.values;
    }

    public static DataBaseTable byName(String name) {
        return Arrays.stream(DataBaseTable.values())
                .filter(dataBaseTable -> dataBaseTable.tableName.equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
