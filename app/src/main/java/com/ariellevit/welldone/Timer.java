package com.ariellevit.welldone;



public class Timer {
    private long timerId, start, time;
    private String name;

    public Timer(long timerId, long start, String name, long time) {
        this.timerId = timerId;
        this.start = start;
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public long getTimerId() {
        return timerId;
    }

    public long getStart() {
        return start;
    }

    public long getTime() {
        return time;
    }
}
