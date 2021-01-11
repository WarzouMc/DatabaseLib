package fr.warzou.databaselib;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.ColumnBuilder;
import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.db.table.DatabaseTableBuilder;
import fr.warzou.databaselib.dbl.drivers.Driver;
import fr.warzou.databaselib.dbl.drivers.DriverManager;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.dbl.user.UsersManager;
import fr.warzou.databaselib.events.DatabaseEventManager;
import fr.warzou.databaselib.impl.database.WDatabase;
import fr.warzou.databaselib.impl.database.manager.WUsersManager;
import fr.warzou.databaselib.impl.database.manager.builder.WColumnBuilder;
import fr.warzou.databaselib.impl.database.manager.builder.WDatabaseTableBuilder;
import fr.warzou.databaselib.impl.drivers.SimpleDriverManager;
import fr.warzou.databaselib.impl.error.database.IncompatibleUserHostException;
import fr.warzou.databaselib.impl.error.user.NonRegisteredUserException;
import fr.warzou.databaselib.impl.event.SimpleDatabaseEventManager;

public final class DatabaseLib {

    private static final DatabaseEventManager databaseEventManager = new SimpleDatabaseEventManager();
    private static final UsersManager usersManager = new WUsersManager();
    private static final DriverManager driverManager = new SimpleDriverManager();

    public static DatabaseEventManager getEventManager() {
        return databaseEventManager;
    }

    public static Database addDatabase(User user, String databaseName, String host, Driver driver) throws IncompatibleUserHostException,
            NonRegisteredUserException {
        return new WDatabase(user, databaseName, host, driver);
    }

    public static Database addDatabase(User user, String databaseName, String host, boolean async, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        return new WDatabase(user, databaseName, host, async, driver);
    }

    public static Database addDatabase(User user, String databaseName, String host, String serverTimeZone, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        return new WDatabase(user, databaseName, host, serverTimeZone, driver);
    }

    public static Database addDatabase(User user, String databaseName, String host, String serverTimeZone, boolean async, Driver driver)
            throws IncompatibleUserHostException, NonRegisteredUserException {
        return new WDatabase(user, databaseName, host, serverTimeZone, async, driver);
    }

    public static void removeDatabase(Database database) {
        ((WDatabase) database).makeUnusable();
    }

    public static DatabaseTableBuilder createDatabaseTableBuilder(String tableName) {
        return new WDatabaseTableBuilder(tableName);
    }

    public static <T extends Data<?>> ColumnBuilder<T> createColumnBuilder(String columnName, Class<T> type) {
        return new WColumnBuilder<>(columnName, type);
    }

    public static UsersManager getUsersManager() {
        return usersManager;
    }

    public static DriverManager getDriverManager() {
        return driverManager;
    }
}
