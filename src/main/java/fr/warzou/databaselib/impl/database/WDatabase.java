package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.DatabaseLib;
import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.impl.error.database.IncompatibleUserHostException;
import fr.warzou.databaselib.impl.error.database.NoConnectedDatabaseException;
import fr.warzou.databaselib.impl.error.database.TooManyConnectionOnDatabaseException;
import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;
import fr.warzou.databaselib.impl.logger.DatabaseLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class WDatabase extends DatabaseLogger implements Database {

    private User user;
    private final String name;
    private final String host;
    private final String serverTimeZone;
    private boolean async;
    private boolean disabled = false;
    private final Driver driver;

    private final DatabaseLib session;

    private final HashMap<UUID, DatabaseTable> tables;
    private DatabaseInteraction databaseInteraction;
    private final DatabaseManager databaseManager;

    public WDatabase(DatabaseLib session, User user, String name, String host, Driver driver) throws IncompatibleUserHostException,
            NonRegisteredUserException {
        this(session, user, name, host, "UTC", true, driver);
    }

    public WDatabase(DatabaseLib session, User user, String name, String host, String serverTimeZone, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        this(session, user, name, host, serverTimeZone, true, driver);
    }

    public WDatabase(DatabaseLib session, User user, String name, String host, boolean async, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        this(session, user, name, host, "UTC", async, driver);
    }

    public WDatabase(DatabaseLib session, User user, String name, String host, String serverTimeZone, boolean async, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        super(user, name, host);
        if (!session.getUsersManager().getUser(user.getUsername(), user.getHost()).isPresent())
            throw new NonRegisteredUserException(user.getUsername(), user.getHost());
        this.user = user;
        this.name = name;
        this.host = host;
        this.serverTimeZone = serverTimeZone;
        this.async = async;
        this.tables = new HashMap<>();
        this.driver = driver;

        this.session = session;
        this.databaseManager = this.session.getDatabaseManager();

        WUser wUser = (WUser) this.user;
        wUser.linkDatabase(this);

        logMessage(new LogRecord(Level.INFO, "Ready to use the database '" + name + "' (" + this.host + ")"));
        logMessage(new LogRecord(Level.INFO, "Associate with user '" + this.user.getUsername() + "'"));
    }

    @Override
    public void connect() throws TooManyConnectionOnDatabaseException {
        if (this.disabled)
            return;
        logMessage(new LogRecord(Level.INFO, "Try connection..."));
        this.databaseManager.connectDatabase(this);

        this.databaseInteraction = new DatabaseInteraction(this);
        this.databaseInteraction.connect();
    }

    @Override
    public void disconnect() throws NoConnectedDatabaseException {
        if (this.disabled)
            return;
        logMessage(new LogRecord(Level.INFO, "Disconnection task..."));
        if (this.databaseInteraction == null || !this.databaseInteraction.isConnect())
            throw new NoConnectedDatabaseException(this);
        this.databaseManager.disconnectDatabase(this);

        this.databaseInteraction.disconnect();
        this.databaseInteraction = null;
    }

    @Override
    public <T> T customQuery(String query, Class<T> t) {
        return this.databaseInteraction.query(query, t);
    }

    @Override
    public void printMessage(String message) {
        logMessage(new LogRecord(Level.INFO, message));
    }

    @Override
    public DatabaseTable setupTable(String name) {
        return null;
    }

    @Override
    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public String getDatabaseName() {
        return this.name;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Collection<DatabaseTable> getTables() {
        return this.tables.values();
    }

    @Override
    public String getServerTimeZone() {
        return this.serverTimeZone;
    }

    @Override
    public boolean isAsync() {
        return this.async;
    }

    @Override
    public Driver getDriver() {
        return this.driver;
    }

    public void makeUnusable() {
        if (isConnected()) {
            try {
                disconnect();
            } catch (NoConnectedDatabaseException exception) {
                exception.printStackTrace();
            }
        }
        ((WUser) this.user).unlinkDatabase(this);
        this.user = null;
        this.disabled = true;
        close();
    }

    public final String getUniqueName() {
        if (this.disabled)
            return Math.round(Math.random() * 100000.0f) + "";
        return this.host + "::" + this.name + "::" + this.user.getUsername();
    }

    public final DatabaseLib getSession() {
        return this.session;
    }

    protected boolean isConnected() {
        return this.databaseManager.getDatabase(getUniqueName(), this.host).isPresent();
    }

    @Override
    protected void close() {
        super.close();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        WDatabase wDatabase = (WDatabase) object;
        return wDatabase.getUniqueName().equals(getUniqueName());
    }
}
