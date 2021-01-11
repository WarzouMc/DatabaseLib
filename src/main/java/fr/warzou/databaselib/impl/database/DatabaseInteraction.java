package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.impl.database.interaction.AbstractDatabaseInteraction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInteraction extends AbstractDatabaseInteraction {

    private final Database database;
    private boolean connect = false;

    protected DatabaseInteraction(Database database) {
        super(database);
        this.database = database;
    }

    @Override
    protected void connect() {
        if (this.database.isAsync()) {
            ExecutorService executorService = getExecutorService();
            executorService.submit(() -> {
                Thread.currentThread().setName("DBL::" + ((WDatabase) this.database).getUniqueName() + "--connector_thread::" + Math.round(Math.random() * 100000));
                super.connect();
                this.connect = true;
            });
            executorService.shutdown();
        } else {
            super.connect();
            this.connect = true;
        }
    }

    @Override
    protected void disconnect() {
        if (this.database.isAsync()) {
            ExecutorService executorService = getExecutorService();
            executorService.submit(() -> {
                Thread.currentThread().setName("DBL::" + ((WDatabase) this.database).getUniqueName() + "--disconnector_thread::" + Math.round(Math.random() * 100000));
                super.disconnect();
                this.connect = false;
            });
            executorService.shutdown();
        } else {
            super.disconnect();
            this.connect = false;
        }
    }

    @Override
    protected <T> T query(String sql, Class<T> clazz) {
        return super.query(sql, clazz);
    }

    private ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    protected boolean isConnect() {
        return this.connect;
    }
}
