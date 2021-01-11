package fr.warzou.databaselib.events.event;

import fr.warzou.databaselib.dbl.db.database.Database;

public abstract class DatabaseEvent {

    private final Database database;

    protected DatabaseEvent(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return this.database;
    }
}
