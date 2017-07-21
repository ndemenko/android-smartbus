package com.test.testgreen.event;

/**
 * Created by Nick Demenko on 17.03.2017.
 */

public class MyDataEvent {
    public final int message;

    public MyDataEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
