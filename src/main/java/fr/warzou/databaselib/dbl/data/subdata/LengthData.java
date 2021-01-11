package fr.warzou.databaselib.dbl.data.subdata;

import fr.warzou.databaselib.dbl.data.Data;

public interface LengthData<T, N extends Number, M extends Number> extends Data<T> {

    LengthData<T, N, M> setLength(M length);

    N getSignedLength();

    M getUnsignedLength();

}
