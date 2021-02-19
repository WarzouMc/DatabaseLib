package fr.warzou.databaselib.impl.query;

import fr.warzou.databaselib.dbl.db.query.Query;
import fr.warzou.databaselib.dbl.db.query.QueryResult;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.drivers.Driver;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public abstract class AbstractQuery implements Query {

    private DatabaseTable databaseTable;
    private QueryResult queryResult;
    private final Driver supportedDriver;

    public AbstractQuery(Driver driver) {
        this.supportedDriver = driver;
    }

    public AbstractQuery(Driver driver, DatabaseTable databaseTable) {
        this.supportedDriver = driver;
        this.databaseTable = databaseTable;
        this.queryResult = null;
    }

    @Override
    public Driver support() {
        return this.supportedDriver;
    }

    @Override
    public boolean isSupport() {
        return false;
    }

    public Optional<AbstractQuery> of(DatabaseTable databaseTable) {
        try {
            return Optional.of(getClass().getDeclaredConstructor(Driver.class, DatabaseTable.class).newInstance(support(), databaseTable));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static <T extends AbstractQuery> Optional<T> createClass(Class<T> clazz, Driver driver) {
        try {
            return Optional.of(clazz.getDeclaredConstructor(Driver.class).newInstance(driver));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
