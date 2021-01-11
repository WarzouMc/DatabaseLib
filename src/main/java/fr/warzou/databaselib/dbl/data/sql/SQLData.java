package fr.warzou.databaselib.dbl.data.sql;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.GlobalDataType;

public interface SQLData<V, T extends Data<V>> {

    boolean isPrimaryKey();

    boolean isUniqueKey();

    int getSize();

    T getData();

    V getDefaultValue();

    int getDefaultValueOption();

    boolean isNullable();

    boolean isAutoIncremented();

    GlobalDataType getType();

    boolean isNumeric();

    boolean isReal();

    boolean isInteger();

    boolean isString();

}
