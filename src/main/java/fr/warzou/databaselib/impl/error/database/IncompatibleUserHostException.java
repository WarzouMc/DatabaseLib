package fr.warzou.databaselib.impl.error.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.user.User;

public class IncompatibleUserHostException extends DatabaseException {

    public IncompatibleUserHostException(User user, Database database) {
        super(database, "'" + user.getUsername() + "' user cannot be link with '" + database.getDatabaseName() + "'. " +
                "(" + user.getHost() + " != " + database.getHost() + ")");
    }

}
