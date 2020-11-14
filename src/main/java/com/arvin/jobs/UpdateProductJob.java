package com.arvin.jobs;

import com.arvin.AbstractJob;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateProductJob extends AbstractJob {
    private boolean retryIfSkiped = false;

    public UpdateProductJob(long batch, Date executeDate) {
        super("com.arvin.jobs.UpdateProductJob", batch, executeDate);
    }

    @Override
    public boolean couldRun() {
        // TODO 判断本地状态是否执行
        // TODO 如果本次不执行，并且在执行下一个任务之前需要重试，则retryIfSkiped设为true
        return false;
    }

    @Override
    public boolean retryIfSkiped() {
        return retryIfSkiped;
    }

    @Override
    public void run() {
        // TODO 业务逻辑
    }

    @Override
    public AbstractJob nextJob() {
        return null;
    }
}
