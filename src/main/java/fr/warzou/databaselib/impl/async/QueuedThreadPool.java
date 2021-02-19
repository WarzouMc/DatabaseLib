package fr.warzou.databaselib.impl.async;

import fr.warzou.databaselib.dbl.async.Task;
import fr.warzou.databaselib.dbl.async.ThreadPool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueuedThreadPool implements ThreadPool {

    private final ThreadFactory threadFactory;
    private final ConcurrentLinkedQueue<Runnable> queue;
    private Worker worker;

    public QueuedThreadPool() {
        this(r -> {
            Consumer<Thread> threadConsumer = thread -> r.run();
            Thread thread = new Thread(() -> threadConsumer.accept(Thread.currentThread()));
            thread.setDaemon(true);
            thread.start();
            return thread;
        });
    }

    public QueuedThreadPool(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public <T> Task<T> perform(Supplier<T> supplier) {
        Task<T> task = new ImplTask<>();
        perform(() -> task.complete(supplier.get()));
        return task;
    }

    @Override
    public void perform(Runnable runnable) {
        if (this.worker == null) {
            this.worker = new Worker(this);
            this.worker.start();
        }
        this.worker.next(runnable);
    }

    @Override
    public void stop() {
        if (this.worker == null)
            return;
        this.worker.stop();
    }

    protected ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    protected Queue<Runnable> getRunnableQueue() {
        return this.queue;
    }

    @Override
    public boolean isActive() {
        synchronized (this.worker.lock) {
            return this.worker.runnable != null;
        }
    }

    private static class ImplTask<T> extends AbstractTask<T> {

        @Override
        protected void complete() {
            synchronized (this.lock) {
                ThreadFactory threadFactory = r -> {
                    Consumer<Thread> threadConsumer = thread -> r.run();
                    Thread thread = new Thread(() -> threadConsumer.accept(Thread.currentThread()));
                    thread.setDaemon(true);
                    thread.start();
                    return thread;
                };

                if (this.consumer == null)
                    this.consumer = t -> {};

                Consumer<T> consumer = item -> threadFactory.newThread(() -> this.consumer.accept(this.value));
                consumer.accept(this.value);

                this.done = true;
                this.lock.notifyAll();
            }
        }

    }
}
