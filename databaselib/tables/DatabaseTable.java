package databaselib.tables;

import java.util.LinkedHashMap;

/**
 * <i>You need to use dbcp2 and mysql-connector (version 8.0.19)</i>
 */
public class DatabaseTable {

    private static LinkedHashMap<Object, AbstractDatabaseTables> abstractDatabaseTablesLinkedHashMap = new LinkedHashMap<>();

    public DatabaseTable add(Object id, AbstractDatabaseTables abstractDatabaseTables) {
        abstractDatabaseTablesLinkedHashMap.put(id, abstractDatabaseTables);
        return this;
    }

    public static LinkedHashMap<Object, AbstractDatabaseTables> getAbstractDatabaseTablesLinkedHashMap() {
        return abstractDatabaseTablesLinkedHashMap;
    }
}
