package fr.warzou.databaselib.impl.database;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.subdata.string.StringData;
import fr.warzou.databaselib.dbl.db.columns.Column;
import fr.warzou.databaselib.impl.data.AbstractData;
import fr.warzou.databaselib.impl.data.datatype.string.CharData;

import java.util.Optional;

public class WColumn<T extends Data<?>> implements Column<T> {

    private final String name;
    private final boolean primary;
    private final boolean unique;
    private final String encode;
    private final int size;
    private final T t;
    private final boolean isNull;

    private int id;

    public WColumn(String name, boolean primary, boolean unique, String encode, int size, T t, boolean isNull) {
        this.name = name;
        this.primary = primary;
        this.unique = unique;
        this.encode = encode;
        this.size = size;
        this.t = t;
        this.isNull = isNull;

        this.id = 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public T getData() {
        return this.t;
    }

    @Override
    public String getEncode() {
        return this.encode;
    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public boolean isPrimaryKey() {
        return this.primary;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public boolean isAutoIncrement() {
        return false;
    }

    protected void setId(int id) {
        this.id = id;
    }
}