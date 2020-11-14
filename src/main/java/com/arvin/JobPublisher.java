package com.arvin;

import com.arvin.config.Config;
import com.arvin.utils.IpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobPublisher {

    public JobPublisher() {
        initRedisIfNeed();
    }

    public void pub(AbstractJob job) {
        initRedisIfNeed();

        //TODO 1. write to db
        //TODO 2. pub to redis
    }

    public void reportSuccess(AbstractJob job) {
        String key = job.getJobId();
        String value = Config.REDIS_KEY_JOB_SUCCESS;
        //TODO set value to redis:SET key value
    }

    public void reportFail(AbstractJob job) {
        String key = job.getJobId();
        //TODO try {del key} catch(){}
    }

    public void reportAlive() {
        String ip = IpUtil.getIp();
        String timestamp = new SimpleDateFormat("").format(new Date());
        //TODO report alive。key存活30s：set keypre_$ip $timestamp ex 30
    }

    private void initRedisIfNeed() {
        //TODO 1.查询redis中是否存在必要的key
        //TODO 2.如果不存在，从DB中加载
    }
}
