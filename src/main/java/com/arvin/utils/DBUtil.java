package com.arvin.utils;

import com.arvin.AbstractJob;
import com.arvin.config.JobStatus;

import java.util.List;

public class DBUtil {

    public static boolean save(AbstractJob job, JobStatus status) {
        //TODO save(job.id, job.msg, status)
        return false;
    }

    public static boolean update(AbstractJob job, JobStatus status) {
        //TODO update(job.id, job.msg, status)
        return false;
    }

    public static List<String> loadJobs() {
        //TODO get(status=INIT,RUNNING,FAILED)
        return null;
    }
}
