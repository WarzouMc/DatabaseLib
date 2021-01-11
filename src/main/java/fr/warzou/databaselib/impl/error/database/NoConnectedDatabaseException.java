package fr.warzou.databaselib.impl.error.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.impl.database.WDatabase;

public class NoConnectedDatabaseException extends DatabaseException {

    public NoConnectedDatabaseException(Database database) {
        super(database, "'" + ((WDatabase) database).getUniqueName() + "' is no connected");
    }

    public NoConnectedDatabaseException(Database database, User user) {
        super(database, "The user '" + user.getUsername() + "' is no connected on '" + ((WDatabase) database).getUniqueName() + "'");
    }

}
