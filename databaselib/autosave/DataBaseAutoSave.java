package databaselib.autosave;

import java.util.TimerTask;

class DataBaseAutoSave extends TimerTask {

    private DataBaseAutoLoader dataBaseAutoLoad;
    DataBaseAutoSave(DataBaseAutoLoader dataBaseAutoLoad) {
        this.dataBaseAutoLoad = dataBaseAutoLoad;
    }

    @Override
    public void run() {
        System.err.println("Run auto save");
        dataBaseAutoLoad.save();
    }
}
