package fr.warzou.databaselib.dbl.db.columns;

public interface UnregisteredCell<T> {

    Column<T> ofColumn();

    T ofValue();

}
