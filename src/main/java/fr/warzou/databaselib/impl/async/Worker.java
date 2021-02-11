package fr.warzou.databaselib.impl.async;

import java.util.concurrent.ThreadFactory;

class Worker {

    protected final Object lock;
    private final QueuedThreadPool queuedThreadPool;
    private final int id;
    private final ThreadFactory threadFactory;

    private Thread thread;
    protected Runnable runnable;

    Worker(QueuedThreadPool threadPool) {
        this.lock = new Object();
        this.queuedThreadPool = threadPool;
        this.id = Math.toIntExact(Math.round(Math.random() * 1000));
        this.threadFactory = threadPool.getThreadFactory();
    }

    protected void start() {
        this.thread = this.threadFactory.newThread(this::run);
        this.thread.setName("Worker::" + this.id);
    }

    protected void next(Runnable runnable) {
        synchronized (this.lock) {
            if (this.runnable != null) {
                this.queuedThreadPool.getRunnableQueue().add(runnable);
                return;
            }
            this.runnable = runnable;
            this.lock.notifyAll();
        }
    }

    protected void stop() {
        this.thread.interrupt();
    }

    private void run() {
        while (this.thread != null && !this.thread.isInterrupted()) {
            synchronized (this.lock) {
                if (this.runnable == null) {
                    Runnable wait = () -> {
                        try {
                            this.lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                    wait.run();
                }
                this.runnable.run();
                this.runnable = null;

                synchronized (this.queuedThreadPool.getRunnableQueue()) {
                    Runnable queue;
                    if ((queue = this.queuedThreadPool.getRunnableQueue().poll()) == null)
                        continue;
                    next(queue);
                }
            }
        }
    }
}
