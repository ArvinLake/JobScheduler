package com.arvin;

import com.arvin.config.Config;
import com.arvin.utils.IpUtil;
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
    public int raceToRun(AbstractJob job, String host) {
        String key = job.getJobId();
        String localValue = IpUtil.getIp();
        int got = 0;//TODO SETNX key localValue
        if (got > 0) {
            return 1;
        }

        //如果任务已经执行成功，则value为success，否则alue仍为执行该任务的ip
        String value = "";// GET key
        if (!Config.REDIS_KEY_JOB_SUCCESS.equals(value)) {
            if (isAlive(value)) {
                job.delay(5, TimeUnit.SECONDS);
            } else {
                publisher.reportFail(job);
            }
            scheduler.schedule(job);
        }
        return 0;
    }

    /**
     * 降低本机任务执行优先级
     * @param host
     */
    public void decrPriority(String host) {
        //TODO
    }

    public boolean isAlive(String host) {
        //TODO
        return false;
    }
}
