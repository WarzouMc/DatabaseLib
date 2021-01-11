package fr.warzou.databaselib.dbl.cache;

import java.util.Optional;

public interface CacheMap<K, V extends Cache<?>> {

    void put(K key, V value);

    void set(K key, V value);

    void clear();

    void remove(K key);

    Optional<V> get(K key);

}
