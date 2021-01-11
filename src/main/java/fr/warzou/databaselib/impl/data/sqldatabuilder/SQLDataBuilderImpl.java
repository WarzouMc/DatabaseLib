package fr.warzou.databaselib.impl.data.sqldatabuilder;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.builder.SQLDataBuilder;
import fr.warzou.databaselib.dbl.data.sql.SQLData;
import org.jetbrains.annotations.Nullable;

public class SQLDataBuilderImpl<V, T extends Data<V>> implements SQLDataBuilder<V, T> {

    protected final Class<V> valueType;
    protected final T data;
    protected final String name;

    protected boolean primaryKey;
    protected boolean uniqueKey;
    protected boolean nullable;
    protected boolean autoIncrement;

    protected V defaultValue;

    protected int defaultValueOption;

    public SQLDataBuilderImpl(Class<V> valueType, T data, String name) {
        this.valueType = valueType;
        this.data = data;
        this.name = name;
    }

    @Override
    public SQLDataBuilder<V, T> withPrimaryKey(boolean primaryKey) {
        return null;
    }

    @Override
    public SQLDataBuilder<V, T> withUniqueKey(boolean uniqueKey) {
        return null;
    }

    @Override
    public SQLDataBuilder<V, T> withDefaultValue(@Nullable V defaultValue) {
        return null;
    }

    @Override
    public SQLDataBuilder<V, T> withDefaultValueOption(int defaultValueOption) {
        return null;
    }

    @Override
    public SQLDataBuilder<V, T> withNullable(boolean nullable) {
        return null;
    }

    @Override
    public SQLDataBuilder<V, T> withAutoIncremented(boolean autoIncremented) {
        return null;
    }

    @Override
    public SQLData<V, T> build() {
        return null;
    }

}
