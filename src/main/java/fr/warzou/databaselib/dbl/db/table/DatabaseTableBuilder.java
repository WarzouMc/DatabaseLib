package fr.warzou.databaselib.dbl.db.table;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.db.columns.Column;

import java.util.LinkedList;

public interface DatabaseTableBuilder {

    DatabaseTableBuilder setName(String name);

    <T extends Data<?>> DatabaseTableBuilder addColumn(Column<T> column);

    DatabaseTableBuilder addColumns(LinkedList<Column<?>> columns);

    DatabaseTableBuilder setTemporary(boolean temporary);

    DatabaseTableBuilder createIfNotExist(boolean createIfNotExist);

    DatabaseTableBuilder setValueLivingTime(long livingTime);

    DatabaseTable build();

}
