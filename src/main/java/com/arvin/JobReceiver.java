package com.arvin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class JobReceiver {

    @Autowired
    JobScheduler scheduler;

    public void receive() {
        while (true) {
            String msg = "";//TODO
            if (StringUtils.isEmpty(msg)) {
                continue;
            }
            AbstractJob job = parse(msg);
            scheduler.schedule(job);
        }
    }

    private AbstractJob parse(String msg) {
        //TODO
        return null;
    }
}
