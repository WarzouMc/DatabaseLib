package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.columns.UnregisteredCell;
import fr.warzou.databaselib.dbl.db.database.Database;
import fr.warzou.databaselib.dbl.db.table.DatabaseTable;

public class WDatabaseTable implements DatabaseTable {

    private final Database database;
    private final String name;

    protected WDatabaseTable(Database database, String name) {
        this.database = database;
        this.name = name;
    }

    @Override
    public <T extends Data<?>> UnregisteredCell<T> getCell(Column<T> column, T data) {
        return new UnregisteredCell<T>() {
            @Override
            public Column<T> ofColumn() {
                return column;
            }

            @Override
            public T ofData() {
                return data;
            }
        };
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }
}
