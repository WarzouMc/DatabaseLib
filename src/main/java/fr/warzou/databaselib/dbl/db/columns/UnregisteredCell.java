package fr.warzou.databaselib.dbl.db.columns;

import fr.warzou.databaselib.dbl.data.Data;

public interface UnregisteredCell<T extends Data<?>> {

    Column<T> ofColumn();

    T ofData();

}
