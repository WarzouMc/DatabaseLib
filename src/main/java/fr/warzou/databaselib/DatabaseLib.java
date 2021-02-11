package fr.warzou.databaselib;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.drivers.DriverManager;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.dbl.user.UsersManager;
import fr.warzou.databaselib.events.DatabaseEventManager;
import fr.warzou.databaselib.impl.database.DatabaseManager;
import fr.warzou.databaselib.impl.database.WDatabase;
import fr.warzou.databaselib.impl.database.manager.WUsersManager;
import fr.warzou.databaselib.impl.drivers.SimpleDriverManager;
import fr.warzou.databaselib.impl.error.database.IncompatibleUserHostException;
import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;
import fr.warzou.databaselib.impl.event.SimpleDatabaseEventManager;
import fr.warzou.databaselib.utils.DatabaseLibSessionAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DatabaseLib {

    private static final DatabaseEventManager publicDatabaseEventManager = new SimpleDatabaseEventManager();
    private static final UsersManager publicUsersManager = new WUsersManager();
    private static final DriverManager publicDriverManager = new SimpleDriverManager();
    private static final Map<UUID, DatabaseLib> publicSessions = new HashMap<>();
    private static final DatabaseManager publicDatabaseManager = new DatabaseManager();


    private final DatabaseLibSessionAccess accessType;
    private final UUID sessionID;
    private final DatabaseEventManager databaseEventManager;
    private final UsersManager usersManager;
    private final DriverManager driverManager;
    private final DatabaseManager databaseManager;

    public DatabaseLib(DatabaseLibSessionAccess databaseLibSessionAccess) {
        this.accessType = databaseLibSessionAccess;
        UUID currentIDGeneration;

        do currentIDGeneration = UUID.randomUUID();
        while (publicSessions.containsKey(currentIDGeneration));

        this.sessionID = currentIDGeneration;        //this is not a fix uuid

        if (this.accessType == DatabaseLibSessionAccess.PUBLIC) {
            publicSessions.put(this.sessionID, this);
            this.databaseEventManager = publicDatabaseEventManager;
            this.usersManager = publicUsersManager;
            this.driverManager = publicDriverManager;
            this.databaseManager = publicDatabaseManager;
            return;
        }

        this.databaseEventManager = new SimpleDatabaseEventManager();
        this.usersManager = new WUsersManager();
        this.driverManager = new SimpleDriverManager();
        this.databaseManager = new DatabaseManager();
    }

    public DatabaseLibSessionAccess getAccessType() {
        return this.accessType;
    }

    public UUID getSessionID() {
        return this.sessionID;
    }

    public Database addDatabase(User user, String databaseName, String host, Driver driver) throws IncompatibleUserHostException,
            NonRegisteredUserException {
        return new WDatabase(this, user, databaseName, host, driver);
    }

    public Database addDatabase(User user, String databaseName, String host, boolean async, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        return new WDatabase(this, user, databaseName, host, async, driver);
    }

    public Database addDatabase(User user, String databaseName, String host, String serverTimeZone, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        return new WDatabase(this, user, databaseName, host, serverTimeZone, driver);
    }

    public Database addDatabase(User user, String databaseName, String host, String serverTimeZone, boolean async, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        return new WDatabase(this, user, databaseName, host, serverTimeZone, async, driver);
    }

    public void removeDatabase(Database database) {
        ((WDatabase) database).makeUnusable();
    }

    public DatabaseEventManager getDatabaseEventManager() {
        return this.databaseEventManager;
    }

    public UsersManager getUsersManager() {
        return this.usersManager;
    }

    public DriverManager getDriverManager() {
        return this.driverManager;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    private static Optional<DatabaseLib> getPublicSession(UUID sessionID) {
        if (!publicSessions.containsKey(sessionID))
            return Optional.empty();
        return Optional.of(publicSessions.get(sessionID));
    }

}
