package fr.warzou.databaselib.impl.data.sqldatabuilder;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.GlobalDataType;

public class SQLLengthDataBuilderImpl<V, T extends Data<V>, N extends Number, M extends Number> extends SQLDataBuilderImpl<V, T> {

    protected final N signedLength;
    protected final M unsignedLength;

    protected SQLLengthDataBuilderImpl(SQLDataBuilderImpl<V, T> base, N signedLength, M unsignedLength) {
        this(base.valueType, base.data, base.name, signedLength, unsignedLength);
        this.primaryKey = base.primaryKey;
        this.uniqueKey = base.uniqueKey;
        this.nullable = base.nullable;
        this.autoIncrement = base.autoIncrement;
        this.defaultValue = base.defaultValue;
        this.defaultValueOption = base.defaultValueOption;
    }

    protected SQLLengthDataBuilderImpl(Class<V> valueType, T data, String name, N signedLength, M unsignedLength) {
        super(valueType, data, name);
        this.signedLength = signedLength;
        this.unsignedLength = unsignedLength;
    }

    protected static boolean accept(GlobalDataType globalDataType) {
        return globalDataType == GlobalDataType.STRING;
    }

}
