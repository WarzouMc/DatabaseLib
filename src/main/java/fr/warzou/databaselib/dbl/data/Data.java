package fr.warzou.databaselib.dbl.data;

import java.util.Optional;

public interface Data<T> {

    Class<T> getType();

    String sqlName();

    GlobalDataType getGlobalType();

    Optional<T> getDefault();

    /**
     *
     * @return -1 if hasDefaultValueOption is false
     */
    int getDefaultValueOption();

    boolean isPrimaryKey();

    boolean isUnique();

    boolean isNullable();

    boolean hasDefaultValue();

    boolean hasDefaultValueOption();

}
