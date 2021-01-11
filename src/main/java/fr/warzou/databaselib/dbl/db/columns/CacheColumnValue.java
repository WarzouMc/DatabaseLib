package fr.warzou.databaselib.dbl.db.columns;

import fr.warzou.databaselib.dbl.cache.Cache;
import fr.warzou.databaselib.dbl.cache.CacheMap;

public interface CacheColumnValue<K extends UnregisteredCell<?>, V extends Cache<?>> extends CacheMap<K, V> {
}
