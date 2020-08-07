package fr.warzou.databaselib.information.save;

import java.util.LinkedList;

/**
 * Log all tables save information
 * @author WarzouMc
 * @version 0.0.2
 * @since 0.0.2
 */
public class DatabaseFullSaveInformation {

    /**
     * This is the time of the save as took
     */
    private double timeTake;
    /**
     * List of all table save log in {@link DatabaseTableSaveInformation}
     */
    private LinkedList<DatabaseTableSaveInformation> databaseTableSaveInformation = new LinkedList<>();

    /**
     * Construct a new {@link DatabaseFullSaveInformation}
     * @since 1.1.0
     */
    public DatabaseFullSaveInformation() {}

    /**
     * Calculate how many time the save has took
     * @param start when save started
     * @param end when save ended
     * @since 0.0.2
     */
    public void setTimeTake(long start, long end) {
        this.timeTake = (end - start) / 1000.0;
    }

    /**
     * Add table save log.
     * @param dataBaseTableSaveInformation table save log in {@link DatabaseTableSaveInformation}
     * @since 0.0.2
     */
    public void addTableSave(DatabaseTableSaveInformation dataBaseTableSaveInformation) {
        this.databaseTableSaveInformation.add(dataBaseTableSaveInformation);
    }

    /**
     * Get deletion total count
     * @return deletion count
     * @since 0.0.2
     */
    public int getTotalDeletion() {
        return getDatabaseTableSaveInformation().stream().mapToInt(DatabaseTableSaveInformation::getDeletion).sum();
    }

    /**
     * Get addition total count
     * @return addition count
     * @since 0.0.2
     */
    public int getTotalAddition() {
        return getDatabaseTableSaveInformation().stream().mapToInt(DatabaseTableSaveInformation::getAddition).sum();
    }

    /**
     * Get modification total count
     * @return modification count
     * @since 0.0.2
     */
    public int getTotalModification() {
        return getDatabaseTableSaveInformation().stream().mapToInt(DatabaseTableSaveInformation::getModification).sum();
    }

    /**
     * Get how many time the save has took
     * @return how many time the save has took
     * @since 0.0.2
     */
    public double getTimeTake() {
        return this.timeTake;
    }

    /**
     * Get all table save log in {@link DatabaseTableSaveInformation}
     * @return {@link LinkedList} of all table save log
     * @since 0.0.2
     */
    public LinkedList<DatabaseTableSaveInformation> getDatabaseTableSaveInformation() {
        return this.databaseTableSaveInformation;
    }
}
