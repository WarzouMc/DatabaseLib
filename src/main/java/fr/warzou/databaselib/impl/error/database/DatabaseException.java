package fr.warzou.databaselib.impl.error.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.impl.database.WDatabase;

public class DatabaseException extends Exception {

    private final WDatabase database;

    public DatabaseException(Database database, String message) {
        super(message);
        this.database = (WDatabase) database;
    }

    @Override
    public void printStackTrace() {
        this.database.logError(this);
    }
}
