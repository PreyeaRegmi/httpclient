package com.preyearegmi.httpclient;

import java.util.concurrent.*;

/**
 * Created by preyea on 2/19/17.
 */
public final class TaskExecutor implements Executor {

    private static TaskExecutor taskExecutor = null;

    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 5;

    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 10;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private final BlockingQueue<Runnable> workQueue;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final ThreadFactory threadFactory;


    private TaskExecutor() {
        this.workQueue = new LinkedBlockingQueue<>();
        this.threadFactory = new MyThreadFactory();
        this.threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, this.workQueue, this.threadFactory);
    }

    protected static TaskExecutor getTaskExecutor() {
        if (taskExecutor == null)
            taskExecutor = new TaskExecutor();
        return taskExecutor;
    }


    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new IllegalArgumentException("Task to be execute cannot be null");
        }
        this.threadPoolExecutor.execute(command);
    }


    private class MyThreadFactory implements ThreadFactory {

        private static final String THREAD_NAME = "HTTPClient_Spawned_Thread: ";

        private int counter = 0;

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(runnable, THREAD_NAME + counter++);
            return t;
        }
    }
}
