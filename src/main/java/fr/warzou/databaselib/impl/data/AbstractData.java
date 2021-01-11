package fr.warzou.databaselib.impl.data;

import fr.warzou.databaselib.dbl.data.Data;
import fr.warzou.databaselib.dbl.data.builder.SQLDataBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractData<T> implements Data<T> {

    private String sqlName;

    protected AbstractData<T> withSQLName(@NotNull String sqlName) {
        this.sqlName = sqlName;
        return this;
    }

    @Override
    public String sqlName() {
        return this.sqlName;
    }

    @Override
    public abstract SQLDataBuilder<T, ? extends AbstractData<T>> createSQLData();

    protected static <T1> Optional<AbstractData<T1>> ofData(@NotNull Data<T1> data, @NotNull String sqlName) {
        if (!(data instanceof AbstractData))
            return Optional.empty();
        AbstractData<T1> abstractData = ((AbstractData<T1>) data).withSQLName(sqlName);
        return Optional.of(abstractData);
    }

}
