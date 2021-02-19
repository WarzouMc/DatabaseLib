package fr.warzou.databaselib.impl.data;

import fr.warzou.databaselib.dbl.data.Data;

import java.util.Optional;

public class DefaultSqlData<T> {

    private final AbstractData<T> abstractData;

    public DefaultSqlData(Data<T> data, String name) {
        Optional<AbstractData<T>> abstractDataOptional = AbstractData.ofData(data, name);

        if (!abstractDataOptional.isPresent()) {
            this.abstractData = null;
            return;
        }

        this.abstractData = abstractDataOptional.get();
    }
}
