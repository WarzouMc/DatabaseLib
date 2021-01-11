package fr.warzou.databaselib.dbl.db.table;

import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.columns.UnregisteredCell;
import fr.warzou.databaselib.dbl.db.database.Database;

public interface DatabaseTable {

    <T> UnregisteredCell<T> getCell(Column<T> column, T value);

    Database getDatabase();

}
