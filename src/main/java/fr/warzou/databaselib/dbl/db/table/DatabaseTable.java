package fr.warzou.databaselib.dbl.db.table;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.columns.UnregisteredCell;
import fr.warzou.databaselib.dbl.db.database.Database;

public interface DatabaseTable {

    <T extends Data<?>> UnregisteredCell<T> getCell(Column<T> column, T value);

    Database getDatabase();

}
