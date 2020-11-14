package com.arvin;

import com.arvin.jobs.UpdateProductJob;
import com.arvin.utils.BeanUtil;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        AbstractJob job = new UpdateProductJob(1, new Date());
        BeanUtil.getBean(JobPublisher.class).pub(job);
        // JobReciver订阅了任务队列，该任务pub到任务队列之后JobReciver会收到任务推送，随即开始调度任务
    }
}
