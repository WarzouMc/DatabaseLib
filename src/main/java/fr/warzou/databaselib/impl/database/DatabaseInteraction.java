package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.dbl.async.ThreadPool;
import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.impl.async.QueuedThreadPool;
import fr.warzou.databaselib.impl.database.interaction.AbstractDatabaseInteraction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseInteraction extends AbstractDatabaseInteraction {

    private final Database database;
    private boolean connect = false;
    private final ThreadPool threadPool;

    protected DatabaseInteraction(Database database) {
        super(database);
        this.database = database;
        this.threadPool = new QueuedThreadPool();
    }

    @Override
    protected void connect() {
        if (this.database.isAsync()) {
            this.threadPool.perform(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.connect();
                this.connect = true;
            });
            return;
        }
        super.connect();
        this.connect = true;
    }

    @Override
    protected void disconnect() {
        if (this.database.isAsync()) {
            this.threadPool.perform(() -> {
                super.disconnect();
                this.connect = false;
            });
            return;
        }
        super.disconnect();
        this.connect = false;
    }

    @Override
    protected <T> T query(String sql, Class<T> clazz) {
        if (this.database.isAsync()) {
            return this.threadPool.perform(() -> super.query(sql, clazz)).get();
        }
        return super.query(sql, clazz);
    }

    private ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    protected boolean isConnect() {
        return this.connect;
    }
}
