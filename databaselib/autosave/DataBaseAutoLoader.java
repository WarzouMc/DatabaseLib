package databaselib.autosave;

import databaselib.DataBase;
import databaselib.tables.DataBaseTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

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
        System.out.println("Initialisation of your database with DataBaseLib v_0.0.1 by Warzou");
        Arrays.asList(DataBaseTable.values()).forEach(dataBaseTable -> {
            DataBase dataBase = new DataBase(this.host, this.user, this.password, this.name, dataBaseTable);
            this.map.put(dataBaseTable, dataBase);
        });
        return this.map;
    }

    public Map<DataBaseTable, DataBase> load(long save) {
        this.map.forEach((dataBaseTable1, dataBase) -> dataBase.load());
        Timer timer = new Timer();
        if (save > 0) {
            timer.schedule(new DataBaseAutoSave(this), save, save);
            return this.map;
        }
        timer.schedule(new DataBaseAutoSave(this), 5000 * 60, 5000 * 60);
        return this.map;
    }

    Map<DataBaseTable, DataBase> save() {
        this.map.forEach((dataBaseTable1, dataBase) -> dataBase.save());
        return this.map;
    }

    public Map<DataBaseTable, DataBase> get() {
        return this.map;
    }

}
