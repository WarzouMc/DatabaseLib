package fr.warzou.databaselib.dbl.async;

import java.util.function.Supplier;

/**
 * @author Redstonneur1256
 */
public interface ThreadPool {

    <T> Task<T> perform(Supplier<T> supplier);

    void perform(Runnable runnable);

    void stop();

    boolean isActive();

}
