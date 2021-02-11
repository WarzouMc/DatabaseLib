package fr.warzou.databaselib.impl.async;

import fr.warzou.databaselib.dbl.async.Task;

import java.util.function.Consumer;

abstract class AbstractTask<T> implements Task<T> {

    protected final Object lock = new Object();
    protected boolean done;
    protected T value;
    protected Consumer<T> consumer;

    @Override
    public void complete(T value) {
        synchronized (this.lock) {
            if (this.done)
                throw new InternalError("This task is already done !");
            this.value = value;
            complete();
        }
    }

    protected abstract void complete();

    @Override
    public void whenComplete(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    @Override
    public T get() {
        synchronized (this.lock) {
            Runnable wait = () -> {
                try {
                    this.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            wait.run();
            return this.value;
        }
    }

    @Override
    public boolean isDone() {
        synchronized (this.lock) {
            return this.done;
        }
    }

}
