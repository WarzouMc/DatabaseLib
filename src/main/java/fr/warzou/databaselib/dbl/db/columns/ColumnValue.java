package fr.warzou.databaselib.dbl.db.columns;

public interface ColumnValue<T> {

    //key
    <T1> UnregisteredCell<T1> getFixation();

    //value
    T getValue();

}
