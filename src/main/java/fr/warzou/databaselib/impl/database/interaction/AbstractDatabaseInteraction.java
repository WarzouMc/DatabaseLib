package fr.warzou.databaselib.impl.database.interaction;

import fr.warzou.databaselib.DatabaseLib;
import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.user.User;
import fr.warzou.databaselib.events.event.connection.ConnectDatabaseEvent;
import fr.warzou.databaselib.events.event.connection.DisconnectDatabaseEvent;
import fr.warzou.databaselib.impl.database.WDatabase;
import fr.warzou.databaselib.task.ActionClock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class AbstractDatabaseInteraction {

    private final Database database;
    private Connection connection;
    private long connectSince = -1;

    protected AbstractDatabaseInteraction(Database database) {
        this.database = database;
    }

    protected void connect() {
        ActionClock actionClock = new ActionClock();
        actionClock.start();
        User user = this.database.getUser();
        String url = String.format("%s//%s/%s?autoReconnect=true&useSSL=false&serverTimezone=%s",
                this.database.getDriver().getDriverUrlPart(), this.database.getHost(), this.database.getDatabaseName(),
                this.database.getServerTimeZone());
        try {
            this.connection = DriverManager.getConnection(url, user.getUsername(), user.getPassword());
            this.connectSince = new Date().getTime();
            actionClock.stop();
            getWDatabase().logMessage(new LogRecord(Level.INFO, "Connection success in " + actionClock.toSeconds() + "s"));
            ConnectDatabaseEvent connectDatabaseEvent = new ConnectDatabaseEvent(this.database);
            DatabaseLib.getEventManager().callEvent(connectDatabaseEvent);
        } catch (SQLException exception) {
            getWDatabase().logError(exception);
        }
    }

    protected void disconnect() {
        ActionClock actionClock = new ActionClock();
        actionClock.start();
        try {
            this.connection.close();
            this.connectSince = -1;
            actionClock.stop();
            getWDatabase().logMessage(new LogRecord(Level.INFO, "Connection close in " + actionClock.toSeconds() + "s"));
            DisconnectDatabaseEvent disconnectDatabaseEvent = new DisconnectDatabaseEvent(this.database);
            DatabaseLib.getEventManager().callEvent(disconnectDatabaseEvent);
        } catch (SQLException exception) {
            getWDatabase().logError(exception);
        }
    }

    protected <T> T query(String sql, Class<T> clazz) {
        ActionClock actionClock = new ActionClock();
        actionClock.start();
        AtomicReference<T> atomicReference = new AtomicReference<>();
        Consumer<ResultSet> consumer = resultSet -> {
            try {
                if (resultSet.next())
                    atomicReference.set(resultSet.getObject("email", clazz));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        };

        try {
            ResultSet resultSet = this.connection.createStatement().executeQuery(sql);
            consumer.accept(resultSet);
            actionClock.stop();
            System.out.println(actionClock.toSeconds());
            return atomicReference.get();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private WDatabase getWDatabase() {
        return (WDatabase) this.database;
    }

}
