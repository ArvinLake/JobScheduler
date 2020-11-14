package com.arvin;

import com.arvin.config.Config;
import com.arvin.utils.IpUtil;
import com.arvin.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class JobRacer {

    @Autowired
    JobScheduler scheduler;
    @Autowired
    JobPublisher publisher;

    /**
     * 查询redis，该任务是否可以执行
     * @param job
     * @return
     */
    public boolean raceToRun(AbstractJob job) {
        String key = job.getJobId();
        String localValue = IpUtil.getIp();
        boolean got = RedisUtil.setIfAbsent(key, localValue, job.getExpireSeconds());
        if (got) {
            return true;
        }

        //如果任务已经执行成功，则value为success，否则alue仍为执行该任务的ip
        String value = RedisUtil.get(key);
        if (!Config.REDIS_KEY_JOB_SUCCESS.equals(value)) {
            if (isAlive(value)) {
                job.delay(5, TimeUnit.SECONDS);
            } else {
                publisher.reportFail(job);
            }
            scheduler.schedule(job);
        }
        return false;
    }

    public boolean isAlive(String host) {
        String key = Config.REDIS_KEY_HOST_ALIVE_PRE + host;
        return RedisUtil.exists(key);
    }
}
