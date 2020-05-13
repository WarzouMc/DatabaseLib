package databaselib.autosave;

import databaselib.Database;
import databaselib.information.save.DatabaseFullSaveInformation;
import databaselib.information.save.DatabaseTableSaveInformation;
import databaselib.tables.DatabaseTable;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseAutoLoader {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Map<Object, Database> map = new HashMap<>();

    public Map<Object, Database> init() {
        System.out.println("Initialisation of your database with DataBaseLib v_1.0.0 by Warzou");
        DatabaseTable.getAbstractDatabaseTablesLinkedHashMap().forEach((id, abstractDatabaseTables) -> {
            Database dataBase = new Database(abstractDatabaseTables);
            this.map.put(id, dataBase);
        });
        return this.map;
    }

    public Map<Object, Database> load(long save) {
        this.map.forEach((id, database) -> database.load());
        System.out.println("Initialization finish");
        Timer timer = new Timer();
        if (save > 0) {
            timer.schedule(new DatabaseAutoSave(this), save, save);
            return this.map;
        }
        timer.schedule(new DatabaseAutoSave(this), 5000 * 60, 5000 * 60);
        return this.map;
    }

    public Future<DatabaseFullSaveInformation> save() {
        return executorService.submit(() -> {
            long start = new Date().getTime();
            DatabaseFullSaveInformation dataBaseFullSaveInformation = new DatabaseFullSaveInformation();
            this.map.forEach((dataBaseTable1, database) -> dataBaseFullSaveInformation.addTableSave(database.save()));
            long end = new Date().getTime();
            dataBaseFullSaveInformation.setTimeTake(start, end);
            out(dataBaseFullSaveInformation);
            return dataBaseFullSaveInformation;
        });
    }

    private void out(DatabaseFullSaveInformation databaseFullSaveInformation) {
        for (DatabaseTableSaveInformation dataBaseTableSaveInformation : databaseFullSaveInformation.getDatabaseTableSaveInformation()) {
            System.out.println("### Save statistics on " + dataBaseTableSaveInformation.getAbstractDatabaseTables().getTableName() + " ###");
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

    public Map<Object, Database> get() {
        return this.map;
    }

    public Database get(Object id) {
        return this.map.get(id);
    }

}
