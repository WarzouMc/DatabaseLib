package fr.warzou.databaselib.impl.data.datatype.string;

import fr.warzou.databaselib.dbl.data.GlobalDataType;
import fr.warzou.databaselib.dbl.data.subdata.LengthData;

import java.util.Optional;

public class CharData extends AbstractStringData<Byte, Short> {

    private byte signedLength;

    public CharData(Short length) {
        this.signedLength = length.byteValue();
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public GlobalDataType getGlobalType() {
        return GlobalDataType.STRING;
    }

    @Override
    public Optional<String> getDefault() {
        return Optional.empty();
    }

    @Override
    public int getDefaultValueOption() {
        return 0;
    }

    @Override
    public boolean isPrimaryKey() {
        return false;
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public boolean hasDefaultValue() {
        return false;
    }

    @Override
    public boolean hasDefaultValueOption() {
        return false;
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
