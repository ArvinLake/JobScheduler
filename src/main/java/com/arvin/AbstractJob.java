package com.arvin;

import com.arvin.config.Config;
import com.arvin.utils.BeanUtil;
import com.arvin.utils.IpUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class AbstractJob {
    private String name;
    private long batch;
    private Date executeDate;

    public AbstractJob(String name, long batch, Date executeDate) {
        this.name = name;
        this.batch = batch;
        this.executeDate = executeDate;
    }

    public final String getName() {
        return name;
    }

    public final long getBatch() {
        return batch;
    }

    public final Date getExecuteDate() {
        return executeDate;
    }

    public String getJobId() {
        return Config.REDIS_KEY_JOB_PRE + getName() + "_" + getBatch() + "_" + getExecuteTimes();
    }

    public final void execute() {
        JobRacer racer = BeanUtil.getBean(JobRacer.class);
        JobPublisher publisher = BeanUtil.getBean(JobPublisher.class);
        /**
         * 抢不到执行权限则返回
         */
        if (racer.raceToRun(this, IpUtil.getIp()) <= 0) {
            return;
        }
        try {
            if (!couldRun()) {
                if (retryIfSkiped()) {
                    delay(5, TimeUnit.SECONDS);
                    publisher.pub(this);
                }
                return;
            }
            racer.decrPriority(IpUtil.getIp());
            run();
            publisher.reportSuccess(this);
            publisher.pub(nextJob());
        } catch (Exception e){
            e.printStackTrace();

            publisher.reportFail(this);
            publisher.pub(this);
        }
    }

    public void delay(long time, TimeUnit timeUnit) {
        //TODO
    }

    public abstract boolean couldRun();

    public abstract boolean retryIfSkiped();

    public abstract void run();

    public abstract int getExecuteTimes();

    public abstract AbstractJob nextJob();
}
