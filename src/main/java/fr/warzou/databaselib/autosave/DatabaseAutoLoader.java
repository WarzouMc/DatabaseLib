package fr.warzou.databaselib.autosave;

import fr.warzou.databaselib.Database;
import fr.warzou.databaselib.information.save.DatabaseFullSaveInformation;
import fr.warzou.databaselib.information.save.DatabaseTableSaveInformation;
import fr.warzou.databaselib.tables.DatabaseTable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Load your table.
 * @author Warzou
 * @version 1.1.4
 * @since 0.0.1
 */
public class DatabaseAutoLoader {

    /**
     * Use for async save.
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    /**
     * Map to stock id for all {@link Database}
     * {@link Map}
     * <p>key: object (id)</p>
     * <p>value: {@link Database}</p>
     */
    private Map<Object, Database> map = new HashMap<>();

    /**
     * Construct a new {@link DatabaseAutoLoader}
     * @since 1.1.0
     */
    public DatabaseAutoLoader() {}

    /**
     * Init all tables
     * @return all {@link Database} with their id
     * @since 0.0.1
     */
    public Map<Object, Database> init() {
        System.out.println("Initialisation of your database with DatabaseLib v_1.1.4 by Warzou !");
        DatabaseTable.getDatabaseTablesRegisterLinkedHashMap().forEach((id, abstractDatabaseTables) -> {
            Database dataBase = new Database(abstractDatabaseTables);
            this.map.put(id, dataBase);
        });
        return this.map;
    }

    /**
     * Load all tables
     * @param save time between two save
     * @return all {@link Database} with their id
     * @since 0.0.1
     */
    public Map<Object, Database> load(long save) {
        this.map.forEach((id, database) -> database.load());
        System.out.println("Initialization finish");
        Timer timer = new Timer();
        save = save > 0 ? save : 5000 * 60;
        timer.schedule(new DatabaseAutoSave(this), save, save);
        return this.map;
    }

    /**
     * Save all tables
     * @return save log {@link DatabaseFullSaveInformation}
     * @since 0.0.1
     */
    public Future<DatabaseFullSaveInformation> save() {
        return executorService.submit(() -> {
            long start = new Date().getTime();
            DatabaseFullSaveInformation dataBaseFullSaveInformation = new DatabaseFullSaveInformation();
            this.map.forEach((dataBaseTable1, database) -> {
                DatabaseTableSaveInformation databaseTableSaveInformation = database.save();
                if (databaseTableSaveInformation == null)
                    return;
                dataBaseFullSaveInformation.addTableSave(databaseTableSaveInformation);
            });
            long end = new Date().getTime();
            dataBaseFullSaveInformation.setTimeTake(start, end);
            out(dataBaseFullSaveInformation);
            return dataBaseFullSaveInformation;
        });
    }

    /**
     * Print save log
     * @param databaseFullSaveInformation save log
     * @since 0.0.2
     */
    private void out(DatabaseFullSaveInformation databaseFullSaveInformation) {
        for (DatabaseTableSaveInformation dataBaseTableSaveInformation : databaseFullSaveInformation.getDatabaseTableSaveInformation()) {
            System.out.println("### Save statistics on " + dataBaseTableSaveInformation.getDatabaseTablesRegister().getTableName() + " ###");
            System.out.println("- Additions : " + dataBaseTableSaveInformation.getAddition());
            System.out.println("- Deletions : " + dataBaseTableSaveInformation.getDeletion());
            System.out.println("- Modifications : " + dataBaseTableSaveInformation.getModification());
            System.out.println("This table took " + dataBaseTableSaveInformation.getTimeTake() + "s to be saved !\n");
        }
        System.out.println("\n### All save statistics ###");
        System.out.println("- Additions : " + databaseFullSaveInformation.getTotalAddition());
        System.out.println("- Deletions : " + databaseFullSaveInformation.getTotalDeletion());
        System.out.println("- Modifications : " + databaseFullSaveInformation.getTotalModification());
        System.out.println("This save took " + databaseFullSaveInformation.getTimeTake() + "s !");
    }

    /**
     * Get Database with id
     * @return {@link Map} in key a id and in value {@link Database}
     * @since 0.0.1
     */
    public Map<Object, Database> get() {
        return this.map;
    }

    /**
     * Get Database by id
     * @param id target id
     * @return {@link Database} for a specific if
     * @since 0.0.1
     */
    public Database get(Object id) {
        return this.map.get(id);
    }

}
