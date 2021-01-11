package fr.warzou.databaselib.impl.data.datatype.string;

import fr.warzou.databaselib.dbl.data.GlobalDataType;
import fr.warzou.databaselib.dbl.data.builder.SQLDataBuilder;
import fr.warzou.databaselib.dbl.data.subdata.LengthData;
import fr.warzou.databaselib.impl.data.sqldatabuilder.SQLDataBuilderImpl;

public class CharData extends AbstractStringData<Byte, Short> {

    private byte signedLength = Byte.MAX_VALUE;

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public GlobalDataType getGlobalType() {
        return GlobalDataType.STRING;
    }

    @Override
    public SQLDataBuilder<String, CharData> createSQLData() {
        SQLDataBuilder<String, CharData> builder = new SQLDataBuilderImpl<>(String.class, this, "char");
        return builder;
    }

    @Override
    public LengthData<String, Byte, Short> setLength(Short length) {
        this.signedLength = ((Number) length).byteValue();
        return this;
    }

    @Override
    public Byte getSignedLength() {
        return this.signedLength;
    }

    @Override
    public Short getUnsignedLength() {
        return (short) Byte.toUnsignedInt(this.signedLength);
    }

}
