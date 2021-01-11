package fr.warzou.databaselib.dbl.db.columns;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.sql.SQLData;

public interface ColumnBuilder<T extends Data<?>> {

    ColumnBuilder<T> withPrimaryKey(boolean primaryKey);

    ColumnBuilder<T> withUniqueKey(boolean uniqueKey);

    ColumnBuilder<T> withSize(int size);

    ColumnBuilder<T> withSqlType(T type);

    ColumnBuilder<T> withSqlType(String type);

    ColumnBuilder<T> withDefaultValue(T value);

    ColumnBuilder<T> withDefaultValueOption(int type);

    ColumnBuilder<T> withNullable(boolean nullable);

    ColumnBuilder<T> withAutoIncrementation(boolean autoIncrementation);

    ColumnBuilder<T> withComment(String comment);

    ColumnBuilder<T> withAttribute(int attribute);

    ColumnBuilder<T> withVirtuality(int virtuality, String expression);

    ColumnBuilder<T> withEncode(String encode);

    ColumnBuilder<T> withEncode(String encode, String collate);

    ColumnBuilder<T> withFormat(int format);

    ColumnBuilder<T> withEngine(String engineName);

    ColumnBuilder<T> withSQLData(SQLData<?, T> f);

    Column<T> build();

}
