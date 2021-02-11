package fr.warzou.databaselib.dbl.async;

import java.util.function.Consumer;

/**
 * @author Redstonneur1256
 */
public interface Task<T> {

    void complete(T value);

    void whenComplete(Consumer<T> consumer);

    T get();

    boolean isDone();

}
