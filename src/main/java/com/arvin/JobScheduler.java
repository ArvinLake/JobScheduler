package com.arvin;

import com.arvin.utils.BeanUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JobScheduler {
    private static final int POOL_SIZE = 3;
    private ScheduledExecutorService executorService;

    public JobScheduler() {
        init();
        report();
    }

    public void schedule(AbstractJob job) {
        long delay = job.getExecuteDate().getTime() - System.currentTimeMillis();
        executorService.schedule(() -> {
            job.execute();
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void init() {
        executorService = new ScheduledThreadPoolExecutor(POOL_SIZE, new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                String threadName = "job-scheduler-thread-" + counter.getAndIncrement();
                return new Thread(r, threadName);
            }
        });
    }

    /**
     * 报告存活状态
     */
    private void report() {
        executorService.scheduleAtFixedRate(() -> {
            BeanUtil.getBean(JobPublisher.class).reportAlive();
        }, 0, 5, TimeUnit.SECONDS);
    }
}
