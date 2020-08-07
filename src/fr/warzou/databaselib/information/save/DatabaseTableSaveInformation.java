package fr.warzou.databaselib.information.save;

import fr.warzou.databaselib.tables.DatabaseTablesRegister;

/**
 * Save log of one table
 * @author Warzou
 * @version 0.0.2
 * @since 0.0.2
 */
public class DatabaseTableSaveInformation {

    /**
     * Deletion count
     */
    private int deletion;
    /**
     * Addition count
     */
    private int addition;
    /**
     * Modification count
     */
    private int modification;
    /**
     * How many time the table has took to be update
     */
    private double timeTake;

    /**
     * Associate {@link DatabaseTablesRegister}
     */
    private DatabaseTablesRegister databaseTablesRegister;

    /**
     * Construct a new {@link DatabaseTableSaveInformation}
     * @param databaseTablesRegister associate {@link DatabaseTablesRegister}
     * @since 0.0.2
     */
    public DatabaseTableSaveInformation(DatabaseTablesRegister databaseTablesRegister) {
        this.databaseTablesRegister = databaseTablesRegister;
    }

    /**
     * Set deletion count
     * @param deletion count
     * @since 0.0.2
     */
    public void setDeletion(int deletion) {
        this.deletion = deletion;
    }

    /**
     * Set addition count
     * @param addition count
     * @since 0.0.2
     */
    public void setAddition(int addition) {
        this.addition = addition;
    }

    /**
     * Set modification count
     * @param modification count
     * @since 0.0.2
     */
    public void setModification(int modification) {
        this.modification = modification;
    }

    /**
     * Set how many time the save has took for this table
     * @param start when the save has started
     * @param end when the save has ended
     * @since 0.0.2
     */
    public void setTimeTake(long start, long end) {
        this.timeTake = (end - start) / 1000.0;
    }

    /**
     * Get how many deletion were made
     * @return deletion count
     * @since 0.0.2
     */
    public int getDeletion() {
        return this.deletion;
    }

    /**
     * Get how many addition were made
     * @return addition count
     * @since 0.0.2
     */
    public int getAddition() {
        return this.addition;
    }

    /**
     * Get how many modification were made
     * @return modification count
     * @since 0.0.2
     */
    public int getModification() {
        return this.modification;
    }

    /**
     * Get how many time the save of this table has took
     * @return save time
     * @since 0.0.2
     */
    public double getTimeTake() {
        return this.timeTake;
    }

    /**
     * Get associate {@link DatabaseTablesRegister}
     * @return associate {@link DatabaseTablesRegister}
     * @since 0.0.2
     */
    public DatabaseTablesRegister getAbstractDatabaseTables() {
        return this.databaseTablesRegister;
    }
}
