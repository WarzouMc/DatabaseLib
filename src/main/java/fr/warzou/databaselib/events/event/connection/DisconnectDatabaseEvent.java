package fr.warzou.databaselib.events.event.connection;

import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.dbl.db.database.Database;

public class DisconnectDatabaseEvent extends DatabaseEvent {

    public DisconnectDatabaseEvent(Database database) {
        super(database);
    }
}
