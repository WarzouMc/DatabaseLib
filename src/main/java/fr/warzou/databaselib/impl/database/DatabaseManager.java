package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.impl.error.database.NoConnectedDatabaseException;
import fr.warzou.databaselib.impl.error.database.TooManyConnectionOnDatabaseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class DatabaseManager {

    private final Collection<Database> databases = new ArrayList<>();

    protected void connectDatabase(Database database) throws TooManyConnectionOnDatabaseException {
        Optional<Database> databaseOptional = getDatabase(((WDatabase) database).getUniqueName(), database.getHost());
        if (databaseOptional.isPresent())
            throw new TooManyConnectionOnDatabaseException(database.getUser(), databaseOptional.get().getUser(), database);

        this.databases.add(database);
    }

    protected void disconnectDatabase(Database database) throws NoConnectedDatabaseException {
        Optional<Database> databaseOptional = getDatabase(((WDatabase) database).getUniqueName(), database.getHost());
        if (!databaseOptional.isPresent())
            throw new NoConnectedDatabaseException(database);
        Database linked = databaseOptional.get();
        if (!linked.getUser().getUsername().equals(database.getUser().getUsername()))
           throw new NoConnectedDatabaseException(database, database.getUser());

        this.databases.remove(database);
    }

    protected Optional<Database> getDatabase(String name, String host) {
        return this.databases.stream().filter(database -> ((WDatabase) database).getUniqueName().equals(name)
                && database.getHost().equals(host)).findFirst();
    }

}
