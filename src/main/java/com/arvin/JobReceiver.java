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
            try {
                scheduler.schedule(AbstractJob.parse(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
