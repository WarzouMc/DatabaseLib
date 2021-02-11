package fr.warzou.databaselib.utils;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.ColumnBuilder;
import fr.warzou.databaselib.dbl.db.table.DatabaseTableBuilder;
import fr.warzou.databaselib.impl.database.manager.builder.WColumnBuilder;
import fr.warzou.databaselib.impl.database.manager.builder.WDatabaseTableBuilder;

public final class DatabaseUtils {

    public static DatabaseTableBuilder createDatabaseTableBuilder(String tableName) {
        return new WDatabaseTableBuilder(tableName);
    }

    public static <T extends Data<?>> ColumnBuilder<T> createColumnBuilder(String columnName, Class<T> type) {
        return new WColumnBuilder<>(columnName, type);
    }

}
