package com.arvin.config;

public class Config {

    /**
     * job schedule
     */
    public static final int THREAD_POOL_SIZE = 3;

    /**
     * host status
     */
    public static final int HOST_ALIVE_SECONDS = 15;
    public static final int HOST_ALIVE_REPORT_GAP_SECONDS = 5;
    public static final String REDIS_KEY_HOST_ALIVE_PRE = "alive_";

    /**
     * job list
     */
    public static final String REDIS_KEY_JOBS = "jobs";

    public static final String REDIS_KEY_JOB_PRE = "job_";

    public static final String REDIS_KEY_JOB_SUCCESS = "success";

}
