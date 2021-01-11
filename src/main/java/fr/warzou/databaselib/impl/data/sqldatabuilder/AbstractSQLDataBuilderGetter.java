package fr.warzou.databaselib.impl.data.sqldatabuilder;

import fr.warzou.databaselib.dbl.data.Data;

import java.util.Optional;

public abstract class AbstractSQLDataBuilderGetter {

    public static <V, T extends Data<V>, N extends Number, M extends Number> Optional<SQLLengthDataBuilderImpl<V, T, N, M>> asLength(SQLDataBuilderImpl<V, T> base, N n, M m) {
        if (!SQLLengthDataBuilderImpl.accept(base.data.getGlobalType()))
            return Optional.empty();

        return Optional.of(new SQLLengthDataBuilderImpl<>(base, n, m));
    }

}
