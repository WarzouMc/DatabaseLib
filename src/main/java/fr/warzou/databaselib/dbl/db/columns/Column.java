package fr.warzou.databaselib.dbl.db.columns;

import fr.warzou.databaselib.dbl.data.Data;

public interface Column<T extends Data<?>> {

    String getName();

    T getData();

    String getEncode();

    String getComment();

    boolean isPrimaryKey();

    boolean isUnique();

    boolean isAutoIncrement();

}
