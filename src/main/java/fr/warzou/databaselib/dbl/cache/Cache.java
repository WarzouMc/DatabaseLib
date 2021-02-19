package fr.warzou.databaselib.dbl.cache;

import java.io.Serializable;
import java.util.Optional;

public interface Cache<V> extends Serializable {

    void put(V value);

    void set(V value);

    Optional<V> get();

}
