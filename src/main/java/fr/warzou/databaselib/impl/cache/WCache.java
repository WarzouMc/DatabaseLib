package fr.warzou.databaselib.impl.cache;

import fr.warzou.databaselib.dbl.cache.Cache;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;

public class WCache<T> implements Cache<T> {

    private final long lifeTime;

    private long since;
    private T t;

    public WCache(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    @Override
    public void put(@NotNull T value) {
        if (isLiving())
            return;
        this.since = Instant.now().toEpochMilli();
        this.t = value;
    }

    @Override
    public void set(@NotNull T value) {
        if (!isLiving()) {
            this.since = -1;
            this.t = null;
            return;
        }
        this.since = Instant.now().toEpochMilli();
        this.t = value;
    }

    @Override
    public Optional<T> get() {
        if (!isLiving()) {
            this.since = -1;
            this.t = null;
            return Optional.empty();
        }
        return Optional.of(this.t);
    }

    public boolean isLiving() {
        long now = Instant.now().toEpochMilli();
        return this.t != null && this.since != -1 && this.since + this.lifeTime < now;
    }
}
