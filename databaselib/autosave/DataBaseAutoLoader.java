package databaselib.autosave;

import databaselib.DataBase;
import databaselib.information.save.DataBaseFullSaveInformation;
import databaselib.information.save.DataBaseTableSaveInformation;
import databaselib.tables.DataBaseTable;

import java.util.*;

public class DataBaseAutoLoader {

    private String host;
    private String user;
    private String password;
    private String name;

    private Map<DataBaseTable, DataBase> map = new HashMap<>();
    public DataBaseAutoLoader(String host, String user, String password, String name) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.name = name;
    }

    public Map<DataBaseTable, DataBase> init() {
        System.out.println("Initialisation of your database with DataBaseLib v_0.0.2 by Warzou");
        Arrays.asList(DataBaseTable.values()).forEach(dataBaseTable -> {
            DataBase dataBase = new DataBase(this.host, this.user, this.password, this.name, dataBaseTable);
            this.map.put(dataBaseTable, dataBase);
        });
        return this.map;
    }

    public Map<DataBaseTable, DataBase> load(long save) {
        this.map.forEach((dataBaseTable1, dataBase) -> dataBase.load());
        System.out.println("Initialization finish");
        Timer timer = new Timer();
        if (save > 0) {
            timer.schedule(new DataBaseAutoSave(this), save, save);
            return this.map;
        }
        timer.schedule(new DataBaseAutoSave(this), 5000 * 60, 5000 * 60);
        return this.map;
    }

    public DataBaseFullSaveInformation save() {
        long start = new Date().getTime();
        DataBaseFullSaveInformation dataBaseFullSaveInformation = new DataBaseFullSaveInformation();
        this.map.forEach((dataBaseTable1, dataBase) -> dataBaseFullSaveInformation.addTableSave(dataBase.save()));
        long end = new Date().getTime();
        dataBaseFullSaveInformation.setTimeTake(start, end);
        out(dataBaseFullSaveInformation);
        return dataBaseFullSaveInformation;
    }

    private void out(DataBaseFullSaveInformation dataBaseFullSaveInformation) {
        for (DataBaseTableSaveInformation dataBaseTableSaveInformation : dataBaseFullSaveInformation.getDataBaseTableSaveInformation()) {
            System.out.println("### Save statistics on " + dataBaseTableSaveInformation.getDataBaseTable().getTableName() + " ###");
            System.out.println("- Additions : " + dataBaseTableSaveInformation.getAddition());
            System.out.println("- Deletions : " + dataBaseTableSaveInformation.getDeletion());
            System.out.println("- Modifications : " + dataBaseTableSaveInformation.getModification());
            System.out.println("This table has taken " + dataBaseTableSaveInformation.getTimeTake() + "s to be saved !\n");
        }
        System.out.println("\n### All save statistics ###");
        System.out.println("- Additions : " + dataBaseFullSaveInformation.getTotalAddition());
        System.out.println("- Deletions : " + dataBaseFullSaveInformation.getTotalDeletion());
        System.out.println("- Modifications : " + dataBaseFullSaveInformation.getTotalModification());
        System.out.println("This save has taken " + dataBaseFullSaveInformation.getTimeTake() + "s !");
    }

    public Map<DataBaseTable, DataBase> get() {
        return this.map;
    }

    public DataBase get(DataBaseTable dataBaseTable) {
        return this.map.get(dataBaseTable);
    }

}
