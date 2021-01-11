package fr.warzou.databaselib.impl.error.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.user.User;

public class TooManyConnectionOnDatabaseException extends DatabaseException {

    public TooManyConnectionOnDatabaseException(User who, User alreadyConnect, Database target) {
        super(target, who.equals(alreadyConnect) ? "'" + who.getUsername() + "' account is already connect on '" + target.getDatabaseName() + ":" + target.getHost() + "'"
                : "'" + alreadyConnect.getUsername() + "' is already connected on '" + target.getDatabaseName()  + ":" + target.getHost() + "'" +
                ". Disconnect this user if you want to connect '" + who.getUsername() + "'");
    }

}
