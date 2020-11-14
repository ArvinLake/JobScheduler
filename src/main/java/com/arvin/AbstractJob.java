package com.arvin;

import com.arvin.config.Config;
import com.arvin.utils.BeanUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.Assert;

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
        return Config.REDIS_KEY_JOB_PRE + getName() + "_" + getBatch();
    }

    public final void execute() {
        JobRacer racer = BeanUtil.getBean(JobRacer.class);
        JobPublisher publisher = BeanUtil.getBean(JobPublisher.class);
        /**
         * 抢不到执行权限则返回
         */
        if (!racer.raceToRun(this)) {
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
            publisher.reportRunning(this);
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
        long delaySeconds = timeUnit.toSeconds(time);
        Assert.isTrue(delaySeconds < Integer.MAX_VALUE, "延迟时间超过了2147483647秒！");

        executeDate = DateUtils.addSeconds(executeDate, (int) delaySeconds);
    }

    public String toMsg() {
        return name + "##" + batch + "##" + executeDate.getTime();
    }

    public static AbstractJob parse(String msg) throws Exception {
        String[] parts = msg.split("##");
        String name = parts[0];
        long batch = Long.valueOf(parts[1]);
        Date executeDate = new Date(Long.valueOf(parts[2]));

        Class<?> jobClass = Class.forName(name);
        return (AbstractJob) jobClass.getDeclaredConstructor(AbstractJob.class).newInstance(name, batch, executeDate);
    }

    public long getExpireSeconds() {
        return (executeDate.getTime() - System.currentTimeMillis()) / 1000;
    }

    public abstract boolean couldRun();

    public abstract boolean retryIfSkiped();

    public abstract void run();

    public abstract AbstractJob nextJob();
}
