package com.arvin;

import com.arvin.config.Config;
import com.arvin.config.JobStatus;
import com.arvin.utils.DBUtil;
import com.arvin.utils.IpUtil;
import com.arvin.utils.RedisUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobPublisher {

    public JobPublisher() {
        initRedisIfNeed();
    }

    public void pub(AbstractJob job) {
        initRedisIfNeed();

        String msg = job.toMsg();
        //1. write to db
        DBUtil.save(job, JobStatus.INIT);
        //2. pub to redis
        RedisUtil.lpush(Config.REDIS_KEY_JOBS, msg);
    }

    public void reportRunning(AbstractJob job) {
        DBUtil.update(job, JobStatus.RUNNING);
    }

    public void reportSuccess(AbstractJob job) {
        String key = job.getJobId();
        String value = Config.REDIS_KEY_JOB_SUCCESS;
        //1.set value to redis:SET key value
        RedisUtil.set(key, value, job.getExpireSeconds());
        //2.update to DB
        DBUtil.update(job, JobStatus.SUCCESS);
    }

    public void reportFail(AbstractJob job) {
        String key = job.getJobId();
        //1.del key
        RedisUtil.del(key);
        //2.update to DB
        DBUtil.update(job, JobStatus.FAILED);
    }

    public void reportAlive() {
        String key = Config.REDIS_KEY_HOST_ALIVE_PRE + IpUtil.getIp();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //report alive--key存活30s：set key $timestamp ex $Config.HOST_ALIVE_SECONDS
        RedisUtil.set(key, timestamp, Config.HOST_ALIVE_SECONDS);
    }

    private void initRedisIfNeed() {
        String key = Config.REDIS_KEY_JOBS;
        //1.查询redis中是否存在key
        if (RedisUtil.exists(key)) {
            return;
        }
        //2.如果不存在，从DB中加载
        List<String> jobs = DBUtil.loadJobs();
        RedisUtil.lpush(key, jobs.toArray(new String[jobs.size()]));
    }
}
