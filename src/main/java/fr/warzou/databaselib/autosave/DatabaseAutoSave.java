package fr.warzou.databaselib.autosave;

import java.util.TimerTask;

/**
 * Create a auto save every x time
 * @author Warzou
 * @version 0.0.1
 * @since 0.0.1
 */
class DatabaseAutoSave extends TimerTask {

    /**
     * Instance of {@link DatabaseAutoLoader}, use to call {@link DatabaseAutoLoader#save()}
     */
    private DatabaseAutoLoader databaseAutoLoader;

    /**
     * Construct a new {@link DatabaseAutoSave}
     * @param databaseAutoLoader instance of {@link DatabaseAutoLoader}
     * @since 0.0.1
     */
    DatabaseAutoSave(DatabaseAutoLoader databaseAutoLoader) {
        this.databaseAutoLoader = databaseAutoLoader;
    }

    /**
     * "run" method (from {@link TimerTask#run()}
     * <p>Execute every x time for start a save process.</p>
     */
    @Override
    public void run() {
        System.err.println("Run auto save");
        this.databaseAutoLoader.save();
    }
}
