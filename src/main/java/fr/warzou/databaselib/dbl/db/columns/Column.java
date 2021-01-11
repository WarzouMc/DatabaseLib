package fr.warzou.databaselib.dbl.db.columns;

import fr.warzou.databaselib.dbl.data.Data;

import java.util.Optional;

public interface Column<T extends Data<?>> {

    String getName();

    Optional<Class<T>> getType();

    Optional<T> getDefault();

    String getEncode();

    boolean isPrimaryKey();

    int getId();

}
