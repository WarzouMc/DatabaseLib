package fr.warzou.databaselib.impl.data.datatype.string;

import fr.warzou.databaselib.dbl.data.GlobalDataType;
import fr.warzou.databaselib.dbl.data.builder.SQLDataBuilder;
import fr.warzou.databaselib.dbl.data.subdata.string.StringData;
import fr.warzou.databaselib.impl.data.AbstractData;

abstract class AbstractStringData<N extends Number, M extends Number>
        extends AbstractData<String>
        implements StringData<N, M> {

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public GlobalDataType getGlobalType() {
        return GlobalDataType.STRING;
    }

    @Override
    public abstract SQLDataBuilder<String, ? extends AbstractStringData<N, M>> createSQLData();
}
