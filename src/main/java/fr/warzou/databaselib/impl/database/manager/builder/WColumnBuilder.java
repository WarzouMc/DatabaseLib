package fr.warzou.databaselib.impl.database.manager.builder;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.sql.SQLData;
import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.dbl.db.columns.ColumnBuilder;
import fr.warzou.databaselib.impl.database.WColumn;

public class WColumnBuilder<T extends Data<?>> implements ColumnBuilder<T> {

    private final String name;

    private T t;
    private final Class<T> clazz;

    public WColumnBuilder(String name, Class<T> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public ColumnBuilder<T> withPrimaryKey(boolean primaryKey) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withUniqueKey(boolean uniqueKey) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withSize(int size) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withSqlType(T type) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withSqlType(String type) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withDefaultValue(T value) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withDefaultValueOption(int type) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withNullable(boolean nullable) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withAutoIncrementation(boolean autoIncrementation) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withComment(String comment) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withAttribute(int attribute) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withVirtuality(int virtuality, String expression) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withEncode(String encode) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withEncode(String encode, String collate) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withFormat(int format) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withEngine(String engineName) {
        return this;
    }

    @Override
    public ColumnBuilder<T> withSQLData(SQLData<?, T> consumer) {
        return null;
    }

    @Override
    public Column<T> build() {
        return new WColumn<>(null, true, true, null, 5, this.t, true);
    }

}
