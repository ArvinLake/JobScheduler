package com.arvin.utils;

import org.testng.annotations.Test;

import java.util.concurrent.locks.LockSupport;

public class TestLongAdder {

    @Test
    public void test() {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (i == 5) {
                    LockSupport.park();
                    System.out.println("5 pass.");
                }
                if (i == 8) {
                    LockSupport.park();
                    System.out.println("8 pass.");
                }
            }
        });
        t.start();
        LockSupport.unpark(t);
    }


}
