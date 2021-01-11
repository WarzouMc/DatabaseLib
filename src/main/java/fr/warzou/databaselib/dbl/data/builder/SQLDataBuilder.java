package fr.warzou.databaselib.dbl.data.builder;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.sql.SQLData;
import org.jetbrains.annotations.Nullable;

public interface SQLDataBuilder<V, T extends Data<V>> {

    SQLDataBuilder<V, T> withPrimaryKey(boolean primaryKey);

    SQLDataBuilder<V, T> withUniqueKey(boolean uniqueKey);

    SQLDataBuilder<V, T> withDefaultValue(@Nullable V defaultValue);

    SQLDataBuilder<V, T> withDefaultValueOption(int defaultValueOption);

    SQLDataBuilder<V, T> withNullable(boolean nullable);

    SQLDataBuilder<V, T> withAutoIncremented(boolean autoIncremented);

    SQLData<V, T> build();

}
