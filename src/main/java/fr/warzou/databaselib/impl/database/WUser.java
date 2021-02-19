package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.impl.error.database.IncompatibleUserHostException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class WUser implements User {

    private final Collection<Database> linkedDatabases;

    private final String username;
    private final String password;
    private final String host;

    public WUser(String username, String password, String host) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.linkedDatabases = new ArrayList<>();
    }

    protected void linkDatabase(Database database) throws IncompatibleUserHostException {
        if (!database.getHost().equals(this.host))
            throw new IncompatibleUserHostException(this, database);
        if (getDatabase(database.getDatabaseName(), database.getHost()).isPresent())
            return;
        this.linkedDatabases.add(database);
    }

    protected void unlinkDatabase(Database database) {
        if (!getDatabase(database.getDatabaseName(), database.getHost()).isPresent())
            return;
        this.linkedDatabases.remove(database);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Optional<Database> getDatabase(String name, String host) {
        return this.linkedDatabases.stream()
                .filter(database -> database.getDatabaseName().equals(name) && database.getHost().equals(host))
                .findFirst();
    }

    public Collection<Database> getLinkedDatabases() {
        return this.linkedDatabases;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        return ((User) object).getUsername().equals(this.username) && ((User) object).getPassword().equals(this.password)
                && ((User) object).getHost().equals(this.host);
    }
}