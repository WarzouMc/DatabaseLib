package databaselib.autosave;

import java.util.TimerTask;

class DatabaseAutoSave extends TimerTask {

    private DatabaseAutoLoader databaseAutoLoad;
    DatabaseAutoSave(DatabaseAutoLoader databaseAutoLoad) {
        this.databaseAutoLoad = databaseAutoLoad;
    }

    @Override
    public void run() {
        System.err.println("Run auto save");
        this.databaseAutoLoad.save();
    }
}
