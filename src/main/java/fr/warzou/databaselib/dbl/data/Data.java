package fr.warzou.databaselib.dbl.data;

import fr.warzou.databaselib.dbl.data.builder.SQLDataBuilder;

public interface Data<T> {

    Class<T> getType();

    String sqlName();

    GlobalDataType getGlobalType();

    SQLDataBuilder<T, ? extends Data<T>> createSQLData();

}
