package fr.warzou.databaselib.impl.database.manager.builder;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;
import fr.warzou.databaselib.dbl.db.table.DatabaseTableBuilder;

import java.util.LinkedList;

public class WDatabaseTableBuilder implements DatabaseTableBuilder {

    private final String name;

    public WDatabaseTableBuilder(String name) {
        this.name = name;
    }

    @Override
    public DatabaseTableBuilder setName(String name) {
        return null;
    }

    @Override
    public <T extends Data<?>> DatabaseTableBuilder addColumn(Column<T> column) {
        return this;
    }

    @Override
    public DatabaseTableBuilder addColumns(LinkedList<Column<?>> columns) {
        return this;
    }

    @Override
    public DatabaseTableBuilder setTemporary(boolean temporary) {
        return this;
    }

    @Override
    public DatabaseTableBuilder createIfNotExist(boolean createIfNotExist) {
        return null;
    }

    @Override
    public DatabaseTableBuilder setValueLivingTime(long livingTime) {
        return this;
    }

    @Override
    public DatabaseTable build() {
        return null;
    }
}
