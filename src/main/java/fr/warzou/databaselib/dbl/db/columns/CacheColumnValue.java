package fr.warzou.databaselib.dbl.db.columns;

import fr.warzou.databaselib.dbl.cache.Cache;
import fr.warzou.databaselib.dbl.cache.CacheMap;
import fr.warzou.databaselib.dbl.data.Data;

public interface CacheColumnValue<K extends UnregisteredCell<Data<?>>, V extends Cache<?>> extends CacheMap<K, V> {
}
