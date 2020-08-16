package fr.warzou.databaselib.tables;

import fr.warzou.databaselib.Database;
import fr.warzou.databaselib.autosave.DatabaseAutoLoader;

import java.util.LinkedHashMap;

/**
 * Keep all your table and get it later
 * @author Warzou
 * @version 1.1.0
 * @since 0.0.1
 */
public class DatabaseTable {

    /**
     * {@link LinkedHashMap} to keep all {@link DatabaseTablesRegister} with an id.
     * @since 1.0.0
     */
    private static final LinkedHashMap<Object, DatabaseTablesRegister> databaseTablesRegisterLinkedHashMap = new LinkedHashMap<>();

    /**
     * Construct a new {@link DatabaseTable}
     */
    public DatabaseTable() {}

    /**
     * Add table in the code
     * @param id unique id for your table, use it to have access at {@link Database} from {@link DatabaseAutoLoader}
     * @param databaseTablesRegister instance of a {@link DatabaseTablesRegister}
     * @return instance of this class
     * @since 1.0.0
     */
    public DatabaseTable add(Object id, DatabaseTablesRegister databaseTablesRegister) {
        databaseTablesRegisterLinkedHashMap.put(id, databaseTablesRegister);
        return this;
    }

    /**
     * Get a list of all your registry {@link DatabaseTablesRegister}
     * @return a {@link LinkedHashMap} of all your registry table (key: id, value: {@link DatabaseTablesRegister})
     * @since 1.0.0
     */
    public static LinkedHashMap<Object, DatabaseTablesRegister> getDatabaseTablesRegisterLinkedHashMap() {
        return databaseTablesRegisterLinkedHashMap;
    }
}
