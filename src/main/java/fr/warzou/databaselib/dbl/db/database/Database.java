package fr.warzou.databaselib.dbl.db.database;

import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.impl.error.database.NoConnectedDatabaseException;
import fr.warzou.databaselib.impl.error.database.TooManyConnectionOnDatabaseException;

import java.util.Collection;

public interface Database {

    void connect() throws TooManyConnectionOnDatabaseException;

    void disconnect() throws NoConnectedDatabaseException;

    <T> T customQuery(String query, Class<T> returnType);

    void printMessage(String message);

    DatabaseTable setupTable(String name);

    String getDatabaseName();

    User getUser();

    String getHost();

    String getServerTimeZone();

    Collection<DatabaseTable> getTables();

    void setAsync(boolean async);

    boolean isAsync();

    Driver getDriver();

}
