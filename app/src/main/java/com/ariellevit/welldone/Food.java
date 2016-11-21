package com.ariellevit.welldone;

/**
 * Created by אריאל on 20/11/2016.
 */

public class Food {
    private String name;
    private long nameId, time, dateCreatedMilli;


    public Food(String name, long time) {
        this.time = time;
        this.name = name;
        this.nameId = 0;
        this.dateCreatedMilli = 0;
    }

    public Food(String name, long time, long dateCreatedMilli, long nameId) {
        this.name = name;
        this.time = time;
        this.dateCreatedMilli = dateCreatedMilli;
        this.nameId = nameId;
    }

    public String getName() {
        return name;
    }

    public long getNameId() {
        return nameId;
    }

    public long getTime() {
        return time;
    }

    public long getDateCreatedMilli() {
        return dateCreatedMilli;
    }
}
