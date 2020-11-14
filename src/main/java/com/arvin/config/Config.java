package com.arvin.config;

public class Config {

    /**
     * job schedule
     */
    public static final int THREAD_POOL_SIZE = 3;

    /**
     * job list
     */
    public static final String REDIS_KEY_JOBS = "jobs";

    public static final String REDIS_KEY_JOB_PRE = "job_";

    public static final String REDIS_KEY_JOB_SUCCESS = "success";

}
