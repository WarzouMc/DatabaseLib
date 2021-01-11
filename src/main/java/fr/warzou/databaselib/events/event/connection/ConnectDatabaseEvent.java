package fr.warzou.databaselib.events.event.connection;

import fr.warzou.databaselib.events.event.DatabaseEvent;
import fr.warzou.databaselib.dbl.db.database.Database;

public class ConnectDatabaseEvent extends DatabaseEvent {

    public ConnectDatabaseEvent(Database database) {
        super(database);
    }
}
